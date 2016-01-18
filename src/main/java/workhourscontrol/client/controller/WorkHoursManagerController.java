package workhourscontrol.client.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.controlsfx.dialog.Dialogs;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import workhourscontrol.client.MainApp;
import workhourscontrol.client.component.TabelaRegistroHora;
import workhourscontrol.client.component.TabelaTotalizador;
import workhourscontrol.client.component.TabelaTotalizadorSemanal;
import workhourscontrol.client.model.RegistroHoraObservable;
import workhourscontrol.client.service.ControleHorasService;
import workhourscontrol.client.service.IntegracaoService;
import workhourscontrol.client.util.FXMLLoaderFactory;
import workhourscontrol.client.util.FileHelper;
import workhourscontrol.entity.RegistroHora;

public class WorkHoursManagerController {

	private Logger logger = Logger.getLogger(WorkHoursManagerController.class);

	private MainApp mainApp;

	private IntegracaoService integracaoService;
	private ControleHorasService controleHorasService;

	@FXML private AnchorPane workHoursManagerLayout;

	/** Tabelas da aplicação */
	@FXML private TabelaRegistroHora tabelaRegistroHora;
	@FXML private TabelaTotalizador tabelaTotalizador;
	@FXML private TabelaTotalizadorSemanal tabelaTotalizadorSemanal;

	@FXML private Label labelTotal;
	@FXML private Label horasRestantesLabel;
	@FXML private Label saldoHorasLabel;

	private LocalDate ultimaDataRegistro;

	@FXML
	public void initialize() {

		integracaoService = IntegracaoService.getInstance();
		controleHorasService = ControleHorasService.getInstance();


		tabelaRegistroHora.adicionarEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.PRIMARY) && event.getTarget().toString().contains("TableColumn") && !event.getTarget().toString().contains("Header")) {
					if (event.getClickCount() >= 2) {
						RegistroHoraObservable itemSelecionado = tabelaRegistroHora.getItemSelecionado();
						initHoursEdit(itemSelecionado);
					}
				}
			}

		});

		tabelaTotalizador.setOnSelecionarItem(event -> labelTotal.setText(tabelaTotalizador.getTotalSelecionado().toString()));

	}

	private void initHoursEdit(RegistroHoraObservable itemSelecionado) {

		try {

			FXMLLoader loader = FXMLLoaderFactory.createLoader("view/EditHour.fxml");
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Horas");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(workHoursManagerLayout.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Define a pessoa no controller.
            EditHourController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setControllerPai(this);
            controller.setRegistroHora(itemSelecionado);
            controller.setOnSave(() -> {
            	tabelaTotalizador.atualizarTotalizador();
            	tabelaTotalizadorSemanal.atualizarTotalizador();
            	atualizarLabelHorasRestantes();
            	atualizarLabelSaldoHoras();
            	this.mainApp.setNotSaved();
            });

            // Mostra a janela e espera até o usuário fechar.
            mainApp.getPrimaryStage().hide();
            dialogStage.showAndWait();
            mainApp.getPrimaryStage().show();

		} catch (IOException e) {
			logger.error("Ocorreu um erro ao carregar tela de edição", e);
			throw new RuntimeException(e);
		}
	}

	/*
	 * Abre modal para novo registro
	 */
	private void initHoursEdit() {
		initHoursEdit(null);
	}

	@FXML
	public void handleNovoRegistro() {
		initHoursEdit();
	}

	public void adicionarRegistro(RegistroHoraObservable registroHora) {
		mainApp.getRegistrosHora().add(registroHora);
	}

	@FXML
	public void handleDeletarRegistro() {
		int selectedIndex = tabelaRegistroHora.getSelectedIndex();

    	if (selectedIndex >= 0) {
    		tabelaRegistroHora.getItems().remove(selectedIndex);
    		tabelaTotalizador.atualizarTotalizador();
    		tabelaTotalizadorSemanal.atualizarTotalizador();
    		atualizarLabelHorasRestantes();
    		atualizarLabelSaldoHoras();
    	} else {
    		//Nada selecionado
    		Dialogs.create()
    			.title("Nenhuma seleção")
    			.masthead("Nenhum registro selecionado")
    			.message("Por favor, selecione um registro na tabela.")
    			.showWarning();
    	}
	}

	@FXML
	public void handleSincronizar() {
		List<RegistroHoraObservable> items = tabelaRegistroHora.getItems();

    	if (!items.isEmpty()) {

    		List<RegistroHora> itensNaoLancados = items.stream()
    											.filter(t -> !t.isLancado() && StringUtils.isNotBlank(t.getHoraFim()))
    											.collect(Collectors.toList());



    		integracaoService.sincronizarRegistrosHora(itensNaoLancados);
    		this.mainApp.setNotSaved();

    	}
	}

	/**
	 * Sincroniza todos os itens com planilha
	 */
	@FXML
	public void handleSincronizarPlanilha() {
		List<RegistroHora> items = new ArrayList<RegistroHora>(tabelaRegistroHora.getItems());

    	if (!items.isEmpty()) {

    		File arquivo = obterArquivoPlanilha();

    		if (Objects.nonNull(arquivo)) {
    			integracaoService.sincronizarRegistrosHoraComPlanilha(items, arquivo);
    			Dialogs.create()
	    			.title("Mensagem sucesso")
	    			.masthead("Importado com sucesso...")
	    			.message("Sincronização realizada com sucesso!!.")
	    			.showInformation();
    		}
    	}
	}

	/**
	 * Caso haja uma planilha default, não abre janela para escolher
	 */
	private File obterArquivoPlanilha() {
		String urlDefaultPlanilha = MainApp.configuracoesAplicacao.getUrlPlanilha();
		if (StringUtils.isNotBlank(urlDefaultPlanilha)) {
			return new File(urlDefaultPlanilha);
		}

		return FileHelper.chooseFileForOpening(mainApp.getPrimaryStage(), "*.xlsx", "Planílhas de extensão .xlsx");
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		this.tabelaRegistroHora.setItems(this.mainApp.getRegistrosHora());
		this.tabelaTotalizador.setListaRegistroHoras(this.mainApp.getRegistrosHora());
		this.tabelaTotalizadorSemanal.setListaRegistroHoras(this.mainApp.getRegistrosHora());

		this.mainApp.getRegistrosHora().addListener(new ListChangeListener<RegistroHoraObservable>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends RegistroHoraObservable> c) {
				tabelaTotalizador.atualizarTotalizador();  //errado
				tabelaTotalizadorSemanal.atualizarTotalizador();  //errado
				atualizarLabelHorasRestantes();
				atualizarLabelSaldoHoras();
			}
		});


	}

	/**
	 * Exibe quantas horas faltam para completar 8 horas de trabalho
	 */
	private void atualizarLabelHorasRestantes() {

		// Apenas registro para a data de hoje
		final FilteredList<RegistroHoraObservable> registrosHoje = this.mainApp.getRegistrosHora().filtered(new Predicate<RegistroHoraObservable>() {
			@Override
			public boolean test(RegistroHoraObservable t) {
				try {
					return  t.getData().equals(LocalDate.now());
				} catch (ParseException e) {
					logger.error("Ocorreu um erro ao atualizar label de horas restantes. ", e);
					throw new RuntimeException(e);
				}
			}
		});

		double horaRestante = controleHorasService.calcularHorasTrabalhoRestantes(registrosHoje);
		horasRestantesLabel.setText(workhourscontrol.client.util.StringUtils.formatarRetornoDuracaoComoHoras(horaRestante));
	}

	private void atualizarLabelSaldoHoras() {
		final List<Double> listaTotais = tabelaTotalizador.getTotaisMenosHoje();
		saldoHorasLabel.setText(controleHorasService.calcularSaldoHoras(listaTotais));
	}



	public LocalDate getUltimaDataRegistro() {
		return ultimaDataRegistro;
	}

	public void setUltimaDataRegistro(LocalDate ultimaDataRegistro) {
		this.ultimaDataRegistro = ultimaDataRegistro;
	}

}

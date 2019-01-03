package workhourscontrol.client.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import workhourscontrol.client.MainApp;
import workhourscontrol.client.component.LabelAviso;
import workhourscontrol.client.component.TabelaRegistroHora;
import workhourscontrol.client.component.TabelaTotalizador;
import workhourscontrol.client.component.TabelaTotalizadorSemanal;
import workhourscontrol.client.model.RegistroHoraObservable;
import workhourscontrol.client.service.ControleHorasService;
import workhourscontrol.client.service.IntegracaoService;
import workhourscontrol.client.thread.TaskExecutorService;
import workhourscontrol.client.util.ClipboardUtils;
import workhourscontrol.client.util.FXMLLoaderFactory;
import workhourscontrol.client.util.FileHelper;
import workhourscontrol.entity.RegistroHora;

public class WorkHoursManagerController {

	private Logger logger = Logger.getLogger(WorkHoursManagerController.class);

	private MainApp mainApp;

	private IntegracaoService integracaoService;
	private ControleHorasService controleHorasService;

	@FXML private AnchorPane workHoursManagerLayout;

	/** Tabelas da aplicaçãoo */
	@FXML private TabelaRegistroHora tabelaRegistroHora;
	@FXML private TabelaTotalizador tabelaTotalizador;
	@FXML private TabelaTotalizadorSemanal tabelaTotalizadorSemanal;

	@FXML private Label labelTotal;
	@FXML private Label horasRestantesLabel;
	@FXML private LabelAviso saldoHorasLabel;

	@FXML private Button btnSalvarPlanilha;
	@FXML private Button btnAbrirPlanilha;
	@FXML private Button btnSincronizar;

	@FXML private Label saldoHorasDescricaoLabel;

	@FXML private TextField filtroObs;

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

		// Atribui evento para totalizar itens selecionados nas tabelas
		tabelaTotalizador.setOnSelecionarItem(event -> labelTotal.setText(tabelaTotalizador.getTotalSelecionado().toString()));
		tabelaRegistroHora.setOnSelecionarItem(event -> {
			final Double totalSelecionado = tabelaRegistroHora.getTotalSelecionado(r -> {
				return controleHorasService.calcularDuracaoTrabalho(r);
			});
			labelTotal.setText(getValorTotalFormatado(totalSelecionado));
			
		});

		saldoHorasLabel.setTesteDanger(valor -> valor < 0);
		saldoHorasLabel.setTesteWarning(valor -> valor >= 5);

		// Inicializando filtro
		filtroObs.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				tabelaRegistroHora.getItems().setPredicate( registro -> {

					if (StringUtils.isBlank(newValue)) {
						return true;
					}

					if (workhourscontrol.util.StringUtils.containsNice(registro.getObservacao(), newValue)) {
						return true;
					}

					return false;

				});

			}
		});

		// Evento ao pressionar Ctrl + c
		tabelaRegistroHora.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.isControlDown() && event.getCode().equals(KeyCode.C)) {
					ClipboardUtils.adicionarStringEmClipboard(labelTotal.getText());
				}

			}
		});

		initializeButtons();
		
	}

	private String getValorTotalFormatado(final Double totalSelecionado) {
		if (MainApp.configuracoesAplicacao.isContabilizarHorasFormatado()) {
			return workhourscontrol.util.StringUtils.formatarRetornoDuracaoComoHoras(totalSelecionado);
		} 
		return workhourscontrol.util.StringUtils.formatarRetornoDuracao(totalSelecionado);
	}

	private void initializeButtons() {
		if (MainApp.configuracoesAplicacao.getUrlPlanilha() == null) {
			btnAbrirPlanilha.setVisible(false);
			btnSalvarPlanilha.setVisible(false);
		}
		if (MainApp.configuracoesAplicacao.getLoginAplicacao() == null) {
			btnSincronizar.setVisible(false);
		}
		
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

            // Mostra a janela e espera até o usu�rio fechar.
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
		List<RegistroHoraObservable> selectedItems = tabelaRegistroHora.getSelectedItems();

    	if (!selectedItems.isEmpty()) {
    		this.mainApp.getRegistrosHora().removeAll(selectedItems);
    		tabelaTotalizador.atualizarTotalizador();
    		tabelaTotalizadorSemanal.atualizarTotalizador();
    		atualizarLabelHorasRestantes();
    		atualizarLabelSaldoHoras();
    	} else {
    		//Nada selecionado
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("Nenhuma seleção");
    		alert.setHeaderText("Nenhum registro selecionado");
    		alert.setContentText("Por favor, selecione um registro na tabela.");
    		alert.showAndWait();
    	}
	}

	@FXML
	public void handleSincronizar() {
		List<RegistroHoraObservable> items = tabelaRegistroHora.getItems();

    	if (!items.isEmpty()) {

    		List<RegistroHora> itensNaoLancados = items.stream()
    											.filter(t -> !t.isLancado() && StringUtils.isNotBlank(t.getHoraFim()))
    											.collect(Collectors.toList());



    		Task<Void> task = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					integracaoService.sincronizarRegistrosHora(itensNaoLancados);
					return null;
				}

				protected void done() {
					mainApp.setNotSaved();
				};

				protected void failed() {
					logger.error("Ocorreu um erro ao sincronizar registros de hora. ", getException());
				};

    		};

    		btnSincronizar.disableProperty().bind(task.runningProperty());
    		TaskExecutorService.executeTask(task);

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

    			Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						integracaoService.sincronizarRegistrosHoraComPlanilha(items, arquivo);
						return null;
					}

					@Override
					protected void succeeded() {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Mensagem Sucessot");
						alert.setHeaderText("Importado com sucesso...");
						alert.setContentText("Sincronização realizada com sucesso!!");

						alert.showAndWait();

						super.succeeded();
					}
				};

				btnSalvarPlanilha.disableProperty().bind(task.runningProperty());

				TaskExecutorService.executeTask(task);

    		}
    	}
	}

	public void handleAbrirPlanilha() throws MalformedURLException {
		File arquivo = obterArquivoPlanilha();
		mainApp.getHostServices().showDocument(arquivo.toURI().toURL().toExternalForm().replace("%20", " "));
	}

	/**
	 * Caso haja uma planilha default, n�o abre janela para escolher
	 */
	private File obterArquivoPlanilha() {
		String urlDefaultPlanilha = MainApp.configuracoesAplicacao.getUrlPlanilha();
		if (StringUtils.isNotBlank(urlDefaultPlanilha)) {
			return new File(urlDefaultPlanilha);
		}

		return FileHelper.chooseFileForOpening(mainApp.getPrimaryStage(), "*.xlsx", "Planilhas de extensão .xlsx");
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
		horasRestantesLabel.setText(workhourscontrol.util.StringUtils.formatarRetornoDuracaoComoHoras(horaRestante));

	}

	private void atualizarLabelSaldoHoras() {
		final List<Double> listaTotais = tabelaTotalizador.getTotaisMenosHoje();
		double saldoHorasMesAtual = controleHorasService.calcularSaldoHoras(listaTotais);

		Double saldoHorasAnterior = getSaldoHorasMesAnterior();

		// Exibe "*" quando n�o for poss�vel obter o saldo de horas do servidor
		if (Objects.isNull(saldoHorasAnterior)) {
			saldoHorasDescricaoLabel.setText("*" + saldoHorasDescricaoLabel.getText());
		} else {
			saldoHorasMesAtual += saldoHorasAnterior;
			saldoHorasDescricaoLabel.setText(saldoHorasDescricaoLabel.getText().replace("*", ""));
		}

		saldoHorasLabel.setValor(saldoHorasMesAtual);
	}

	private Double getSaldoHorasMesAnterior() {
		Double saldoHorasMesAnteriorFromFile = MainApp.configuracoesAplicacao.getSaldoHorasMesAnterior();
		if (saldoHorasMesAnteriorFromFile != null) {
			return saldoHorasMesAnteriorFromFile;
		}
		return controleHorasService.obterSaldoHorasMesAnterior();
	}



	public LocalDate getUltimaDataRegistro() {
		return ultimaDataRegistro;
	}

	public void setUltimaDataRegistro(LocalDate ultimaDataRegistro) {
		this.ultimaDataRegistro = ultimaDataRegistro;
	}

}

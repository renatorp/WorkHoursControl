package workhourscontrol.client.component;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import workhourscontrol.client.model.RegistroHoraObservable;
import workhourscontrol.client.properties.PropertyAdapter;
import workhourscontrol.client.service.ControleHorasService;
import workhourscontrol.client.util.ClipboardUtils;
import workhourscontrol.client.util.FXMLLoaderFactory;
import workhourscontrol.util.DateUtils;

public class TabelaTotalizador extends HBox{

	private Logger logger = Logger.getLogger(TabelaTotalizador.class);

	@FXML private TableView<LocalDate> tabelaTotalizador;
	@FXML private TableColumn<LocalDate, LocalDate> colunaDataTotal;
	@FXML private TableColumn<LocalDate, String> colunaTotal;

	private ObservableList<RegistroHoraObservable> listaRegistroHoras;

	private ObservableMap<LocalDate, String> mapTotais;

	private ControleHorasService controleHorasService;

	public TabelaTotalizador() throws IOException {
		FXMLLoader loader = FXMLLoaderFactory.createLoader("view/TabelaTotalizador.fxml", TabelaTotalizador.class);
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
	}

	@FXML
	public void initialize() {

		mapTotais = FXCollections.observableHashMap();

		// Permitindo selecionar múltiplas linhas
		tabelaTotalizador.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		controleHorasService = ControleHorasService.getInstance();

		colunaDataTotal.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<LocalDate,LocalDate>, ObservableValue<LocalDate>>() {
			@Override
			public ObservableValue<LocalDate> call(CellDataFeatures<LocalDate, LocalDate> param) {
				return PropertyAdapter.getProperty(param.getValue(), "data");
			}
		});

		colunaDataTotal.setCellFactory(new Callback<TableColumn<LocalDate,LocalDate>, TableCell<LocalDate,LocalDate>>() {

			@Override
			public TableCell<LocalDate, LocalDate> call(TableColumn<LocalDate, LocalDate> param) {
				return new TableCell<LocalDate, LocalDate>() {
					protected void updateItem(LocalDate item, boolean empty) {
						if (!empty) {
							setText(DateUtils.formatarData(item));
						} else {
							setText("");
						}
					};
				};
			}
		});

		colunaTotal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LocalDate,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<LocalDate, String> param) {
				return new SimpleStringProperty(mapTotais.get(param.getValue()));
			}
		});


		// Evento ao pressionar Ctrl + c
		tabelaTotalizador.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				if (event.isControlDown() && event.getCode().equals(KeyCode.C)) {

					LocalDate data = tabelaTotalizador.getSelectionModel().getSelectedItem();
					if (data != null) {
						ClipboardUtils.adicionarStringEmClipboard(mapTotais.get(data));
					}

				}

			}
		});
	}


	public void setListaRegistroHoras(ObservableList<RegistroHoraObservable> listaRegistroHoras) {
		this.listaRegistroHoras = listaRegistroHoras;
	}

	public void atualizarTotalizador() {

		// Limpa tabela
		tabelaTotalizador.getItems().clear();

		// Cria map de data por lista de registroHoras
		final Map<LocalDate, List<RegistroHoraObservable>> mapDatas = listaRegistroHoras
				.stream()
				.collect(Collectors.groupingBy(new Function<RegistroHoraObservable, LocalDate>() {

					@Override
					public LocalDate apply(RegistroHoraObservable t) {
						try {
							return DateUtils.parseData(t.getDia(), t.getMes(), t.getAno());
						} catch (ParseException e) {
							logger.error("Ocorreu um erro de parse de data", e);
							throw new RuntimeException(e);
						}
					}
				}));

		// Preenchendo map com total de horas calculado
		for (LocalDate data : mapDatas.keySet()) {
			mapTotais.put(data, controleHorasService.calcularDuracaoTrabalhoFormatado(mapDatas.get(data)));
		}

		// Adiciona datas à tabela
		tabelaTotalizador.getItems().addAll(mapDatas.keySet());
	}

	public ObservableList<RegistroHoraObservable> getListaRegistroHoras() {
		return listaRegistroHoras;
	}

	public Double getTotalSelecionado() {
		return tabelaTotalizador.getSelectionModel().getSelectedItems()
				.stream()
				.collect(Collectors.summingDouble(new ToDoubleFunction<LocalDate>() {
					@Override
					public double applyAsDouble(LocalDate value) {
						return Double.valueOf(mapTotais.get(value).replace(",", "."));
					}
				}));
	}

	public void setOnSelecionarItem(EventHandler<MouseEvent> event) {
		tabelaTotalizador.setOnMouseClicked(event);
	}

	public List<Double> getTotais() {
		return mapTotais.values()
				.stream()
				.map(s -> Double.valueOf(s.replace(",", "."))).collect(Collectors.toList());
	}

	public List<Double> getTotaisMenosHoje() {
		List<Double> totais = new ArrayList<>();

		for (LocalDate data : mapTotais.keySet()) {
			if (!data.equals(LocalDate.now())) {
				totais.add(Double.valueOf(mapTotais.get(data).replace(",", ".")));
			}
		}
		return totais;
	}
}

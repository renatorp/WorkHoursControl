package workhourscontrol.client.component;

import java.io.IOException;
import java.text.ParseException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import workhourscontrol.client.model.RegistroHoraObservable;
import workhourscontrol.client.service.ControleHorasService;
import workhourscontrol.client.util.DateUtils;
import workhourscontrol.client.util.FXMLLoaderFactory;

public class TabelaTotalizadorSemanal extends HBox{

	private Logger logger = Logger.getLogger(TabelaTotalizadorSemanal.class);

	@FXML private TableView<Integer> tabelaSemanas;
	@FXML private TableColumn<Integer, String> colunaDataTotal;
	@FXML private TableColumn<Integer, String> colunaTotal;

	private ObservableList<RegistroHoraObservable> listaRegistroHoras;

	private ObservableMap<Integer, String> mapTotais;

	private ControleHorasService controleHorasService;

	public TabelaTotalizadorSemanal() throws IOException {
		FXMLLoader loader = FXMLLoaderFactory.createLoader("view/TabelaTotalizadorSemanal.fxml", TabelaTotalizadorSemanal.class);
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
	}

	@FXML
	public void initialize() {

		mapTotais = FXCollections.observableHashMap();

		// Permitindo selecionar múltiplas linhas
		tabelaSemanas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		controleHorasService = ControleHorasService.getInstance();

		colunaDataTotal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));

		colunaTotal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Integer,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Integer, String> param) {
				return new SimpleStringProperty(mapTotais.get(param.getValue()));
			}
		});

	}


	public void setListaRegistroHoras(ObservableList<RegistroHoraObservable> listaRegistroHoras) {
		this.listaRegistroHoras = listaRegistroHoras;
	}

	public void atualizarTotalizador() {

		// Limpa tabela
		tabelaSemanas.getItems().clear();

		// Cria map de data por lista de registroHoras
		final Map<Integer, List<RegistroHoraObservable>> mapDatas = listaRegistroHoras
				.stream()
				.collect(Collectors.groupingBy(new Function<RegistroHoraObservable, Integer>() {

					@Override
					public Integer apply(RegistroHoraObservable t) {
						try {
							return DateUtils.getIdentificadorSemana(DateUtils.parseData(t.getDia(), t.getMes(), t.getAno()));
						} catch (ParseException e) {
							logger.error("Ocorreu um erro de parse de data", e);
							throw new RuntimeException(e);
						}
					}
				}));

		// Preenchendo map com total de horas calculado
		for (Integer semana : mapDatas.keySet()) {
			mapTotais.put(semana, controleHorasService.calcularDuracaoTrabalhoFormatado(mapDatas.get(semana)));
		}

		// Adiciona datas à tabela
		tabelaSemanas.getItems().addAll(mapDatas.keySet());
	}

	public ObservableList<RegistroHoraObservable> getListaRegistroHoras() {
		return listaRegistroHoras;
	}

	public Double getTotalSelecionado() {
		return tabelaSemanas.getSelectionModel().getSelectedItems()
				.stream()
				.collect(Collectors.summingDouble(new ToDoubleFunction<Integer>() {
					@Override
					public double applyAsDouble(Integer value) {
						return Double.valueOf(mapTotais.get(value).replace(",", "."));
					}
				}));
	}

	public void setOnSelecionarItem(EventHandler<MouseEvent> event) {
		tabelaSemanas.setOnMouseClicked(event);
	}

}

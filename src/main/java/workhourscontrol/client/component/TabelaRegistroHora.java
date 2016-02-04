package workhourscontrol.client.component;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import workhourscontrol.client.component.view.SimNaoCellFactory;
import workhourscontrol.client.model.RegistroHoraObservable;
import workhourscontrol.client.util.FXMLLoaderFactory;

public class TabelaRegistroHora extends HBox{

	@FXML private TableView<RegistroHoraObservable> tabelaRegistroHora;
	@FXML private TableColumn<RegistroHoraObservable, String> colunaData;
	@FXML private TableColumn<RegistroHoraObservable, String> colunaHoraInicio;
	@FXML private TableColumn<RegistroHoraObservable, String> colunaHoraFim;
	@FXML private TableColumn<RegistroHoraObservable, String> colunaObs;
	@FXML private TableColumn<RegistroHoraObservable, Boolean> colunaSync;

	private FilteredList<RegistroHoraObservable> listaFiltrada;

	public TabelaRegistroHora() throws IOException {
		FXMLLoader loader = FXMLLoaderFactory.createLoader("view/TabelaRegistroHora.fxml", TabelaRegistroHora.class);
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
	}

	@FXML
	public void initialize() {

		tabelaRegistroHora.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		colunaData.setCellValueFactory(cellData -> cellData.getValue().getMesProperty());
		colunaHoraInicio.setCellValueFactory(cellData -> cellData.getValue().getHoraInicioProperty());
		colunaHoraFim.setCellValueFactory(cellData -> cellData.getValue().getHoraFimProperty());
		colunaObs.setCellValueFactory(cellData -> cellData.getValue().getObservacaoProperty());
		colunaSync.setCellValueFactory(cellData -> cellData.getValue().getLancadoProperty());

		colunaData.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RegistroHoraObservable,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<RegistroHoraObservable, String> param) {
				RegistroHoraObservable value = param.getValue();
				return value.getDiaProperty().concat("/").concat(value.getMesProperty()).concat("/").concat(value.getAnoProperty());
			}
		});

		colunaSync.setCellFactory(new SimNaoCellFactory<RegistroHoraObservable>());


	}


	public FilteredList<RegistroHoraObservable> getItems() {
		return this.listaFiltrada;
	}

	public void setItems(ObservableList<RegistroHoraObservable> lista) {
		this.listaFiltrada = new FilteredList<>(lista, p -> true);

		// Wrap the FilteredList in a SortedList.
        SortedList<RegistroHoraObservable> sortedData = new SortedList<>(listaFiltrada);

        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tabelaRegistroHora.comparatorProperty());

        // Add sorted (and filtered) data to the table.
        tabelaRegistroHora.setItems(sortedData);
	}

	public RegistroHoraObservable getItemSelecionado() {
		return tabelaRegistroHora.getSelectionModel().getSelectedItem();
	}

	public int getSelectedIndex() {
		return tabelaRegistroHora.getSelectionModel().getSelectedIndex();
	}

	public List<RegistroHoraObservable> getSelectedItems() {
		return tabelaRegistroHora.getSelectionModel().getSelectedItems();
	}

	public void adicionarEventHandler(EventType<MouseEvent> mouseClicked, EventHandler<MouseEvent> eventHandler) {
		tabelaRegistroHora.addEventHandler(mouseClicked, eventHandler);
	}

	public ObservableValue<? extends Comparator<? super RegistroHoraObservable>> comparatorProperty() {
		return tabelaRegistroHora.comparatorProperty();
	}

	public Double getTotalSelecionado(Function<RegistroHoraObservable,Double> calculoTotal) {
		return tabelaRegistroHora.getSelectionModel().getSelectedItems()
				.stream()
				.collect(Collectors.summingDouble(new ToDoubleFunction<RegistroHoraObservable>() {
					@Override
					public double applyAsDouble(RegistroHoraObservable value) {
						return calculoTotal.apply(value);
					}
				}));
	}

	public void setOnSelecionarItem(EventHandler<MouseEvent> event) {
		tabelaRegistroHora.setOnMouseClicked(event);
	}
}

package workhourscontrol.client.component;

import java.io.IOException;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import workhourscontrol.client.model.RegistroHoraObservable;
import workhourscontrol.client.util.FXMLLoaderFactory;

public class TabelaRegistroHora extends HBox{

	@FXML private TableView<RegistroHoraObservable> tabelaRegistroHora;
	@FXML private TableColumn<RegistroHoraObservable, String> colunaData;
	@FXML private TableColumn<RegistroHoraObservable, String> colunaHoraInicio;
	@FXML private TableColumn<RegistroHoraObservable, String> colunaHoraFim;
	@FXML private TableColumn<RegistroHoraObservable, Boolean> colunaSync;

	public TabelaRegistroHora() throws IOException {
		FXMLLoader loader = FXMLLoaderFactory.createLoader("view/TabelaRegistroHora.fxml", TabelaRegistroHora.class);
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
	}

	@FXML
	public void initialize() {

		colunaData.setCellValueFactory(cellData -> cellData.getValue().getMesProperty());
		colunaHoraInicio.setCellValueFactory(cellData -> cellData.getValue().getHoraInicioProperty());
		colunaHoraFim.setCellValueFactory(cellData -> cellData.getValue().getHoraFimProperty());
		colunaSync.setCellValueFactory(cellData -> cellData.getValue().getLancadoProperty());

		colunaData.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RegistroHoraObservable,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<RegistroHoraObservable, String> param) {
				RegistroHoraObservable value = param.getValue();
				return value.getDiaProperty().concat("/").concat(value.getMesProperty()).concat("/").concat(value.getAnoProperty());
			}
		});

		colunaSync.setCellFactory(new Callback<TableColumn<RegistroHoraObservable,Boolean>, TableCell<RegistroHoraObservable,Boolean>>() {

			@Override
			public TableCell<RegistroHoraObservable, Boolean> call(TableColumn<RegistroHoraObservable, Boolean> param) {
				return new TableCell<RegistroHoraObservable, Boolean>() {
					protected void updateItem(Boolean item, boolean empty) {
						if (!empty) {
							if (item != null && item) {
								this.setText("Sim");
							} else {
								this.setText("Não");
							}
						} else {
							this.setText("");
						}
					};
				};
			}
		});


	}


	public ObservableList<RegistroHoraObservable> getItems() {
		return tabelaRegistroHora.getItems();
	}

	public void setItems(ObservableList<RegistroHoraObservable> lista) {
		tabelaRegistroHora.setItems(lista);
	}

	public RegistroHoraObservable getItemSelecionado() {
		return tabelaRegistroHora.getSelectionModel().getSelectedItem();
	}

	public int getSelectedIndex() {
		return tabelaRegistroHora.getSelectionModel().getSelectedIndex();
	}

	public void adicionarEventHandler(EventType<MouseEvent> mouseClicked, EventHandler<MouseEvent> eventHandler) {
		tabelaRegistroHora.addEventHandler(mouseClicked, eventHandler);
	}


}

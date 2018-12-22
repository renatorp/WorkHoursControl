package workhourscontrol.client.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import workhourscontrol.client.MainApp;

public class RootLayoutController {

	@FXML
	private BorderPane rootPane;

	private MainApp mainApp;

	@FXML
	public void initialize() {

		 rootPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				//Atalho para salvar
				if (event.isControlDown() && event.getCode() == KeyCode.S) {
					mainApp.salvar();
				}

				//Atalho para carregar
				if (event.isControlDown() && event.getCode() == KeyCode.O) {
					mainApp.carregar();
				}
			}
		});

	}

	@FXML
	public void handleExit() {
		this.mainApp.fecharAplicacao();
	}

	@FXML
	public void handleSaveAs() {
		this.mainApp.salvarComo();
	}

	@FXML
	public void handleLoad() {
		this.mainApp.carregar();
	}

	@FXML
	public void handleNew() {
		this.mainApp.novo();
	}

	@FXML
	public void handleSave() {
		this.mainApp.salvar();
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	public void handleShowVersion() {
		this.mainApp.showAppVersion();
	}

}

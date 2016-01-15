package workhourscontrol.client.controller;

import javafx.fxml.FXML;
import workhourscontrol.client.MainApp;

public class RootLayoutController {

	private MainApp mainApp;

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

}

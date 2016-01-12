package workhourscontrol.client.controller;

import java.io.File;
import java.util.Objects;

import javafx.fxml.FXML;
import workhourscontrol.client.MainApp;
import workhourscontrol.client.util.FileHelper;
import workhourscontrol.client.util.PreferencesHelper;

public class RootLayoutController {

	private MainApp mainApp;

	@FXML
	public void handleExit() {
		System.exit(0);
	}

	@FXML
	public void handleSaveAs() {
		File arquivo = FileHelper.chooseFileForSaving(mainApp.getPrimaryStage(), "*.xml", "XML files (*.xml)");
		if (Objects.nonNull(arquivo)) {
			mainApp.salvarRegistrosNoArquivo(arquivo);
		}
	}

	@FXML
	public void handleLoad() {
		File arquivo = FileHelper.chooseFileForOpening(mainApp.getPrimaryStage(), "*.xml", "XML files (*.xml)");
		if (Objects.nonNull(arquivo)) {
			mainApp.carregarRegistrosDoArquivo(arquivo);
			PreferencesHelper.setPersonFilePath("xmlPath", arquivo);
		}
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}

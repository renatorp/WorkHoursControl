package workhourscontrol.client.controller;

import java.io.File;
import java.util.Objects;

import javafx.fxml.FXML;
import workhourscontrol.client.MainApp;
import workhourscontrol.client.util.FileHelper;

public class RootLayoutController {

	private MainApp mainApp;

	@FXML
	public void handleExit() {
		System.exit(0);
	}

	@FXML
	public void handleSaveAs() {
		File arquivo = FileHelper.chooseFileForSaving(mainApp.getPrimaryStage(), "*.xml", "XML files (*.xml)", mainApp.getDiretorioArquivoAberto());
		if (Objects.nonNull(arquivo)) {
			mainApp.salvarRegistrosNoArquivo(arquivo);
			mainApp.setArquivoAberto(arquivo);
		}
	}

	@FXML
	public void handleLoad() {
		File arquivo = FileHelper.chooseFileForOpening(mainApp.getPrimaryStage(), "*.xml", "XML files (*.xml)", mainApp.getDiretorioArquivoAberto());
		if (Objects.nonNull(arquivo)) {
			this.mainApp.limparRegistros();
			mainApp.carregarRegistrosDoArquivo(arquivo);
			mainApp.setArquivoAberto(arquivo);
		}
	}

	@FXML
	public void handleNew() {
		this.mainApp.limparRegistros();
		this.mainApp.setArquivoAberto(null);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML public void handleSave() {
		File arquivo = mainApp.getArquivoAberto();
		if (Objects.nonNull(arquivo)) {
			mainApp.salvarRegistrosNoArquivo(arquivo);
		} else {
			handleSaveAs();
		}
	}

}

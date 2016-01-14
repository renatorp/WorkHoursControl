package workhourscontrol.client.util;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileHelper {

	public enum ContextoDialogo {
		SALVAR, ABRIR;
	}

	/**
	 * Abre diálogo para salvar arquivo
	 */
	public static File chooseFile(Stage stage, String extension, String extensionDescription, ContextoDialogo contexto, File diretorioInicial) {

		FileChooser fileChooser = new FileChooser();

		// Especifica quais arquivos serão exibidos por suas extensões
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extensionDescription, extension);
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setInitialDirectory(diretorioInicial);

		File file;
		if (contexto.equals(ContextoDialogo.SALVAR)) {
			// Abre diálogo para salvar
			file = fileChooser.showSaveDialog(stage);
		} else {
			file = fileChooser.showOpenDialog(stage);
		}

		return file;
	}

	public static File chooseFileForOpening(Stage stage, String extension, String extensionDescription, File diretorioInicial) {
		return chooseFile(stage, extension, extensionDescription, ContextoDialogo.ABRIR, diretorioInicial);
	}

	public static File chooseFileForOpening(Stage stage, String extension, String extensionDescription) {
		return chooseFile(stage, extension, extensionDescription, ContextoDialogo.ABRIR, null);
	}

	public static File chooseFileForSaving(Stage stage, String extension, String extensionDescription, File diretorioInicial) {
		return chooseFile(stage, extension, extensionDescription, ContextoDialogo.SALVAR, diretorioInicial);
	}

	public static File chooseFileForSaving(Stage stage, String extension, String extensionDescription) {
		return chooseFile(stage, extension, extensionDescription, ContextoDialogo.SALVAR, null);
	}
}

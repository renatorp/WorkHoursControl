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
	public static File chooseFile(Stage stage, String extension, String extensionDescription, ContextoDialogo contexto) {
		
		FileChooser fileChooser = new FileChooser();

		// Especifica quais arquivos serão exibidos por suas extensões 
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extensionDescription, extension);
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file; 
		
		if (contexto.equals(ContextoDialogo.SALVAR)) {
			// Abre diálogo para salvar
			file = fileChooser.showSaveDialog(stage);
		} else {
			file = fileChooser.showOpenDialog(stage);
		}
		
		return file;
	}
	
	public static File chooseFileForOpening(Stage stage, String extension, String extensionDescription) {
		return chooseFile(stage, extension, extensionDescription, ContextoDialogo.ABRIR);
	}
	
	public static File chooseFileForSaving(Stage stage, String extension, String extensionDescription) {
		return chooseFile(stage, extension, extensionDescription, ContextoDialogo.SALVAR);
	}
}

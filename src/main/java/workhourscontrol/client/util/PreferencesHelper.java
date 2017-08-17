package workhourscontrol.client.util;

import java.io.File;
import java.util.prefs.Preferences;

import workhourscontrol.client.MainApp;

public class PreferencesHelper {

	 /**
     * Retorna o arquivo de preferências da pessoa, o último arquivo que foi aberto.
     * As preferências são lidas do registro específico do SO (Sistema Operacional).
     * Se tais prefêrencias não puderem  ser encontradas, ele retorna null.
     */
	public static File getEnderecoArquivo(String chave) {
		String filePath = getPref(chave);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
	}

	 /**
     * Define o caminho do arquivo carregado atual. O caminho é persistido no
     * registro específico do SO (Sistema Operacional).
     *
     * @param file O arquivo ou null para remover o caminho
     */
    public static void setPersonFilePath(String chave, File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put(chave, file.getPath());
        } else {
            prefs.remove(chave);
        }
    }

    public static String getPref(String chave) {
    	Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
    	return prefs.get(chave, null);
    }

    public static void setPref(String chave, String valor) {
    	Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
    	prefs.put(chave, valor);
    }
}

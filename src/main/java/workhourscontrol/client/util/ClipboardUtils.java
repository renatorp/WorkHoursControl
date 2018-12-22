package workhourscontrol.client.util;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipboardUtils {

	/**
	 * Adiciona string em área de transferência
	 */
	public static void adicionarStringEmClipboard(String string) {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
		content.putString(string);
		clipboard.setContent(content);
	}
}

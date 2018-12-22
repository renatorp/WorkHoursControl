package workhourscontrol.client.util;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public class DialogoHelper {

	/**
	 * Exibe diálogo onde com opçoes Sim, Não e Cancelar para usuário confirmar se deseja fechar aplicação
	 * Recebe como parâmetro a ação a ser realizada.
	 */
	public static boolean confirmarFechamentoAplicacao(Runnable action) {
		Action response = Dialogs.create()
				.title("Atenção")
				.masthead("Existem alterações não salvas.")
				.message("Deseja salvar as alterações?")
				.showConfirm();

		if (response == Dialog.Actions.YES) {
			action.run();
			return true;
		} else if (response == Dialog.Actions.NO) {
			return true;
		} else {
			return false;
		}

	}
}

package workhourscontrol.client.util;

import java.util.Optional;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class DialogoHelper {

	/**
	 * Exibe di�logo onde com op�oes Sim, N�o e Cancelar para usu�rio confirmar se deseja fechar aplica��o
	 * Recebe como par�metro a a��o a ser realizada.
	 */
	public static boolean confirmarFechamentoAplicacao(Runnable action) {
		Alert alert  = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Atenção");
		alert.setHeaderText("Existem alterações não salvas.");
		alert.setContentText("Deseja salvar as alterações");
		
		ButtonType buttonTypeYes = new ButtonType("Sim");
		ButtonType buttonTypeNo = new ButtonType("Não");
		ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);
		
		Optional<ButtonType> opt = alert.showAndWait();
		
		if (opt.get() == buttonTypeYes) {
			action.run();
			return true;
		} else if (opt.get() == buttonTypeNo) {
			return true;
		}

		return false;
	}
}

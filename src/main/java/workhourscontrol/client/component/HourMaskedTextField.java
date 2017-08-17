package workhourscontrol.client.component;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import workhourscontrol.client.util.FXMLLoaderFactory;

public class HourMaskedTextField extends HBox{

	@FXML
	private TextField campoHora;

	public HourMaskedTextField() throws IOException {
		FXMLLoader loader = FXMLLoaderFactory.createLoader("view/HourMaskedTextField.fxml", HourMaskedTextField.class);
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
	}


	@FXML
	public void initialize() {
	}

	@FXML
	public void mask(KeyEvent event) {

		KeyCode codigoDigitado = event.getCode();

		if (codigoDigitado.equals(KeyCode.BACK_SPACE)
				|| codigoDigitado.equals(KeyCode.RIGHT)
				|| codigoDigitado.equals(KeyCode.LEFT)
				|| codigoDigitado.equals(KeyCode.DELETE)
				|| codigoDigitado.equals(KeyCode.ALT)
				|| codigoDigitado.equals(KeyCode.TAB)
				){
			return ;
		}

		String valorAtual = campoHora.getText();

		if (valorAtual.length() >= 5) {
			campoHora.setText(valorAtual.substring(0, 5));
			campoHora.positionCaret(5);
			event.consume();
			return;
		}


		if (valorAtual.length() == 2) {
			campoHora.setText(valorAtual + ":");
			campoHora.positionCaret(3);
			event.consume();
		}

	}


	public void setText(String horaInicio) {
		this.campoHora.setText(horaInicio);
	}

	public String getText() {
		return this.campoHora.getText();
	}
}
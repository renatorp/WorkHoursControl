package workhourscontrol.client.component;

import java.util.Objects;
import java.util.function.Predicate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import workhourscontrol.util.StringUtils;

/**
 * Exibe label com diferentes cores dependendo de dadas regras
 */
public class LabelAviso extends HBox {

	private Label label;
	private DoubleProperty valor = new SimpleDoubleProperty();
	private Predicate<Double> testeDanger;
	private Predicate<Double> testeWarning;
	private Predicate<Double> testeInfo;

	private ChangeListener<Number> changeListener = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			label.setText(StringUtils.formatarRetornoDuracaoComoHoras(newValue.doubleValue()));
			label.setStyle("-fx-text-fill: " + calcularCorLabel(newValue.doubleValue()) + " ;");
		}
	};

	public LabelAviso() {
		label = new Label();
		getChildren().add(label);

		valor.addListener(changeListener);
	}

	public void setValor(double valor) {
		this.valor.set(valor);
	}

	private String calcularCorLabel(double valor) {
		if (Objects.nonNull(testeDanger) && testeDanger.test(valor)) {
			return "red";
		}
		if (Objects.nonNull(testeWarning) && testeWarning.test(valor)) {
			return "darkOrange";
		}
		if (Objects.nonNull(testeInfo) && testeInfo.test(valor)) {
			return "blue";
		}
		return "black";
	}

	public void setTesteDanger(Predicate<Double> testeDanger) {
		this.testeDanger = testeDanger;
	}

	public void setTesteWarning(Predicate<Double> testeWarning) {
		this.testeWarning = testeWarning;
	}

	public void setTesteInfo(Predicate<Double> testeInfo) {
		this.testeInfo = testeInfo;
	}

}

package workhourscontrol.client.component.view;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Cell Factory para exibir "Sim" ou "Não" em células com valor booleano
 */
public class SimNaoCellFactory<E> implements Callback<TableColumn<E,Boolean>, TableCell<E,Boolean>> {

	@Override
	public TableCell<E, Boolean> call(TableColumn<E, Boolean> param) {
		return new TableCell<E, Boolean>() {
			protected void updateItem(Boolean item, boolean empty) {
				if (!empty) {
					if (item != null && item) {
						this.setText("Sim");
					} else {
						this.setText("Não");
					}
				} else {
					this.setText("");
				}
			};
		};
	}


}
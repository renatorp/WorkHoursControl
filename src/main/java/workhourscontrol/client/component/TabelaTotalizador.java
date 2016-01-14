package workhourscontrol.client.component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import workhourscontrol.client.model.RegistroHoraObservable;
import workhourscontrol.client.util.DateUtils;
import workhourscontrol.client.util.FXMLLoaderFactory;

public class TabelaTotalizador extends HBox{

	private Logger logger = Logger.getLogger(TabelaTotalizador.class);

	@FXML private TableView<LocalDate> tabelaTotalizador;
	@FXML private TableColumn<LocalDate, String> colunaDataTotal;
	@FXML private TableColumn<LocalDate, String> colunaTotal;

	private ObservableList<RegistroHoraObservable> listaRegistroHoras;

	public TabelaTotalizador() throws IOException {
		FXMLLoader loader = FXMLLoaderFactory.createLoader("view/TabelaTotalizador.fxml", TabelaTotalizador.class);
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
	}

	@FXML
	public void initialize() {
		colunaDataTotal.setCellValueFactory(cellData -> new SimpleStringProperty(DateUtils.formatarData(cellData.getValue())));

		colunaTotal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LocalDate,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<LocalDate, String> param) {
				String numRegPorData = calcularDuracaoTrabalhoPorData(param.getValue());
				return new SimpleStringProperty(String.valueOf(numRegPorData));
			}
		});
	}

	private String calcularDuracaoTrabalhoPorData(LocalDate date) {
		double numRegPorData = 0d;

		for (RegistroHoraObservable r : listaRegistroHoras) {
			if ((r.getDia() + "/"  + r.getMes() + "/" + r.getAno()).equals(DateUtils.formatarData(date))) {
				LocalTime horaInicio = DateUtils.parseHora(r.getHoraInicio());
				LocalTime horaFim = DateUtils.parseHora(r.getHoraFim());
				numRegPorData += DateUtils.getDuracaoEmMinutos(horaInicio, horaFim);
			}
		}
		return formatarRetornoDuracao(numRegPorData);
	}

	public void setListaRegistroHoras(ObservableList<RegistroHoraObservable> listaRegistroHoras) {
		this.listaRegistroHoras = listaRegistroHoras;
	}

	public void atualizarTotalizador() {
		tabelaTotalizador.getItems().clear();
		tabelaTotalizador.getItems().addAll(
				listaRegistroHoras
						.stream()
						.collect(Collectors.groupingBy(new Function<RegistroHoraObservable, LocalDate>() {

							@Override
							public LocalDate apply(RegistroHoraObservable t) {
								try {
									return DateUtils.parseData(t.getDia(), t.getMes(), t.getAno());
								} catch (ParseException e) {
									logger.error("Ocorreu um erro de parse de data", e);
									throw new RuntimeException(e);
								}
							}
						})).keySet());
	}


	private String formatarRetornoDuracao(double numRegPorData) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(numRegPorData);
	}

}

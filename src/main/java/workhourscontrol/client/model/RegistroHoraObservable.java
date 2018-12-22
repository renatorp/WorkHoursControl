package workhourscontrol.client.model;

import java.text.ParseException;
import java.time.LocalDate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import workhourscontrol.entity.RegistroHora;
import workhourscontrol.util.DateUtils;

public class RegistroHoraObservable extends RegistroHora{

	private StringProperty horaInicioProperty = new SimpleStringProperty();
	private StringProperty horaFimProperty = new SimpleStringProperty();
	private StringProperty observacaoProperty = new SimpleStringProperty();
	private StringProperty issueProperty = new SimpleStringProperty();
	private StringProperty diaProperty = new SimpleStringProperty();
	private StringProperty mesProperty = new SimpleStringProperty();
	private StringProperty anoProperty = new SimpleStringProperty();
	private BooleanProperty lancadoProperty = new SimpleBooleanProperty();

	public StringProperty getHoraInicioProperty() {
		return horaInicioProperty;
	}
	@Override
	public String getHoraInicio() {
		return horaInicioProperty.get();
	}
	@Override
	public void setHoraInicio(String horaInicio) {
		this.horaInicioProperty.set(horaInicio);
	}

	public StringProperty getHoraFimProperty() {
		return horaFimProperty;
	}
	@Override
	public String getHoraFim() {
		return horaFimProperty.get();
	}
	@Override
	public void setHoraFim(String horaFim) {
		this.horaFimProperty.set(horaFim);
	}

	public StringProperty getObservacaoProperty() {
		return observacaoProperty;
	}
	@Override
	public String getObservacao() {
		return observacaoProperty.get();
	}
	@Override
	public void setObservacao(String observacao) {
		this.observacaoProperty.set(observacao);
	}

	public StringProperty getIssueProperty() {
		return issueProperty;
	}
	@Override
	public String getIssue() {
		return issueProperty.get();
	}
	@Override
	public void setIssue(String issue) {
		this.issueProperty.set(issue);
	}

	public StringProperty getDiaProperty() {
		return diaProperty;
	}
	@Override
	public String getDia() {
		return diaProperty.get();
	}
	@Override
	public void setDia(String dia) {
		this.diaProperty.set(dia);
	}

	public StringProperty getMesProperty() {
		return mesProperty;
	}
	@Override
	public String getMes() {
		return mesProperty.get();
	}
	@Override
	public void setMes(String mes) {
		this.mesProperty.set(mes);
	}

	public StringProperty getAnoProperty() {
		return anoProperty;
	}
	@Override
	public String getAno() {
		return anoProperty.get();
	}
	@Override
	public void setAno(String ano) {
		this.anoProperty.set(ano);
	}

	public BooleanProperty getLancadoProperty() {
		return lancadoProperty;
	}
	@Override
	public Boolean isLancado() {
		return lancadoProperty.get();
	}
	@Override
	public void setLancado(Boolean lancado) {
		this.lancadoProperty.set(lancado);
	}

	public LocalDate getData() throws ParseException {
		return DateUtils.parseData(getDia(), getMes(), getAno());
	}

}

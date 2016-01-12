package workhourscontrol.entity;

public class RegistroHora {
	private String horaInicio;
	private String horaFim;
	private String observacao;
	private String issue;
	private String dia;
	private String mes;
	private String ano;
	private Boolean lancado;

	public String getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFim() {
		return horaFim;
	}
	public void setHoraFim(String horaFim) {
		this.horaFim = horaFim;
	}

	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getDia() {
		return dia;
	}
	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAno() {
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}

	public Boolean isLancado() {
		return lancado;
	}
	public void setLancado(Boolean lancado) {
		this.lancado = lancado;
	}

}

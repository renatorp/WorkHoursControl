package workhourscontrol.client;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configuracao")
public class ConfiguracoesAplicacao {

	private String issueDefault;
	private String proxyHost;
	private String proxyPort;
	private String proxyUser;
	private String proxyPassword;
	private String loginAplicacao;
	private String passwordAplicacao;
	private String urlPlanilha;
	private String controleHorasClass;
	private Double saldoHorasMesAnterior;
	private boolean contabilizarHorasFormatado = false;

	@XmlElement(name = "issueDefault")
	public String getIssueDefault() {
		return issueDefault;
	}
	public void setIssueDefault(String issueDefault) {
		this.issueDefault = issueDefault;
	}

	@XmlElement(name = "proxyHost")
	public String getProxyHost() {
		return proxyHost;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	@XmlElement(name = "proxyPort")
	public String getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	@XmlElement(name = "proxyUser")
	public String getProxyUser() {
		return proxyUser;
	}
	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	@XmlElement(name = "proxyPassword")
	public String getProxyPassword() {
		return proxyPassword;
	}
	public void setProxyPassword(String proxyPassoword) {
		this.proxyPassword = proxyPassoword;
	}

	@XmlElement(name = "loginAplicacao")
	public String getLoginAplicacao() {
		return loginAplicacao;
	}
	public void setLoginAplicacao(String loginAplicacao) {
		this.loginAplicacao = loginAplicacao;
	}

	@XmlElement(name = "passwordAplicacao")
	public String getPasswordAplicacao() {
		return passwordAplicacao;
	}
	public void setPasswordAplicacao(String passwordAplicacao) {
		this.passwordAplicacao = passwordAplicacao;
	}

	@XmlElement(name = "urlPlanilha")
	public String getUrlPlanilha() {
		return urlPlanilha;
	}
	public void setUrlPlanilha(String urlPlanilha) {
		this.urlPlanilha = urlPlanilha;
	}

	@XmlElement(name = "controleHorasClass")
	public String getControleHorasClass() {
		return controleHorasClass;
	}
	public void setControleHorasClass(String controleHorasClass) {
		this.controleHorasClass = controleHorasClass;
	}

	@XmlElement(name = "saldoHorasMesAnterior")
	public Double getSaldoHorasMesAnterior() {
		return saldoHorasMesAnterior;
	}
	public void setSaldoHorasMesAnterior(Double saldoHorasMesAnterior) {
		this.saldoHorasMesAnterior = saldoHorasMesAnterior;
	}
	
	@XmlElement(name = "contabilizarHorasFormatado")
	public boolean isContabilizarHorasFormatado() {
		return contabilizarHorasFormatado;
	}
	public void setContabilizarHorasFormatado(boolean contabilizarHorasFormatado) {
		this.contabilizarHorasFormatado = contabilizarHorasFormatado;
	}

}

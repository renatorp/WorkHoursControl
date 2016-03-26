package workhourscontrol.service;

import java.util.List;

import workhourscontrol.strategy.AjusteHorasStrategy;

public class ParametrosControleHorasHttp {

	private boolean usarProxy;
	private String proxyHost;
	private int proxyPort;
	private String proxyUser;
	private String proxyPassword;
	private String user;
	private String password;
	private List<AjusteHorasStrategy> ajusteHoraStrategies;

	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isUsarProxy() {
		return usarProxy;
	}
	public void setUsarProxy(boolean usarProxy) {
		this.usarProxy = usarProxy;
	}
	public String getProxyHost() {
		return proxyHost;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	public int getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}
	public String getProxyPassword() {
		return proxyPassword;
	}
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}
	public String getProxyUser() {
		return proxyUser;
	}
	public List<AjusteHorasStrategy> getAjusteHoratrategies() {
		return ajusteHoraStrategies;
	}
	public void setAjusteHoratrategies(List<AjusteHorasStrategy> ajusteHoraStrategies) {
		this.ajusteHoraStrategies = ajusteHoraStrategies;
	}

}

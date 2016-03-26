package workhourscontrol.service;

import java.util.ArrayList;

import workhourscontrol.strategy.AjusteHorasStrategy;

public class ControleHorasHttpBuilder implements ControleHorasBuilder{

	ControleHorasHttp controleHoras;
	ParametrosControleHorasHttp parametros;

	public ControleHorasHttpBuilder(ControleHorasHttp controleHoras) {
		parametros = new ParametrosControleHorasHttp();
		parametros.setAjusteHoratrategies(new ArrayList<>());
		this.controleHoras = controleHoras;
	}

	public ControleHorasHttpBuilder setProxy(String host, int port) {
		parametros.setUsarProxy(true);
		parametros.setProxyHost(host);
		parametros.setProxyPort(port);
		return this;
	}

	public ControleHorasHttpBuilder setCredenciaisProxy(String user, String pass) {
		parametros.setProxyUser(user);
		parametros.setProxyPassword(pass);
		return this;
	}

	public ControleHorasHttpBuilder setCredenciaisAcessoRemoto(String user, String password) {
		parametros.setUser(user);
		parametros.setPassword(password);
		return this;
	}
	
	public ControleHorasHttpBuilder addAjusteHorasStrategy(AjusteHorasStrategy ajusteHorasStrategy) {
		this.parametros.getAjusteHoratrategies().add(ajusteHorasStrategy);
		return this;
	}

	public ControleHoras build() {
		this.controleHoras.setParametros(parametros);
		return this.controleHoras;
	}
}

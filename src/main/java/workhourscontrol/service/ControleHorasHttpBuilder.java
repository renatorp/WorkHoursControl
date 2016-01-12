package workhourscontrol.service;


public class ControleHorasHttpBuilder implements ControleHorasBuilder{

	ControleHorasHttp controleHoras;
	ParametrosControleHorasHttp parametros;

	public ControleHorasHttpBuilder(ControleHorasHttp controleHoras) {
		parametros = new ParametrosControleHorasHttp();
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

	public ControleHoras build() {
		this.controleHoras.setParametros(parametros);
		return this.controleHoras;
	}
}

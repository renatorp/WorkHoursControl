package workhourscontrol.service;

import java.io.File;

public class ControleHorasPlanilhaBuilder implements ControleHorasBuilder{

	ControleHorasPlanilha controleHoras;
	ParametrosControleHorasPlanilha parametros;

	public ControleHorasPlanilhaBuilder(ControleHorasPlanilha controleHoras) {
		parametros = new ParametrosControleHorasPlanilha();
		this.controleHoras = controleHoras;
	}

	public ControleHorasPlanilhaBuilder setPlanilha(File arquivo) {
		parametros.setFile(arquivo);
		return this;
	}

	public ControleHoras build() {
		this.controleHoras.setParametros(parametros);
		return this.controleHoras;
	}
}

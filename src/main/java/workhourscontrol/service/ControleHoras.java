package workhourscontrol.service;

import java.util.List;

import workhourscontrol.entity.RegistroHora;

public interface ControleHoras {

	void registrarHoras(List<RegistroHora> registros);

	void fecharConexao();

	double obterSaldoHoras();

}

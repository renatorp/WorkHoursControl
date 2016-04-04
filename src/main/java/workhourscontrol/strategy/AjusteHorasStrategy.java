package workhourscontrol.strategy;

import java.util.List;

import workhourscontrol.entity.RegistroHora;

public interface AjusteHorasStrategy {

	List<RegistroHora> ajustarRegistros(List<RegistroHora> registros);
	
}

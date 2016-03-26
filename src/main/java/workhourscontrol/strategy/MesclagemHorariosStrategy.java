package workhourscontrol.strategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import workhourscontrol.entity.RegistroHora;
import workhourscontrol.entity.RegistroHoraMescladoGroupWrapper;
import workhourscontrol.util.RegistroHoraUtil;

public class MesclagemHorariosStrategy implements AjusteHorasStrategy {

	@Override
	public List<RegistroHora> ajustarRegistros(List<RegistroHora> registros) {
		
		List<RegistroHora> registrosAgrupados = new ArrayList<>();
		
		RegistroHoraMescladoGroupWrapper wrapper = new RegistroHoraMescladoGroupWrapper();
		
		// Agrupa registros por data
		Map<LocalDate, List<RegistroHora>> registrosPorData = RegistroHoraUtil.agruparRegistrosPorData(registros);
		
		//Faz o agrupamento
		for (List<RegistroHora> listRegistroHora : registrosPorData.values()) {
			for (int i = 0; i < listRegistroHora.size(); i++) {
				
				RegistroHora registroHora = listRegistroHora.get(i);
				
				// Primeiro registro
				if (wrapper.getListSize() == 0) {
					wrapper.addRegistroHora(registroHora);
					
				} else {
					
					if (wrapper.getHoraFim().equals(registroHora.getHoraInicio())) {
						wrapper.addRegistroHora(registroHora);
						
					} else {
						registrosAgrupados.add(wrapper);
						wrapper = new RegistroHoraMescladoGroupWrapper();
						wrapper.addRegistroHora(registroHora);
					}
			
				}
				
				if (i == listRegistroHora.size() - 1) {
					registrosAgrupados.add(wrapper);
				}
				
			}
			
			wrapper = new RegistroHoraMescladoGroupWrapper();
		}
		return registrosAgrupados;
	}

}

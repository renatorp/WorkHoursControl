package workhourscontrol.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegistroHoraGroupWrapper extends RegistroHora{
	protected List<RegistroHora> listaRegistroHora;

	public RegistroHoraGroupWrapper() {
		listaRegistroHora = new ArrayList<>();
	}
	
	public Boolean isLancado() {
		for (RegistroHora registroHora : listaRegistroHora) {
			if (!registroHora.isLancado()) {
				return false;
			}
		}
		return true;
	}
	
	public void setLancado(Boolean lancado) {
		for (RegistroHora registroHora : listaRegistroHora) {
			registroHora.setLancado(lancado);
		}
	}

	public void addRegistroHora(RegistroHora registroHora) {
		this.listaRegistroHora.add(registroHora);
	}
	
	public void removeRegistroHora(RegistroHora registroHora) {
		this.listaRegistroHora.remove(registroHora);
	}
	
	public void addAllRegistroHora(RegistroHora... registrosHora) {
		this.listaRegistroHora.addAll(Arrays.asList(registrosHora));
	}
	
	public void clearRegistroHora() {
		this.listaRegistroHora.clear();
	}
	
	public int getListSize() {
		return this.listaRegistroHora.size();
	}
}

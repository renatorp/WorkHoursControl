package workhourscontrol.entity;

import java.util.Objects;
import java.util.stream.Collectors;

import workhourscontrol.comparator.HoraFimRegistroComparator;
import workhourscontrol.comparator.HoraInicioRegistroComparator;

public class RegistroHoraMescladoGroupWrapper extends RegistroHoraGroupWrapper{
	
	public String getHoraInicio() {
		RegistroHora registroMenorHora = this.listaRegistroHora.stream().collect(Collectors.minBy(HoraInicioRegistroComparator.instance())).get();
		return Objects.nonNull(registroMenorHora) ? registroMenorHora.getHoraInicio() : null;
	}
	public void setHoraInicio(String horaInicio) {
		RegistroHora r = getPrimeiroLista();
		if (Objects.nonNull(r) ) {
			r.setHoraInicio(horaInicio);
		}
	}

	public String getHoraFim() {
		RegistroHora registroMaiorHora = this.listaRegistroHora.stream().collect(Collectors.maxBy(HoraFimRegistroComparator.instance())).get();
		return Objects.nonNull(registroMaiorHora) ? registroMaiorHora.getHoraFim() : null;
	}
	public void setHoraFim(String horaFim) {
		RegistroHora r = getUltimoLista();
		if (Objects.nonNull(r) ) {
			r.setHoraFim(horaFim);
		}
	}

	public String getObservacao() {
		RegistroHora r = getPrimeiroLista();
		return Objects.nonNull(r) ? r.getObservacao() : null;
	}
	public void setObservacao(String observacao) {
		//No op
	}

	public String getIssue() {
		RegistroHora r = getPrimeiroLista();
		return Objects.nonNull(r) ? r.getIssue() : null;
	}
	public void setIssue(String issue) {
		//No op
	}

	public String getDia() {
		RegistroHora r = getPrimeiroLista();
		return Objects.nonNull(r) ? r.getDia() : null;
	}
	public void setDia(String dia) {
		//No op
	}

	public String getMes() {
		RegistroHora r = getPrimeiroLista();
		return Objects.nonNull(r) ? r.getMes() : null;
	}
	public void setMes(String mes) {
		//No op
	}

	public String getAno() {
		RegistroHora r = getPrimeiroLista();
		return Objects.nonNull(r) ? r.getAno() : null;
	}
	public void setAno(String ano) {
		//No op
	}
	
	private RegistroHora getPrimeiroLista() {
		if (!this.listaRegistroHora.isEmpty()) {
			return listaRegistroHora.iterator().next();
		}
		return null;
	}
	
	private RegistroHora getUltimoLista() {
		if (!this.listaRegistroHora.isEmpty()) {
			return listaRegistroHora.get(listaRegistroHora.size() -1 );
		}
		return null;
	}
}

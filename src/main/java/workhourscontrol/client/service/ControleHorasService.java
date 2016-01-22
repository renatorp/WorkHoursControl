package workhourscontrol.client.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import workhourscontrol.client.model.RegistroHoraObservable;
import workhourscontrol.client.util.DateUtils;
import workhourscontrol.client.util.SaldoHorasHolder;
import workhourscontrol.entity.RegistroHora;

public class ControleHorasService {

	private IntegracaoService integracaoService = IntegracaoService.getInstance();

	private static ControleHorasService controleHorasService;

	/** Singleton */
	public static ControleHorasService getInstance() {
		if (Objects.isNull(controleHorasService)) {
			controleHorasService = new ControleHorasService();
		}
		return controleHorasService;
	}

	public String calcularDuracaoTrabalhoFormatado(List<RegistroHoraObservable> listaRegistroHoras) {
		double numRegPorData = calcularDuracaoTrabalho(listaRegistroHoras);
		return workhourscontrol.client.util.StringUtils.formatarRetornoDuracao(numRegPorData);
	}

	public double calcularDuracaoTrabalho(List<RegistroHoraObservable> listaRegistroHoras) {
		double numRegPorData = 0d;
		for (RegistroHora r : listaRegistroHoras) {
			numRegPorData += calcularDuracaoTrabalho(r);
		}
		return numRegPorData;
	}

	public double calcularDuracaoTrabalho(RegistroHora r) {
		return  calcularDuracaoTrabalho(r.getHoraInicio(), r.getHoraFim());
	}

	public double calcularDuracaoTrabalho(String hInicio, String hFim) {
		LocalTime horaInicio = DateUtils.parseHora(hInicio);
		LocalTime horaFim = DateUtils.parseHora(hFim);
		return  DateUtils.getDuracaoEmMinutos(horaInicio, horaFim);
	}


	/**
	 * Calcula quanto tempo falta para completar as oito horas do dia
	 */
	public double calcularHorasTrabalhoRestantes(List<RegistroHoraObservable> registrosHoje) {
		double horaRestante = 8;
		for (RegistroHoraObservable registroHora : registrosHoje) {
			if (StringUtils.isNotBlank(registroHora.getHoraFim())) {
				horaRestante -= calcularDuracaoTrabalho(registroHora);
			} else {
				horaRestante -= calcularDuracaoTrabalho(registroHora.getHoraInicio(), DateUtils.formatarHoraAgora());
			}
		}
		return horaRestante;
	}

	/**
	 * Calcula saldo de horas dadas a lista dos totais.
	 */
	public double calcularSaldoHoras(final List<Double> listaTotais) {
		Double totalHoras = listaTotais
				.stream()
				.collect(Collectors.summingDouble(d -> d.doubleValue()));

		int tamanho = listaTotais.size();

		return totalHoras - (tamanho * 8) ;
	}

	public Double obterSaldoHorasMesAnterior() {
		Double saldoAnterior = SaldoHorasHolder.getSaldoHoras(() -> integracaoService.obterSaldoHoras() );
		if (Objects.nonNull(saldoAnterior)) {
			return saldoAnterior;
		}
		return null;
	}

}

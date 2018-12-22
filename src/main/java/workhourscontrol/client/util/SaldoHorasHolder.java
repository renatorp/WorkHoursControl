package workhourscontrol.client.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

import workhourscontrol.util.DateUtils;

public class SaldoHorasHolder {

	private static final String CHAVE_SALDO_HORAS = "saldoHoras";
	private static final String CHAVE_DATA_SALDO_HORAS = "dataSaldoHoras";
	private static final String FORMATO_DATA = "ddMMyyyy";

	public static Double saldoHoras;

	// Retorna apenas uma vez
	public static Double getSaldoHoras(Supplier<Double> action) {

		// Verifica primeiramente na aplicação já aberta
		if (Objects.isNull(saldoHoras)) {

			String saldoString = PreferencesHelper.getPref(CHAVE_SALDO_HORAS);
			String dataRegistro = PreferencesHelper.getPref(CHAVE_DATA_SALDO_HORAS);

			// Caso não exista, verifica nas preferências do sistema

			if (StringUtils.isBlank(saldoString) || "null".equals(saldoString)
					|| DateUtils.isNotHoje(LocalDate.parse(dataRegistro, DateTimeFormatter.ofPattern(FORMATO_DATA)))) {

				// Se não encontrar, busca remotamente
				saldoHoras = action.get();

				if (Objects.nonNull(saldoString)) {
					PreferencesHelper.setPref(CHAVE_SALDO_HORAS, String.valueOf(saldoHoras));
					PreferencesHelper.setPref(CHAVE_DATA_SALDO_HORAS, DateUtils.formatarData(LocalDate.now(), FORMATO_DATA));
				}

			} else {
				saldoHoras = Double.valueOf(saldoString);
			}
		}

		return saldoHoras;
	}
}

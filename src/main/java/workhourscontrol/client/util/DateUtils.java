package workhourscontrol.client.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {
	public static final String formatoPadrao = "dd/MM/yyyy";
	public static final String formatoPadraoDataHora = "dd/MM/yyyy hh:mm";
	public static final String formatoPadraoHora = "HH:mm";

	public static String formatarData(String dia, String mes, String ano, String formato) throws ParseException {
		String dateString = dia + mes + ano;
		SimpleDateFormat sdfPadrao = new SimpleDateFormat("ddmmyyyy");
		Date data = sdfPadrao.parse(dateString);
		return new SimpleDateFormat(formato).format(data);
	}

	public static String formatarDataHora(String dia, String mes, String ano, String hora, String minuto, String formato) throws ParseException {
		String dateString = dia + mes + ano + hora + minuto;
		SimpleDateFormat sdfPadrao = new SimpleDateFormat("ddmmyyyyhhmm");
		Date data = sdfPadrao.parse(dateString);
		return new SimpleDateFormat(formato).format(data);
	}

	public static String formatarData(String dia, String mes, String ano) throws ParseException {
		return formatarData(dia, mes, ano, formatoPadrao);
	}

	public static String formatarData(LocalDate date) {
		return formatarData(date, formatoPadrao);
	}

	public static String formatarData(LocalDate date, String formato) {
		return date.format(DateTimeFormatter.ofPattern(formato));
	}

	public static LocalDate parseData(String dia, String mes, String ano) throws ParseException {
		String dataString = formatarData(dia, mes, ano);
		return LocalDate.parse(dataString, DateTimeFormatter.ofPattern(DateUtils.formatoPadrao));
	}

	public static LocalDate parseDataHora(String dia, String mes, String ano, String hora, String minuto) throws ParseException {
		String dataString = formatarDataHora(dia, mes, ano, hora, minuto);
		return LocalDate.parse(dataString, DateTimeFormatter.ofPattern(DateUtils.formatoPadraoDataHora));
	}

	public static String formatarDataHora(String dia, String mes, String ano, String hora, String minuto) throws ParseException {
		return formatarDataHora(dia, mes, ano, hora, minuto, formatoPadraoDataHora);
	}

	public static String formatarHora(LocalDateTime hora) {
		return hora.format(DateTimeFormatter.ofPattern(formatoPadraoHora));
	}

	public static String formatarHoraAgora() {
		return formatarHora(LocalDateTime.now());
	}

	public static String getDiaAsString(LocalDate data) {
		return workhourscontrol.client.util.StringUtils.inserirZeroAEsquerda(data.get(ChronoField.DAY_OF_MONTH));
	}

	public static String getMesAsString(LocalDate data) {
		return workhourscontrol.client.util.StringUtils.inserirZeroAEsquerda(data.get(ChronoField.MONTH_OF_YEAR));
	}

	public static String getAnoAsString(LocalDate data) {
		return String.valueOf(data.get(ChronoField.YEAR_OF_ERA));
	}

	public static LocalTime parseHora(String hora) {
		if (StringUtils.isNotBlank(hora)) {
			String[] split = hora.split(":");
			return LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		}
		return null;
	}

	public static double getDuracaoEmMinutos(LocalTime from, LocalTime to) {
		if (Objects.isNull(from) || Objects.isNull(to)) {
			return 0d;
		}
		return (double)LocalTime.from(from).until(to, java.time.temporal.ChronoUnit.MINUTES) / 60;
	}

	/**
	 * Obtém identificador da semana a partir de data
	 * @param parseData
	 * @return
	 */
	public static Integer getIdentificadorSemana(LocalDate data) {
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		return data.get(weekFields.weekOfMonth());
	}

	public static boolean isHoje(LocalDate data) {
		return data.isEqual(LocalDate.now());
	}

	public static boolean isNotHoje(LocalDate data) {
		return !isHoje(data);
	}
}

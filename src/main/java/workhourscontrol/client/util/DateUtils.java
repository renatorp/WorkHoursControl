package workhourscontrol.client.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {
	public static final String formatoPadrao = "dd/MM/yyyy";

	public static String formatarData(String dia, String mes, String ano, String formato) throws ParseException {
		String dateString = dia + mes + ano;
		SimpleDateFormat sdfPadrao = new SimpleDateFormat("ddmmyyyy");
		Date data = sdfPadrao.parse(dateString);
		return new SimpleDateFormat(formato).format(data);
	}

	public static String formatarData(String dia, String mes, String ano) throws ParseException {
		return formatarData(dia, mes, ano, formatoPadrao);
	}

	public static String formatarData(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern(formatoPadrao));
	}

	public static LocalDate parseData(String dia, String mes, String ano) throws ParseException {
		String dataString = formatarData(dia, mes, ano);
		return LocalDate.parse(dataString, DateTimeFormatter.ofPattern(DateUtils.formatoPadrao));
	}

	public static String getDiaAsString(LocalDate data) {
		return inserirZeroAEsquerda(data.get(ChronoField.DAY_OF_MONTH));
	}

	public static String getMesAsString(LocalDate data) {
		return inserirZeroAEsquerda(data.get(ChronoField.MONTH_OF_YEAR));
	}

	public static String getAnoAsString(LocalDate data) {
		return String.valueOf(data.get(ChronoField.YEAR_OF_ERA));
	}

	private static String inserirZeroAEsquerda(int inteiro) {
		return (inteiro < 10 ? "0" : "") + String.valueOf(inteiro);
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

}

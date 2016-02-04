package workhourscontrol.client.util;

import java.text.DecimalFormat;

public class StringUtils {

	public static String inserirZeroAEsquerda(String inteiro) {
		return (Integer.valueOf(inteiro) < 10 ? "0" : "") + String.valueOf(inteiro);
	}

	public static String inserirZeroAEsquerda(int inteiro) {
		return inserirZeroAEsquerda(String.valueOf(inteiro));
	}

	public static String formatarRetornoDuracao(double numRegPorData) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(numRegPorData);
	}

	public static String formatarRetornoMinutos(double num) {
		DecimalFormat df = new DecimalFormat("##");
		return workhourscontrol.client.util.StringUtils.inserirZeroAEsquerda(df.format(num));
	}

	public static String formatarRetornoDuracaoComoHoras(double numHoras) {
		String prefixo = numHoras < 0 ? "-" : "";
		String s1 = String.valueOf(Math.abs((int) numHoras));
		String s2 = formatarRetornoMinutos(Math.abs(((numHoras%1) * 60) ));
		return prefixo + s1 + ":" + s2;
	}

	/**
	 * Verifica se uma string está contida na outra independente de espaços no início e fim e case sensitivity
	 */
	public static boolean containsNice(String container, String content) {
		return org.apache.commons.lang3.StringUtils.contains(container.trim().toLowerCase(), content.trim().toLowerCase());
	}
}

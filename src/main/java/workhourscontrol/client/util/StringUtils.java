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

	public static String formatarRetornoDuracaoComoHoras(double numRegPorData) {
		String s1 = String.valueOf((int) numRegPorData);
		String s2 = formatarRetornoMinutos(Math.abs(((numRegPorData%1) * 60) ));
		return s1 + ":" + s2;
	}
}

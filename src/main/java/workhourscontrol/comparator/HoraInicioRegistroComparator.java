package workhourscontrol.comparator;

import java.util.Comparator;

import workhourscontrol.client.util.DateUtils;
import workhourscontrol.entity.RegistroHora;

public class HoraInicioRegistroComparator implements Comparator<RegistroHora> {
	
	public static HoraInicioRegistroComparator instance() {
		return new HoraInicioRegistroComparator();
	}
	
	@Override
	public int compare(RegistroHora o1, RegistroHora o2) {
		if (o1.getHoraInicio() == null) { return -1; }
		if (o2.getHoraInicio() == null) { return 1; }
		return DateUtils.parseHora(o1.getHoraInicio()).compareTo(DateUtils.parseHora(o2.getHoraInicio()));
	}

}

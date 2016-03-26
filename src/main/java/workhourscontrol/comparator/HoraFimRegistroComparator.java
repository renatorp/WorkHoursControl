package workhourscontrol.comparator;

import java.util.Comparator;

import workhourscontrol.client.util.DateUtils;
import workhourscontrol.entity.RegistroHora;

public class HoraFimRegistroComparator implements Comparator<RegistroHora> {
	
	public static HoraFimRegistroComparator instance() {
		return new HoraFimRegistroComparator();
	}
	
	@Override
	public int compare(RegistroHora o1, RegistroHora o2) {
		if (o1.getHoraFim() == null) { return 1; }
		if (o2.getHoraFim() == null) { return -1; }
		return DateUtils.parseHora(o1.getHoraFim()).compareTo(DateUtils.parseHora(o2.getHoraFim()));
	}

}

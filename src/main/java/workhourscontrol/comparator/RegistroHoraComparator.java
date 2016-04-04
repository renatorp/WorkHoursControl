package workhourscontrol.comparator;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Comparator;

import workhourscontrol.client.util.DateUtils;
import workhourscontrol.entity.RegistroHora;

public class RegistroHoraComparator implements Comparator<RegistroHora> {

	@Override
	public int compare(RegistroHora o1, RegistroHora o2) {
		try {
			LocalDate dataO1 = DateUtils.parseData(o1.getDia(), o1.getMes(), o1.getAno());
			LocalDate dataO2 = DateUtils.parseData(o2.getDia(), o2.getMes(), o2.getAno());
			
			if (!dataO1.equals(dataO2)) {
				return dataO1.compareTo(dataO2);
			}
			
			return HoraInicioRegistroComparator.instance().compare(o1, o2);
			
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static RegistroHoraComparator instance() {
		return new RegistroHoraComparator();
	}

}

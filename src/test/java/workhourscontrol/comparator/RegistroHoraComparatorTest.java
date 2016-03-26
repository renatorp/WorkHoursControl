package workhourscontrol.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import workhourscontrol.entity.RegistroHora;

public class RegistroHoraComparatorTest extends TestCase{

	@Test
	public void test() {
		RegistroHora r1 = new RegistroHora();
		RegistroHora r2 = new RegistroHora();
		RegistroHora r3 = new RegistroHora();
		
		List<RegistroHora> l = new ArrayList<>();
		Collections.addAll(l, r1, r2, r3);
		
		setRegistro(r1, "1", "01", "01", "2001", "12:00");
		setRegistro(r2, "2", "02", "01", "2001", "13:00");
		setRegistro(r3, "3", "01", "03", "2001", "10:00");
		
		sort(l);
		assertEquals("1", l.get(0).getObservacao());
		
		setRegistro(r1, "1", "01", "01", "2018", "12:00");
		setRegistro(r2, "2", "02", "01", "2001", "13:00");
		setRegistro(r3, "3", "01", "03", "2001", "10:00");
		
		sort(l);
		assertEquals("2", l.get(0).getObservacao());
		
		setRegistro(r1, "1", "01", "01", "2018", "12:00");
		setRegistro(r2, "2", "01", "03", "2001", "13:00");
		setRegistro(r3, "3", "01", "03", "2001", "10:00");
		
		sort(l);
		assertEquals("3", l.get(0).getObservacao());
	}

	private void sort(List<RegistroHora> l) {
		Collections.sort(l, new RegistroHoraComparator());
	}

	private void setRegistro(RegistroHora r1, String o, String d, String m, String a, String h) {
		r1.setObservacao(o);
		r1.setDia(d);
		r1.setMes(m);
		r1.setAno(a);
		r1.setHoraInicio(h);
	}

}

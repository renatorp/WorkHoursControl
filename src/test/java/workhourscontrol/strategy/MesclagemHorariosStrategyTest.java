package workhourscontrol.strategy;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import workhourscontrol.comparator.RegistroHoraComparator;
import workhourscontrol.entity.RegistroHora;

public class MesclagemHorariosStrategyTest {

	private MesclagemHorariosStrategy strategy  = new MesclagemHorariosStrategy();

	@Test
	public void testAjustarRegistros() {

		RegistroHora r1 = criarRegistro(1, 1, 2016, "08:00", "11:00", "antes do almoço");
		RegistroHora r2 = criarRegistro(1, 1, 2016, "12:00", "17:00", "depois do almoço");
		List<RegistroHora> rs = strategy.ajustarRegistros(Arrays.asList(r1, r2));
		assertValuesOneDate(rs, 2, "08:00", "11:00", "12:00", "17:00");


		r1 = criarRegistro(1, 1, 2016, "08:00", "11:00", "antes do almoço");
		r2 = criarRegistro(1, 1, 2016, "12:00", "15:00", "depois do almoço");
		RegistroHora r3 = criarRegistro(1, 1, 2016, "15:00", "17:00", "depois de depois do almoço");
		rs = strategy.ajustarRegistros(Arrays.asList(r1, r2, r3));
		Collections.sort(rs, new RegistroHoraComparator());
		assertValuesOneDate(rs, 2, "08:00", "11:00", "12:00", "17:00");


		r1 = criarRegistro(1, 1, 2016, "08:00", "11:00", "antes do almoço");
		r2 = criarRegistro(1, 1, 2016, "11:00", "12:00", "depois do almoço");
		r3 = criarRegistro(1, 1, 2016, "13:00", "15:00", "depois de depois do almoço");
		rs = strategy.ajustarRegistros(Arrays.asList(r1, r2, r3));
		Collections.sort(rs, new RegistroHoraComparator());
		assertValuesOneDate(rs, 2, "08:00", "12:00", "13:00", "15:00");


		r1 = criarRegistro(1, 1, 2016, "08:00", "11:00", "antes do almoço");
		rs = strategy.ajustarRegistros(Arrays.asList(r1));
		Collections.sort(rs, new RegistroHoraComparator());
		assertValuesOneDate(rs, 1, "08:00", "11:00", null, null);


		r1 = criarRegistro(1, 1, 2016, "08:00", "11:00", "antes do almoço");
		r2 = criarRegistro(1, 1, 2016, "11:00", "12:00", "depois do almoço");
		r3 = criarRegistro(1, 1, 2016, "13:00", "15:30", "depois de depois do almoço");
		RegistroHora r4 = criarRegistro(1, 1, 2016, "15:30", "16:00", "depois de depois do almoço");
		rs = strategy.ajustarRegistros(Arrays.asList(r1, r2, r3, r4));
		Collections.sort(rs, new RegistroHoraComparator());
		assertValuesOneDate(rs, 2, "08:00", "12:00", "13:00", "16:00");

		r1 = criarRegistro(1, 1, 2016, "08:00", "11:00", "antes do almoço");
		r2 = criarRegistro(1, 1, 2016, "11:00", "12:00", "depois do almoço");
		r3 = criarRegistro(1, 1, 2016, "12:00", "13:30", "depois de depois do almoço");
		r4 = criarRegistro(1, 1, 2016, "14:30", "15:00", "depois de depois do almoço");
		RegistroHora r5 = criarRegistro(1, 1, 2016, "15:00", "16:30", "depois de depois do almoço");
		RegistroHora r6 = criarRegistro(1, 1, 2016, "17:30", "18:00", "depois de depois do almoço");
		RegistroHora r7 = criarRegistro(1, 1, 2016, "18:00", "19:00", "depois de depois do almoço");
		rs = strategy.ajustarRegistros(Arrays.asList(r1, r2, r3, r4, r5, r6, r7));
		Collections.sort(rs, new RegistroHoraComparator());
		assertValuesOneDate(rs, 3, "08:00", "13:30", "14:30", "16:30");

		r1 = criarRegistro(1, 1, 2016, "08:00", "11:00", "antes do almoço");
		r2 = criarRegistro(1, 1, 2016, "12:00", "13:00", "depois do almoço");
		r3 = criarRegistro(1, 1, 2016, "13:00", "14:00", "depois de depois do almoço");
		r4 = criarRegistro(2, 1, 2016, "14:00", "14:30", "depois de depois do almoço");
		r5 = criarRegistro(2, 1, 2016, "15:30", "16:30", "depois de depois do almoço");
		r6 = criarRegistro(3, 1, 2016, "07:30", "10:00", "depois de depois do almoço");
		r7 = criarRegistro(3, 1, 2016, "10:00", "19:00", "depois de depois do almoço");
		rs = strategy.ajustarRegistros(Arrays.asList(r1, r2, r3, r4, r5, r6, r7));
		Collections.sort(rs, new RegistroHoraComparator());
		assertValuesOneDate(rs, 5, "08:00", "11:00", "12:00", "14:00");

		rs.remove(0);rs.remove(0);
		assertValuesOneDate(rs, 3, "14:00", "14:30", "15:30", "16:30");

		rs.remove(0);rs.remove(0);
		assertValuesOneDate(rs, 1, "07:30", "19:00", null, null);
	}

	private void assertValuesOneDate(List<RegistroHora> rs, int size, String h1, String h2, String h3, String h4) {
		assertEquals(size, rs.size());
		assertEquals(h1, rs.get(0).getHoraInicio());
		assertEquals(h2, rs.get(0).getHoraFim());

		if (size > 1) {
			assertEquals(h3, rs.get(1).getHoraInicio());
			assertEquals(h4, rs.get(1).getHoraFim());
		}
	}

	private RegistroHora criarRegistro(Integer d, Integer m, Integer a, String hi, String hf, String o ) {
		RegistroHora r = new RegistroHora();
		r.setDia(d.toString());
		r.setMes(m.toString());
		r.setAno(a.toString());
		r.setHoraInicio(hi);
		r.setHoraFim(hf);
		r.setObservacao(o);

		return r;
	}

}

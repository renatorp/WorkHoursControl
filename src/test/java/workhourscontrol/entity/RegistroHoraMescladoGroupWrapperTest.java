package workhourscontrol.entity;

import org.junit.Test;

import junit.framework.TestCase;

public class RegistroHoraMescladoGroupWrapperTest extends TestCase {

	@Test
	public void testGetHoraInicio() {
		RegistroHora r1 = new RegistroHora();
		RegistroHora r2 = new RegistroHora();
		RegistroHora r3 = new RegistroHora();
		
		RegistroHoraMescladoGroupWrapper rg = new RegistroHoraMescladoGroupWrapper();
		rg.addAllRegistroHora(r1, r2, r3);
		
		r1.setHoraInicio("13:00");
		r2.setHoraInicio("15:00");
		r3.setHoraInicio("17:00");
		
		assertEquals("13:00", rg.getHoraInicio());
		
		r1.setHoraInicio("13:00");
		r2.setHoraInicio("15:00");
		r3.setHoraInicio("10:00");
		
		assertEquals("10:00", rg.getHoraInicio());
		
		r1.setHoraInicio("13:00");
		r2.setHoraInicio("15:00");
		r3.setHoraInicio(null);
		
		assertNull(rg.getHoraInicio());

		r1.setHoraInicio("13:00");
		r2.setHoraInicio(null);
		r3.setHoraInicio("15:00");
		
		assertNull(rg.getHoraInicio());
		
		r1.setHoraInicio("13:00");
		r2.setHoraInicio(null);
		r3.setHoraInicio(null);
		
		assertNull(rg.getHoraInicio());
	}

	@Test
	public void testGetHoraFim() {
		RegistroHora r1 = new RegistroHora();
		RegistroHora r2 = new RegistroHora();
		RegistroHora r3 = new RegistroHora();
		
		RegistroHoraMescladoGroupWrapper rg = new RegistroHoraMescladoGroupWrapper();
		rg.addAllRegistroHora(r1, r2, r3);
		
		r1.setHoraFim("13:00");
		r2.setHoraFim("15:00");
		r3.setHoraFim("17:00");
		
		assertEquals("17:00", rg.getHoraFim());
		
		r1.setHoraFim("13:00");
		r2.setHoraFim("15:00");
		r3.setHoraFim("10:00");
		
		assertEquals("15:00", rg.getHoraFim());
		
		r1.setHoraFim("13:00");
		r2.setHoraFim("15:00");
		r3.setHoraFim(null);
		
		assertNull(rg.getHoraFim());

		r1.setHoraFim("13:00");
		r2.setHoraFim(null);
		r3.setHoraFim("15:00");
		
		assertNull(rg.getHoraFim());
		
		r1.setHoraFim("13:00");
		r2.setHoraFim(null);
		r3.setHoraFim(null);
		
		assertNull(rg.getHoraFim());
	}
}

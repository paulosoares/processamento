package br.gov.stf.estf.entidade.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HoraUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHora() {
		testHoraNull();

		testHoraEmpty();

		testHoraInvalida();

		testHoraValida();
	}

	private void testHoraNull() {
		String hora = null;
		String horaExtenso = HoraUtil.hora(hora);

		assertNull(horaExtenso);
	}

	private void testHoraEmpty() {
		String hora = "";
		String horaExtenso = HoraUtil.hora(hora);

		assertNull(horaExtenso);
	}

	private void testHoraInvalida() {
		String hora = "XXX";

		try {
			HoraUtil.hora(hora);
		} catch (IllegalArgumentException exception) {
			assertNotNull(exception.getLocalizedMessage());
			return;
		}

		fail("Era esperada uma IllegalArgumentException.");
	}

	private void testHoraValida() {
		String hora = "11:12";
		String horaExtenso = HoraUtil.hora(hora);

		assertNotNull(horaExtenso);
		assertEquals("onze", horaExtenso);
	}
}

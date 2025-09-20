package util;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

public class DataUtilTest {

	@Test
	public void testIsDiaUtil() {
		Calendar hoje = Calendar.getInstance();
		hoje.set(Calendar.DAY_OF_MONTH, 8);
		hoje.set(Calendar.MONTH, Calendar.JUNE);	
		hoje.set(Calendar.YEAR, 2023);
		hoje.set(Calendar.HOUR_OF_DAY, 0);
		hoje.set(Calendar.MINUTE, 0);
		hoje.set(Calendar.SECOND, 0);
		hoje.set(Calendar.MILLISECOND, 0);
		
		Calendar feriado = Calendar.getInstance();
		feriado.set(Calendar.DAY_OF_MONTH, 8);
		feriado.set(Calendar.MONTH, Calendar.JUNE);
		feriado.set(Calendar.YEAR, 2023);
		feriado.set(Calendar.HOUR_OF_DAY, 0);
		feriado.set(Calendar.MINUTE, 0);
		feriado.set(Calendar.SECOND, 0);
		feriado.set(Calendar.MILLISECOND, 0);
		
		List<Calendar> feriados = new ArrayList<Calendar>();
		feriados.add(feriado);
		
		assertTrue(feriados.contains(hoje));
	}

}

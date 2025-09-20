package br.gov.stf.estf.entidade.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class HoraUtil {

	public static String hora(String h) {
		if (StringUtils.isNotBlank(h)) {
			String horaMinuto[] = h.split(":");

			if (!NumberUtils.isNumber(horaMinuto[0])) {
				throw new IllegalArgumentException("Valor inválido para hora: " + horaMinuto[0]);
			}

			String[] hora = { "", "uma", "duas", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez", "onze", "doze", "treze", "quatorze",
					"quinze", "dezesseis", "dezessete", "dezoito", "dezenove", "vinte", "vinte e uma", "vinte e duas", "vinte e três", "vinte e quatro" };

			return hora[Integer.parseInt(horaMinuto[0])];
		}

		return null;
	}

}

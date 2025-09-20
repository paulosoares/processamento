package br.gov.stf.estf.publicacao.model.util;

public class OrdinalUtil {

	public static String ordinal(Integer i) {

		String[] unidades = { "", "Primeira", "Segunda", "Terceira", "Quarta",
				"Quinta", "Sexta", "Sétima", "Oitava", "Nona" };

		//' inicializa dezenas

		String[] dezenas = { "", "Décima", "Vigésima", "Trigésima",
				"Quadragésima", "Quinquagésima", "Sexagésima", "Septuagésima",
				"Octogésima", "Nonagésima" };

		// inicializa centenas

		String[] centenas = { "", "Centésima", "Ducentésima", "Trecentésima",
				"Quadringentésima", "Quingentésima", "Sexcentésima",
				"Septingentésima", "Octingentésima", "Nongentésima" };

		String numero = i.toString();

		switch (numero.length()) {
		case 1:
			return unidades[Integer.valueOf(numero.substring(0, 1))];
		case 2:
			return dezenas[Integer.valueOf(numero.substring(0, 1))] + " "
					+ unidades[Integer.valueOf(numero.substring(1, 2))];
		case 3:
			return centenas[Integer.valueOf(numero.substring(0, 1))] + " "
					+ dezenas[Integer.valueOf(numero.substring(1, 2))] + " "
					+ unidades[Integer.valueOf(numero.substring(2, 3))];
		}
		return null;
	}

}

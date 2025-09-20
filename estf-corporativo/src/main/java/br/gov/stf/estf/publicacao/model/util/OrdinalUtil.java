package br.gov.stf.estf.publicacao.model.util;

public class OrdinalUtil {

	public static String ordinal(Integer i) {

		String[] unidades = { "", "Primeira", "Segunda", "Terceira", "Quarta",
				"Quinta", "Sexta", "S�tima", "Oitava", "Nona" };

		//' inicializa dezenas

		String[] dezenas = { "", "D�cima", "Vig�sima", "Trig�sima",
				"Quadrag�sima", "Quinquag�sima", "Sexag�sima", "Septuag�sima",
				"Octog�sima", "Nonag�sima" };

		// inicializa centenas

		String[] centenas = { "", "Cent�sima", "Ducent�sima", "Trecent�sima",
				"Quadringent�sima", "Quingent�sima", "Sexcent�sima",
				"Septingent�sima", "Octingent�sima", "Nongent�sima" };

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

package br.gov.stf.estf.entidade.util;

import org.apache.commons.lang.Validate;

public class NomeProprioUtil {

	/**
	 * M�todo que retorna uma string no padr�o primeira letra de cada palavra (exceto preposi��es e conjun��es) mai�scula. Ex.: Celso de Mello
	 * Preposi��es: de, da, do
	 * Conjun��es: e, a, o
	 * @param String
	 * @return String
	 * @author Rodrigo.Lisboa
	 */
	public static String primeiraMaiuscula(String n) {
		Validate.notEmpty(n, "String n�o informada!");

		String[] valor = n.toUpperCase().split(" ");
		StringBuffer novoValor = new StringBuffer();

		String[] manterMinusculo = { "A", "E", "O", "DA", "DE", "DO", };
		boolean existeValor = false;

		// varre o vetor valor
		for (int x = 0; x < valor.length; x++) {
			// varre o vetor manterMinusculo
			for (int y = 0; y < manterMinusculo.length; y++)
				if (valor[x].equals(manterMinusculo[y])) {
					existeValor = true;
					break;
				}

			if (!existeValor) {
				novoValor.append(valor[x].substring(0, 1).toUpperCase());
				novoValor.append(valor[x].substring(1, valor[x].length()).toLowerCase() + " ");
			} else {
				novoValor.append(valor[x].substring(0, valor[x].length()).toLowerCase() + " ");
			}

			existeValor = false;
		}
		// retira o �ltimo espa�o em branco
		if (novoValor.substring(novoValor.length() - 1, novoValor.length()).equals(" "))
			novoValor.delete(novoValor.length() - 1, novoValor.length());

		return novoValor.toString();
	}
	/*
	public static void main(String[] args) {
		System.out.println(primeiraMaiuscula("MIN. CELSO DE MELLO"));
	}
	*/

}

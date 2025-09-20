package br.gov.stf.estf.assinatura.visao.util;

/**
 * 
 * @author francisco.fiuza
 */
public class ProcessoParser {

	public static String getSigla(String siglaNumero) {

		char[] caracteres = siglaNumero.toCharArray();
		String sigla = null;
		int i = 0;
		boolean comecouContagem = false;
		int inicioContagem = 0;
		for (; i < caracteres.length; i++) {
			if (Character.isLetter(caracteres[i]) && !comecouContagem) {
				inicioContagem = i;
				comecouContagem = true;
			}
			if (comecouContagem) {
				if (!Character.isLetter(caracteres[i])) {
					sigla = siglaNumero.substring(inicioContagem, i);
					break;
				} else if (i == caracteres.length - 1) {
					sigla = siglaNumero.substring(inicioContagem, i + 1);
					break;
				}
			}
		}

		return sigla;
	}

	public static Long getNumero(String siglaNumero) {

		String numero = null;
		char[] caracteres = siglaNumero.toCharArray();
		boolean comecouContagem = false;
		int i = 0;
		int inicioContagem = 0;
		for (; i < caracteres.length; i++) {
			if (Character.isDigit(caracteres[i]) && !comecouContagem) {
				inicioContagem = i;
				comecouContagem = true;
			}
			if (comecouContagem) {
				if (!Character.isDigit(caracteres[i])) {
					numero = siglaNumero.substring(inicioContagem, i);
					break;
				} else if (i == caracteres.length - 1) {
					numero = siglaNumero.substring(inicioContagem, i + 1);
					break;
				}
			}
		}
		
		return numero == null ? null : new Long(numero);
	}
	
	public static String getNumeroPet(String siglaNumero) {

		String numero = null;
		char[] caracteres = siglaNumero.toCharArray();
		boolean comecouContagem = false;
		int i = 0;
		int inicioContagem = 0;
		for (; i < caracteres.length; i++) {
			if (Character.isDigit(caracteres[i]) && !comecouContagem) {
				inicioContagem = i;
				comecouContagem = true;
			}
			if (comecouContagem) {
				if (!Character.isDigit(caracteres[i]) && caracteres[i] != '/') {
					numero = siglaNumero.substring(inicioContagem, i);
					break;
				} else if (i == caracteres.length - 1) {
					numero = siglaNumero.substring(inicioContagem, i + 1);
					break;
				}
			}
		}
		
		return numero == null ? null : new String(numero);
	}
}

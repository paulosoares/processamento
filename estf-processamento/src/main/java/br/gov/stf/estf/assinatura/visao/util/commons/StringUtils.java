package br.gov.stf.estf.assinatura.visao.util.commons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe utilitária para realizar operações diversas sobre Strings.
 * 
 * @author thiago.miranda
 * @since 3.14.0
 */
public class StringUtils {

	private static final String STRING_ACENTOS = "àâêôûãõáéíóúçüÀÂÊÔÛÃÕÁÉÍÓÚÇÜ";
	private static final String STRING_SEM_ACENTOS = "aaeouaoaeioucuAAEOUAOAEIOUCU";

	private StringUtils() {

	}

	/**
	 * Verifica se uma determina String está vazia (ou seja, se é nula, "" ou formada apenas por espaços em branco).<br />
	 * <br />
	 * 
	 * Exemplos de situações em que esse método pode ser chamado:
	 * 
	 * <pre>
	 * StringUtils.isEmpty(null)		= true
	 * StringUtils.isEmpty("")		= true
	 * StringUtils.isEmpty(" ")		= false
	 * StringUtils.isEmpty("bob")		= false
	 * StringUtils.isEmpty("  bob  ")	= false
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isVazia(String s) {
		return org.apache.commons.lang.StringUtils.isBlank(s);
	}

	/**
	 * Funciona de maneira inversa a {@link #isVazia(String)}, verificando se uma dada String não é nula, igual a "" e nem formada apenas por espaços em branco.
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNotVazia(String s) {
		return !isVazia(s);
	}

	/**
	 * Troca todos os espaços em branco de uma String por "_".
	 * 
	 * @param s
	 * @return
	 */
	public static String substituiEspacosBrancoPorUnderline(String s) {
		String padrao = "\\s";
		Pattern regPat = Pattern.compile(padrao);
		Matcher matcher = regPat.matcher(s);
		String res = matcher.replaceAll("_");
		return res;
	}

	/**
	 * Verifica se umda dada String é não-nula e vazia e, caso seja, retorna um valor nulo.
	 * 
	 * @param s
	 * @return
	 */
	public static String getStringNulaSeVazia(String s) {
		String retorno = s;

		if (s != null && s.trim().length() == 0) {
			retorno = null;
		}

		return retorno;
	}

	/**
	 * Retira todos os acentos e cedilhas de uma dada string.<br />
	 * <br />
	 * 
	 * Exemplo de utilização:
	 * 
	 * <pre>
	 * String mensagem = &quot;A petição é única e está em análise&quot;;
	 * mensagem = StringUtils.retirarAcentos(mensagem);
	 * // mensagem = A peticao e unica e esta em analise
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static String retirarAcentos(String s) {
		String varString = new String(s);

		int i = 0;
		String cString = "";

		for (int j = 0; j < varString.length(); j++) {
			i = STRING_ACENTOS.indexOf(varString.substring(j, j + 1), 0);
			if (i >= 0) {
				cString += STRING_SEM_ACENTOS.substring(i, i + 1);
			} else {
				cString += varString.substring(j, j + 1);
			}

		}
		return cString;
	}
}

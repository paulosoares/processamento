package br.gov.stf.estf.assinatura.visao.util.constantes;

import java.util.Locale;
import java.util.PropertyResourceBundle;

/**
 * Obtém dados do arquivo de constantes.
 * 
 * @author <a href="mailto:leonardo@stf.gov.br">Leonardo Inacio Costa</a>
 * @version 1.0
 */
public class ConstantesResourceReader {

	/**
	 * Construtor padrão.
	 */
	private ConstantesResourceReader() {
	}

	/**
	 * Resource bundle que contém o arquivo.
	 */
	private static PropertyResourceBundle oResBundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle("constantes", Locale.getDefault(),
			Constantes.class.getClassLoader());

	/**
	 * Recupera a propriedade a partir da chave informada, se não encontrar o valor pra chave ou o bundle estiver vazio, retorna um String vazio.
	 * 
	 * @param pKey
	 *            chave da propriedade
	 * @return Valor da propriedade
	 */
	public static String getProperty(String pKey) {
		try {
			String prop = oResBundle.getString(pKey);

			if (prop != null) {
				prop = prop.trim();
			}

			return prop;
		} catch (Throwable e) {
			return "";
		}
	}

	public static boolean isInitialized() {
		return oResBundle != null;
	}
}

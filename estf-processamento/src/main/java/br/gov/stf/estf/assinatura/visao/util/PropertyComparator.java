package br.gov.stf.estf.assinatura.visao.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * Comparador genérico de propriedades de objeto. A cadeia de nomes de
 * propriedades deve possuir o formato:
 * 
 * propriedade1.propriedade2.propriedadeN
 * 
 * Onde a última propriedade será a utilizada nas comparações e, portanto, deve
 * implementar a interface Comparable (direta ou indiretamente).
 * 
 * Também é aceito que seja passado apenas um nome de propriedade.
 * 
 * @author thiago.miranda
 */
public class PropertyComparator<OBJETO> implements Comparator<OBJETO> {

	private String[] nomesPropriedades;
	private TipoOrdenacao tipoOrdenacao;

	public PropertyComparator(TipoOrdenacao tipoOrdenacao, String... cadeiaNomesPropriedades) {
		this.nomesPropriedades = cadeiaNomesPropriedades;
		this.tipoOrdenacao = tipoOrdenacao;
	}

	@Override
	public int compare(OBJETO objeto1, OBJETO objeto2) {
		int comparacao = 0;
		Comparable<?> propriedade1 = null;
		Comparable<?> propriedade2 = null;

		for (String nomePropriedade : nomesPropriedades) {
			try {
				propriedade1 = getPropriedade(objeto1, nomePropriedade);
				propriedade2 = getPropriedade(objeto2, nomePropriedade);

				comparacao = new CompareToBuilder().append(propriedade1, propriedade2).toComparison() * tipoOrdenacao.getIndice();
				if (comparacao != 0) {
					break;
				}
			} catch (Exception exception) {
				throw new IllegalArgumentException("Erro ao obter propriedade dos objetos: " + nomePropriedade, exception);
			}
		}

		return comparacao;
	}

	private Comparable<?> getPropriedade(OBJETO objeto, String nomePropriedade) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return (Comparable<?>) PropertyUtils.getProperty(objeto, nomePropriedade);
	}
}
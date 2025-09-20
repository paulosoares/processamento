package br.gov.stf.estf.assinatura.service.impl;

import java.util.Collection;
import java.util.Map;

/**
 * Classe utilit�ria para realizar opera��es diversas sobre cole��es.
 * 
 * @author thiago.miranda
 * @since 3.14.0
 */
public class CollectionUtils {

	private CollectionUtils() {

	}

	/**
	 * Verifica se uma determinada cole��o � nula ou vazia. Dada uma cole��o X, o comando <code>CollectionUtils.isVazia(X)</code> se equivale a utilizar
	 * <code>X == null || X.isEmpty()</code>
	 * 
	 * @param colecao
	 * @return
	 */
	public static boolean isVazia(Collection<?> colecao) {
		return colecao == null || colecao.isEmpty();
	}

	/**
	 * Funciona de modo contr�rio ao m�todo {@link #isVazia(Collection)}, verificando se uma dada cole��o n�o � nula e n�o est� vazia.
	 * 
	 * @param colecao
	 * @return
	 */
	public static boolean isNotVazia(Collection<?> colecao) {
		return !isVazia(colecao);
	}

	/**
	 * Verifica se um determinado mapa � nulo ou vazio. Dado um mapa X, o comando <code>CollectionUtils.isVazia(X)</code> se equivale a utilizar
	 * <code>X == null || X.isEmpty()</code>
	 * 
	 * @param mapa
	 * @return
	 */
	public static boolean isVazia(Map<?, ?> mapa) {
		return mapa == null || mapa.isEmpty();
	}

	/**
	 * Funciona de modo contr�rio ao m�todo {@link #isVazia(Map)}, verificando se um dado mapa n�o � nulo e n�o est� vazio.
	 * 
	 * @param colecao
	 * @return
	 */
	public static boolean isNotVazia(Map<?, ?> mapa) {
		return !isVazia(mapa);
	}

	/**
	 * <p>
	 * Verifica se uma dada cole��o possui ao menos certa quantidade de elementos.
	 * </p>
	 * 
	 * <p>
	 * Exemplos de utiliza��o:
	 * </p>
	 * 
	 * <pre>
	 * Collection<String> naoVazia = Arrays.asList("A", "B", "C");
	 * Collection<String> vazia = Collections.emptyList();
	 * 
	 * CollectionUtils.possuiAoMenos(null, 2)  	= false
	 * CollectionUtils.possuiAoMenos(vazia, 2)    	= false
	 * CollectionUtils.possuiAoMenos(naoVazia, 2) 	= true
	 * CollectionUtils.possuiAoMenos(null, 5)  	= false
	 * CollectionUtils.possuiAoMenos(vazia, 5)    	= false
	 * CollectionUtils.possuiAoMenos(naoVazia, 5) 	= false
	 * </pre>
	 * 
	 * @param colecao
	 * @param quantidadeElementos
	 * @return
	 * @throws IllegalArgumentException
	 *             se quantidadeElementos <= 0
	 */
	public static boolean possuiAoMenos(Collection<?> colecao, int quantidadeElementos) {
		return isNotVazia(colecao) && colecao.size() >= quantidadeElementos;
	}
}

package br.gov.stf.estf.assinatura.service.impl;

import java.util.Collection;
import java.util.Map;

/**
 * Classe utilitária para realizar operações diversas sobre coleções.
 * 
 * @author thiago.miranda
 * @since 3.14.0
 */
public class CollectionUtils {

	private CollectionUtils() {

	}

	/**
	 * Verifica se uma determinada coleção é nula ou vazia. Dada uma coleção X, o comando <code>CollectionUtils.isVazia(X)</code> se equivale a utilizar
	 * <code>X == null || X.isEmpty()</code>
	 * 
	 * @param colecao
	 * @return
	 */
	public static boolean isVazia(Collection<?> colecao) {
		return colecao == null || colecao.isEmpty();
	}

	/**
	 * Funciona de modo contrário ao método {@link #isVazia(Collection)}, verificando se uma dada coleção não é nula e não está vazia.
	 * 
	 * @param colecao
	 * @return
	 */
	public static boolean isNotVazia(Collection<?> colecao) {
		return !isVazia(colecao);
	}

	/**
	 * Verifica se um determinado mapa é nulo ou vazio. Dado um mapa X, o comando <code>CollectionUtils.isVazia(X)</code> se equivale a utilizar
	 * <code>X == null || X.isEmpty()</code>
	 * 
	 * @param mapa
	 * @return
	 */
	public static boolean isVazia(Map<?, ?> mapa) {
		return mapa == null || mapa.isEmpty();
	}

	/**
	 * Funciona de modo contrário ao método {@link #isVazia(Map)}, verificando se um dado mapa não é nulo e não está vazio.
	 * 
	 * @param colecao
	 * @return
	 */
	public static boolean isNotVazia(Map<?, ?> mapa) {
		return !isVazia(mapa);
	}

	/**
	 * <p>
	 * Verifica se uma dada coleção possui ao menos certa quantidade de elementos.
	 * </p>
	 * 
	 * <p>
	 * Exemplos de utilização:
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

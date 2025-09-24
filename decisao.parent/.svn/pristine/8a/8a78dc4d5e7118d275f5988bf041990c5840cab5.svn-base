package br.jus.stf.estf.decisao.support.controller.faces.datamodel;

import java.util.List;

/**
 * Representa um lista paginada de registros.
 * 
 * @author Rodrigo Barreiros
 *
 * @param <T> o tipo do registro
 */
public class PagedList<T> {

	private List<T> results;

	private int start;

	private int total;
	
	private Boolean hasMorePagesLoaded;

	/**
	 * Constroi a lista paginada informando todos os seus atributos.
	 * 
	 * @param results a lista de registros
	 * @param start o index do primeiro registo
	 * @param total a quantidade total de registros
	 */
	public PagedList(List<T> results, int start, int total) {
		this.results = results;
		this.start = start;
		this.total = total;
		this.hasMorePagesLoaded = Boolean.FALSE;
	}
	
	public PagedList(List<T> results, int start, int total, Boolean hasMorePagesLoaded) {
		this.results = results;
		this.start = start;
		this.total = total;
		this.hasMorePagesLoaded = hasMorePagesLoaded;
	}

	/**
	 * Retorna o index do primeiro registro recuperado a partir 
	 * da base de dados.
	 * 
	 * @return o index do primeiro registro
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Retorna a lista de registros recuperados.
	 * 
	 * @return a lista de registros
	 */
	public List<T> getResults() {
		return results;
	}

	/**
	 * Retorna a quantidade total de registros encontrados
	 * para a pesquisa que resultou nessa lista paginada
	 * 
	 * @return a quantidade total de registros
	 */
	public int getTotal() {
		return total;
	}

	public Boolean hasMorePagesLoaded() {
		if (hasMorePagesLoaded == null) {
			hasMorePagesLoaded = Boolean.FALSE;
		}
		return hasMorePagesLoaded;
	}

}

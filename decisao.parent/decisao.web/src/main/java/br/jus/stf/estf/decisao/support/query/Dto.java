package br.jus.stf.estf.decisao.support.query;

import java.io.Serializable;


/**
 * Interface DTO para implementação pelas entidades de retorno na
 * pesquisa avançada.
 * 
 * @author Rodrigo Barreiros
 * 02.06.2010
 */
public interface Dto extends Selectable, Serializable {

	/**
	 * Retorna o identificador da entidade representada no DTO.
	 * 
	 * @return o identificador
	 */
	Long getId();
	
	
	/**
	 * Retorna um indicativo se o objeto é um fake.
	 * 
	 * @return true or false
	 */
	boolean isFake();

}

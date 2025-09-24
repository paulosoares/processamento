package br.jus.stf.estf.decisao.support.query;

import java.io.Serializable;


/**
 * Interface DTO para implementa��o pelas entidades de retorno na
 * pesquisa avan�ada.
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
	 * Retorna um indicativo se o objeto � um fake.
	 * 
	 * @return true or false
	 */
	boolean isFake();

}

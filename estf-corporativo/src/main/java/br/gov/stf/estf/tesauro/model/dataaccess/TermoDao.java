/**
 * 
 */
package br.gov.stf.estf.tesauro.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.tesauro.Termo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 19.07.2012
 */
public interface TermoDao extends GenericDao<Termo, Long> {

	List<Termo> pesquisarPorDescricao(String suggestion, boolean termoExato) throws DaoException;
	
	List<Termo> pesquisarPorDescricaoExata(String suggestion, boolean termoExato) throws DaoException;

}

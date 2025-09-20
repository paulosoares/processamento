package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.TipoControle;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoControleDao extends GenericDao<TipoControle, Long> {

	public List<TipoControle> pesquisarTodosControles() throws DaoException;
	
	TipoControle recuperarPorSigla(String sigla) throws DaoException;

}

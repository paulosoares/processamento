package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.TipoRecurso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * DAO interface for domain model class TipoRecurso.
 */
public interface TipoRecursoDao extends GenericDao<TipoRecurso, Long> {
	
	public TipoRecurso recuperarTipoRecurso(Long id) throws DaoException;

	public TipoRecurso recuperarTipoRecurso(String sigla) throws DaoException;

	public List<TipoRecurso> recuperarTipoRecurso(boolean ativos) throws DaoException;

}

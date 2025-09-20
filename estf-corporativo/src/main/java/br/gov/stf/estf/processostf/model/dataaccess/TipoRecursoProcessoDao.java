package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.TipoRecursoProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoRecursoProcessoDao extends GenericDao<TipoRecursoProcesso, Long> {
	
	public List<TipoRecursoProcesso> pesquisar(Boolean ativo) throws DaoException;

}

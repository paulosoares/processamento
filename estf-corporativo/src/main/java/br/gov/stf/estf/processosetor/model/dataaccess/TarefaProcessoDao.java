package br.gov.stf.estf.processosetor.model.dataaccess;

import br.gov.stf.estf.entidade.processosetor.TarefaProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TarefaProcessoDao extends GenericDao<TarefaProcesso, Long> {
	
	public TarefaProcesso recuperarTarefaProcesso(Long id)
	throws DaoException;
}

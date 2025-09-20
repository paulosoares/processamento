package br.gov.stf.estf.processosetor.model.service;

import br.gov.stf.estf.entidade.processosetor.TarefaProcesso;
import br.gov.stf.estf.processosetor.model.dataaccess.TarefaProcessoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TarefaProcessoService extends GenericService<TarefaProcesso, Long, TarefaProcessoDao> {
	
	//TODO javadoc
	public TarefaProcesso recuperarTarefaProcesso(Long id)
	throws ServiceException;
}

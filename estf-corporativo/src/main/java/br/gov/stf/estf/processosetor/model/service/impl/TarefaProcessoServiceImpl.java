package br.gov.stf.estf.processosetor.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processosetor.TarefaProcesso;
import br.gov.stf.estf.processosetor.model.dataaccess.TarefaProcessoDao;
import br.gov.stf.estf.processosetor.model.service.TarefaProcessoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tarefaProcessoService")
public class TarefaProcessoServiceImpl extends GenericServiceImpl<TarefaProcesso, Long, TarefaProcessoDao>  
implements TarefaProcessoService {
	
	protected TarefaProcessoServiceImpl(TarefaProcessoDao dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}
	
	public TarefaProcesso recuperarTarefaProcesso(Long id)
	throws ServiceException
	{
		TarefaProcesso tarefaProcesso = null;
		
		try
		{
			tarefaProcesso = dao.recuperarTarefaProcesso(id);
		}
		catch(DaoException e)
		{
			throw new ServiceException(e);
		}
		
		return tarefaProcesso;
	}
	
	
}

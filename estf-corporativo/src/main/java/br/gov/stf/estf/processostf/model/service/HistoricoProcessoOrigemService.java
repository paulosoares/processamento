package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.HistoricoProcessoOrigem;
import br.gov.stf.estf.processostf.model.dataaccess.HistoricoProcessoOrigemDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface HistoricoProcessoOrigemService extends GenericService<HistoricoProcessoOrigem, Long, HistoricoProcessoOrigemDao> {
	
	public List<HistoricoProcessoOrigem> recuperarPorObjetoIncidente(Long objetoIncidente) throws ServiceException;
	
	public List<HistoricoProcessoOrigem> recuperarTodosPorObjetoIncidente(Long objetoIncidente) throws ServiceException;

    public HistoricoProcessoOrigem recuperarOrigemInicialSTJ(Long idObjetoIncidente) throws ServiceException;
}

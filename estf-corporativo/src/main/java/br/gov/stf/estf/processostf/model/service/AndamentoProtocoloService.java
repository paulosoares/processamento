package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.processostf.AndamentoProtocolo;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoProtocoloDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AndamentoProtocoloService extends GenericService<AndamentoProtocolo, Long, AndamentoProtocoloDao> {
	public Long recuperarUltimoNumeroSequencia (Protocolo protocolo) throws ServiceException;
	
	void persistirAndamentoProtocolo(AndamentoProtocolo andamentoProtocolo) throws ServiceException;
}

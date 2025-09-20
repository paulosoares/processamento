package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.processostf.DeslocaProtocolo;
import br.gov.stf.estf.processostf.model.dataaccess.DeslocamentoProtocoloDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface DeslocamentoProtocoloService extends GenericService<DeslocaProtocolo, Long, DeslocamentoProtocoloDao> {

	public void persistirDeslocamentoProtocolo(DeslocaProtocolo deslocamentoProtocolo) 
	throws ServiceException;
}

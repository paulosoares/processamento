package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.AndamentoProtocolo;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoProtocoloDao;
import br.gov.stf.estf.processostf.model.service.AndamentoProtocoloService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("andamentoProtocoloService")
public class AndamentoProtocoloServiceImpl extends GenericServiceImpl<AndamentoProtocolo, Long, AndamentoProtocoloDao> 
	implements AndamentoProtocoloService  {
    public AndamentoProtocoloServiceImpl(AndamentoProtocoloDao dao) { super(dao); }

	public Long recuperarUltimoNumeroSequencia(Protocolo protocolo)
			throws ServiceException {
		Long sequencia = null;
		try {
			sequencia = dao.recuperarUltimoNumeroSequencia(protocolo);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return sequencia;
	}
	
	public void persistirAndamentoProtocolo(AndamentoProtocolo andamentoProtocolo) throws ServiceException {
		
		try {
			dao.persistirAndamentoProtocolo(andamentoProtocolo);
		} 
		catch(DaoException e) {
			throw new ServiceException(e);
		}
	}

}

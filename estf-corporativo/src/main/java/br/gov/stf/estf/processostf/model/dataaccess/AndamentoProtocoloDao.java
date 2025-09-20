package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.processostf.AndamentoProtocolo;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AndamentoProtocoloDao extends GenericDao<AndamentoProtocolo, Long> {
	public Long recuperarUltimoNumeroSequencia (Protocolo protocolo) throws DaoException;
	
	void persistirAndamentoProtocolo(AndamentoProtocolo andamentoProtocolo) throws DaoException;
}

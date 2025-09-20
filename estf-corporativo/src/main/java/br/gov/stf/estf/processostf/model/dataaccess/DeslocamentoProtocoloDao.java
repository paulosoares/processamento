package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.processostf.DeslocaProtocolo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DeslocamentoProtocoloDao extends GenericDao<DeslocaProtocolo, Long> {

	public void persistirDeslocamentoProtocolo(DeslocaProtocolo deslocamentoProtocolo) 
	throws DaoException;
}

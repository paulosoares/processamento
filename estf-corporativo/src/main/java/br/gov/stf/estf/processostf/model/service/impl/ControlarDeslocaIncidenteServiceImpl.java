package br.gov.stf.estf.processostf.model.service.impl;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ControlarDeslocaIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.ControlarDeslocaIncidenteDao;
import br.gov.stf.estf.processostf.model.service.ControlarDeslocaIncidenteService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("controlarDeslocaIncidenteService")
public class ControlarDeslocaIncidenteServiceImpl extends GenericServiceImpl<ControlarDeslocaIncidente, Long, ControlarDeslocaIncidenteDao> 
	implements ControlarDeslocaIncidenteService {

	protected ControlarDeslocaIncidenteServiceImpl(
			ControlarDeslocaIncidenteDao dao) {
		super(dao);
	}
	
	public void insereObjetoIncidente(ControlarDeslocaIncidente controlarDeslocaIncidente) throws ServiceException, SQLException {
		try {
			dao.insereObjetoIncidente(controlarDeslocaIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}

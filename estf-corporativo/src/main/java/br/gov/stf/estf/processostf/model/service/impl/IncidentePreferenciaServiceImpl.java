package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.IncidentePreferencia;
import br.gov.stf.estf.processostf.model.dataaccess.IncidentePreferenciaDao;
import br.gov.stf.estf.processostf.model.service.IncidentePreferenciaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("incidentePreferenciaService")
public class IncidentePreferenciaServiceImpl extends GenericServiceImpl<IncidentePreferencia, Long, IncidentePreferenciaDao>
		implements IncidentePreferenciaService {

	public IncidentePreferenciaServiceImpl(IncidentePreferenciaDao dao) {
		super(dao);
	}
	
	public void inserirPreferenciaConvertidoEletronico(IncidentePreferencia incidentePreferencia)
			throws ServiceException{
		try {
			dao.inserirPreferenciaConvertidoEletronico(incidentePreferencia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}		
	}

	public void removerPreferenciaConvertidoEletronico(IncidentePreferencia incidentePreferencia)
			throws ServiceException{
		try {
			dao.removerPreferenciaConvertidoEletronico(incidentePreferencia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}		
	}
	
}

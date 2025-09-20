package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.processostf.IncidentePreferencia;
import br.gov.stf.estf.processostf.model.dataaccess.IncidentePreferenciaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface IncidentePreferenciaService extends GenericService<IncidentePreferencia, Long, IncidentePreferenciaDao> {

	public void inserirPreferenciaConvertidoEletronico(IncidentePreferencia incidentePreferencia)
			throws ServiceException;

	public void removerPreferenciaConvertidoEletronico(IncidentePreferencia incidentePreferencia)
			throws ServiceException;
	
}

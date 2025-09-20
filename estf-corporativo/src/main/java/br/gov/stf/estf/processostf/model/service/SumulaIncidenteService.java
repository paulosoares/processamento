package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.processostf.SumulaIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.SumulaIncidenteDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface SumulaIncidenteService extends GenericService<SumulaIncidente, Long, SumulaIncidenteDao>{
	
	public SumulaIncidente pesquisaSumulaIncidente(Long idSumula, Long idProcessoPrecedente) throws ServiceException;

}

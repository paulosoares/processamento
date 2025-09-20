package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.SumulaIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.SumulaIncidenteDao;
import br.gov.stf.estf.processostf.model.service.SumulaIncidenteService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("sumulaIncidenteService")
public class SumulaIncidenteServiceImpl extends GenericServiceImpl<SumulaIncidente, Long, SumulaIncidenteDao> 
implements SumulaIncidenteService {

	public SumulaIncidenteServiceImpl(SumulaIncidenteDao dao) {
		super(dao);
	}

	public SumulaIncidente pesquisaSumulaIncidente(Long idSumula,
			Long idProcessoPrecedente) throws ServiceException {
		SumulaIncidente si = new SumulaIncidente();
		
		try {
			si = dao.pesquisaSumulaIncidente(idSumula, idProcessoPrecedente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
		return si;
	}
}

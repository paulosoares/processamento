package br.gov.stf.estf.processostf.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.estf.processostf.model.dataaccess.IncidenteDistribuicaoDao;
import br.gov.stf.estf.processostf.model.service.IncidenteDistribuicaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("incidenteDistribuicaoService")
public class IncidenteDistribuicaoServiceImpl extends GenericServiceImpl<IncidenteDistribuicao, Long, IncidenteDistribuicaoDao> 
    implements IncidenteDistribuicaoService{
    public IncidenteDistribuicaoServiceImpl(IncidenteDistribuicaoDao dao) { super(dao); }

	public List<IncidenteDistribuicao> pesquisar(Integer numAta, Date dataAta) throws ServiceException {
		List<IncidenteDistribuicao> resp = null;
		
		try {
			resp = dao.pesquisar(numAta, dataAta);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		
		return resp;
	}
    
}

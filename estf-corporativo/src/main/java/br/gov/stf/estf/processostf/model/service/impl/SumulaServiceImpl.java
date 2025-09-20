package br.gov.stf.estf.processostf.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Sumula;
import br.gov.stf.estf.processostf.model.dataaccess.SumulaDao;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.SumulaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("sumulaService")
public class SumulaServiceImpl extends GenericServiceImpl<Sumula, Long, SumulaDao> 
	implements SumulaService {
	
	private final ObjetoIncidenteService objetoIncidenteService;
	
    public SumulaServiceImpl(SumulaDao dao, ObjetoIncidenteService objetoIncidenteService) { 
    	super(dao);
    	this.objetoIncidenteService = objetoIncidenteService;
    }

	public Long recuperarNumeroUltimaSumula() throws ServiceException {
		Long numero = null;
		try {
			numero = dao.recuperarNumeroUltimaSumula();
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return numero;
	}
	
	public Long recuperarNumeroUltimaSeqSumula() throws ServiceException {
		Long numero = null;
		try {
			numero = dao.recuperarNumeroUltimaSeqSumula();
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return numero;
	}
	
	public List<Sumula> pesquisarSumula(Long numeroSumula, Long processoPrecedente, String descricaoVerbete, 
			Date dataAprovacao, String tipoSumula) throws ServiceException{

		List<Sumula> lista = null;
			
		try {
			lista = dao.pesquisarSumula(numeroSumula, processoPrecedente, descricaoVerbete, dataAprovacao, tipoSumula);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return lista;
	}

}

/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.LegislacaoIncidenteAnalise;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.LegislacaoIncidenteAnaliseDao;
import br.gov.stf.estf.jurisprudencia.model.service.LegislacaoIncidenteAnaliseService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 03.08.2012
 */
@Service("legislacaoIncidenteAnaliseService")
public class LegislacaoIncidenteAnaliseServiceImpl extends
		GenericServiceImpl<LegislacaoIncidenteAnalise, Long, LegislacaoIncidenteAnaliseDao> implements
		LegislacaoIncidenteAnaliseService {

	protected LegislacaoIncidenteAnaliseServiceImpl(LegislacaoIncidenteAnaliseDao dao) {
		super(dao);
	}

	@Override
	public byte[] recuperarVisualizacaoLegislacaoSucessivo(LegislacaoIncidenteAnalise legislacaoIncidenteAnalise)
			throws ServiceException {
		try {
			return dao.recuperarVisualizacaoLegislacaoSucessivo(legislacaoIncidenteAnalise);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import br.gov.stf.estf.entidade.jurisprudencia.LegislacaoIncidenteAnalise;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.LegislacaoIncidenteAnaliseDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 03.08.2012
 */
public interface LegislacaoIncidenteAnaliseService extends
		GenericService<LegislacaoIncidenteAnalise, Long, LegislacaoIncidenteAnaliseDao> {

	byte[] recuperarVisualizacaoLegislacaoSucessivo(LegislacaoIncidenteAnalise legislacaoIncidenteAnalise)
			throws ServiceException;

}

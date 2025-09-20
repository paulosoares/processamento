/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import br.gov.stf.estf.entidade.jurisprudencia.LegislacaoIncidenteAnalise;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 03.08.2012
 */
public interface LegislacaoIncidenteAnaliseDao extends GenericDao<LegislacaoIncidenteAnalise, Long> {

	byte[] recuperarVisualizacaoLegislacaoSucessivo(LegislacaoIncidenteAnalise legislacaoIncidenteAnalise)
			throws DaoException;

}

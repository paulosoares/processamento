/**
 * 
 */
package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidenteListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidenteListaProcessos.ObjetoIncidenteListaProcessosId;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 25.05.2011
 */
public interface ObjetoIncidenteListaProcessosDao extends
		GenericDao<ObjetoIncidenteListaProcessos, ObjetoIncidenteListaProcessosId> {

	ObjetoIncidenteListaProcessos recuperar(
			ListaProcessos listaProcessos, ObjetoIncidente<?> objetoIncidente) throws DaoException;

}

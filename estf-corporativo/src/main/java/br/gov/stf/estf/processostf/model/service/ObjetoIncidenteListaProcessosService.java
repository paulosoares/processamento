/**
 * 
 */
package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidenteListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidenteListaProcessos.ObjetoIncidenteListaProcessosId;
import br.gov.stf.estf.processostf.model.dataaccess.ObjetoIncidenteListaProcessosDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 25.05.2011
 */
public interface ObjetoIncidenteListaProcessosService extends
		GenericService<ObjetoIncidenteListaProcessos, ObjetoIncidenteListaProcessosId, ObjetoIncidenteListaProcessosDao> {

	ObjetoIncidenteListaProcessos recuperar(ListaProcessos listaProcessos, ObjetoIncidente<?> objetoIncidente) throws ServiceException;
}

/**
 * 
 */
package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidenteListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidenteListaProcessos.ObjetoIncidenteListaProcessosId;
import br.gov.stf.estf.processostf.model.dataaccess.ObjetoIncidenteListaProcessosDao;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteListaProcessosService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 25.05.2011
 */
@Service("objetoIncidenteListaProcessosService")
public class ObjetoIncidenteListaProcessosServiceImpl extends
		GenericServiceImpl<ObjetoIncidenteListaProcessos, ObjetoIncidenteListaProcessosId, ObjetoIncidenteListaProcessosDao> implements
		ObjetoIncidenteListaProcessosService {

	protected ObjetoIncidenteListaProcessosServiceImpl(
			ObjetoIncidenteListaProcessosDao dao) {
		super(dao);

	}

	@Override
	public ObjetoIncidenteListaProcessos recuperar(
			ListaProcessos listaProcessos, ObjetoIncidente<?> objetoIncidente)
			throws ServiceException {
		try {
			return dao.recuperar(listaProcessos, objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	
}

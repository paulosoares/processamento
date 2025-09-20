/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.ObservacaoLivreJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.TipoOrdenacaoObservacaoJurisprudencia;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.ObservacaoLivreJurisprudenciaDao;
import br.gov.stf.estf.jurisprudencia.model.service.ObservacaoLivreJurisprudenciaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 08.10.2012
 */
@Service("observacaoLivreJurisprudenciaService")
public class ObservacaoLivreJurisprudenciaServiceImpl extends GenericServiceImpl<ObservacaoLivreJurisprudencia, Long, ObservacaoLivreJurisprudenciaDao> implements
		ObservacaoLivreJurisprudenciaService {

	protected ObservacaoLivreJurisprudenciaServiceImpl(ObservacaoLivreJurisprudenciaDao dao) {
		super(dao);
	}

	@Override
	public ObservacaoLivreJurisprudencia recuperar(IncidenteAnalise incidenteAnalise,
			TipoOrdenacaoObservacaoJurisprudencia tipoOrdenacao) throws ServiceException {
		try {
			return dao.recuperar(incidenteAnalise, tipoOrdenacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

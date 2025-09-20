/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.ObservacaoLivreJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.TipoOrdenacaoObservacaoJurisprudencia;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.ObservacaoLivreJurisprudenciaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 08.10.2012
 */
public interface ObservacaoLivreJurisprudenciaService extends GenericService<ObservacaoLivreJurisprudencia, Long, ObservacaoLivreJurisprudenciaDao> {

	ObservacaoLivreJurisprudencia recuperar(IncidenteAnalise incidenteAnalise,
			TipoOrdenacaoObservacaoJurisprudencia tipoOrdenacao) throws ServiceException;

}

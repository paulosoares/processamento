/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.ObservacaoLivreJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.TipoOrdenacaoObservacaoJurisprudencia;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 08.10.2012
 */
public interface ObservacaoLivreJurisprudenciaDao extends GenericDao<ObservacaoLivreJurisprudencia, Long> {

	ObservacaoLivreJurisprudencia recuperar(IncidenteAnalise incidenteAnalise,
			TipoOrdenacaoObservacaoJurisprudencia tipoOrdenacao) throws DaoException;

}

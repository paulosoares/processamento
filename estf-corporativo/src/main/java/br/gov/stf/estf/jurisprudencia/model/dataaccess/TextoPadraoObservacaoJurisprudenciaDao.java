/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.TextoPadraoObservacaoJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.TipoOrdenacaoObservacaoJurisprudencia;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 08.10.2012
 */
public interface TextoPadraoObservacaoJurisprudenciaDao extends GenericDao<TextoPadraoObservacaoJurisprudencia, Long> {

	List<TextoPadraoObservacaoJurisprudencia> pesquisar(TipoOrdenacaoObservacaoJurisprudencia tipoOrdenacao) throws DaoException;

}

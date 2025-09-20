/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.TipoEscopoLegislacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 11.08.2012
 */
public interface TipoEscopoLegislacaoDao extends GenericDao<TipoEscopoLegislacao, Long> {

	List<TipoEscopoLegislacao> pesquisarTodos() throws DaoException;

	List<TipoEscopoLegislacao> pesquisarTiposEscopoLegislacao(String sugestao) throws DaoException;

}

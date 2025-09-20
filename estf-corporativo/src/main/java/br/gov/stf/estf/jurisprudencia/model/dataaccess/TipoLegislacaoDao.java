/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.TipoLegislacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 11.08.2012
 */
public interface TipoLegislacaoDao extends GenericDao<TipoLegislacao, Long> {

	List<TipoLegislacao> pesquisarTiposLegislacaoPrincipais(String sugestao) throws DaoException;

}

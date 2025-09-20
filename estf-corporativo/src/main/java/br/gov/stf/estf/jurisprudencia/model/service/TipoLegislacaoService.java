/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.TipoLegislacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.TipoLegislacaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 11.08.2012
 */
public interface TipoLegislacaoService extends GenericService<TipoLegislacao, Long, TipoLegislacaoDao> {

	List<TipoLegislacao> pesquisarTiposLegislacaoPrincipais(String sugestao) throws ServiceException;

}

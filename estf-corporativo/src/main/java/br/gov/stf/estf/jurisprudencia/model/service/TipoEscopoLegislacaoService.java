/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.TipoEscopoLegislacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.TipoEscopoLegislacaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 11.08.2012
 */
public interface TipoEscopoLegislacaoService extends GenericService<TipoEscopoLegislacao, Long, TipoEscopoLegislacaoDao> {

	List<TipoEscopoLegislacao> pesquisarTodos() throws ServiceException;

	List<TipoEscopoLegislacao> pesquisarTiposEscopoLegislacao(String suggest) throws ServiceException;
}

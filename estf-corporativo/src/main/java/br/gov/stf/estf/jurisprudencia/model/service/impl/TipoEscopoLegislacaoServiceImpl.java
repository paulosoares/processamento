/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.TipoEscopoLegislacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.TipoEscopoLegislacaoDao;
import br.gov.stf.estf.jurisprudencia.model.service.TipoEscopoLegislacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 11.08.2012
 */
@Service("tipoEscopoLegislacaoService")
public class TipoEscopoLegislacaoServiceImpl extends
		GenericServiceImpl<TipoEscopoLegislacao, Long, TipoEscopoLegislacaoDao> implements
		TipoEscopoLegislacaoService {

	protected TipoEscopoLegislacaoServiceImpl(TipoEscopoLegislacaoDao dao) {
		super(dao);
	}

	@Override
	public List<TipoEscopoLegislacao> pesquisarTodos() throws ServiceException {
		try {
			return dao.pesquisarTodos();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<TipoEscopoLegislacao> pesquisarTiposEscopoLegislacao(String sugestao) throws ServiceException {
		try {
			return dao.pesquisarTiposEscopoLegislacao(sugestao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
}

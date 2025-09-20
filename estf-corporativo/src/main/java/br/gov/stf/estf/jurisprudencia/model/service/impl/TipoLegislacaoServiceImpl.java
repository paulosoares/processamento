/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.TipoLegislacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.TipoLegislacaoDao;
import br.gov.stf.estf.jurisprudencia.model.service.TipoLegislacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 11.08.2012
 */
@Service("tipoLegislacaoService")
public class TipoLegislacaoServiceImpl extends GenericServiceImpl<TipoLegislacao, Long, TipoLegislacaoDao> implements
		TipoLegislacaoService {

	protected TipoLegislacaoServiceImpl(TipoLegislacaoDao dao) {
		super(dao);
	}

	@Override
	public List<TipoLegislacao> pesquisarTiposLegislacaoPrincipais(String sugestao) throws ServiceException {
		try {
			return dao.pesquisarTiposLegislacaoPrincipais(sugestao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

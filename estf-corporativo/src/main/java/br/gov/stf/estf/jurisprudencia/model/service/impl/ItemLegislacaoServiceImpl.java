/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.ItemLegislacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.ItemLegislacaoDao;
import br.gov.stf.estf.jurisprudencia.model.service.ItemLegislacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 12.08.2012
 */
@Service("itemLegislacaoService")
public class ItemLegislacaoServiceImpl extends GenericServiceImpl<ItemLegislacao, Long, ItemLegislacaoDao> implements
		ItemLegislacaoService {

	protected ItemLegislacaoServiceImpl(ItemLegislacaoDao dao) {
		super(dao);
	}
	
	@Override
	protected void preExclusao(ItemLegislacao entity) throws ServiceException {
		excluirTodos(recuperarFilhos(entity));
	}

	private Collection<ItemLegislacao> recuperarFilhos(ItemLegislacao entity) throws ServiceException {
		try {
			return dao.recuperarFilhos(entity);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
}

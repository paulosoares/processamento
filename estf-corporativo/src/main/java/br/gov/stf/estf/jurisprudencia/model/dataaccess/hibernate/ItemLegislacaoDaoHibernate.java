/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.ItemLegislacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.ItemLegislacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 12.08.2012
 */
@Repository
public class ItemLegislacaoDaoHibernate extends GenericHibernateDao<ItemLegislacao, Long> implements ItemLegislacaoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8292102787467095058L;

	public ItemLegislacaoDaoHibernate() {
		super(ItemLegislacao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemLegislacao> recuperarFilhos(ItemLegislacao entity) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ItemLegislacao.class);
			
			c.add(Restrictions.eq("itemLegislacaoPai", entity));
			
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}

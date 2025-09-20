/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.ItemControlePublicacaoDj;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.ItemControlePublicacaoDjDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 16.08.2012
 */
@Repository
public class ItemControlePublicacaoDjDaoHibernate extends GenericHibernateDao<ItemControlePublicacaoDj, Long> implements
		ItemControlePublicacaoDjDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1539586412345249229L;

	public ItemControlePublicacaoDjDaoHibernate() {
		super(ItemControlePublicacaoDj.class);
	}

}

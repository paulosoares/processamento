/**
 * 
 */
package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.PautaPlenario;
import br.gov.stf.estf.entidade.processostf.TemaPauta;
import br.gov.stf.estf.processostf.model.dataaccess.TemaPautaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 08.08.2011
 */
@Repository
public class TemaPautaDaoHibernate extends GenericHibernateDao<TemaPauta, Long> implements
		TemaPautaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -640311085421117132L;

	public TemaPautaDaoHibernate() {
		super(TemaPauta.class);
	}

	/* (non-Javadoc)
	 * @see br.gov.stf.estf.processostf.model.dataaccess.TemaPautaDao#pesquisarTemaPauta(br.gov.stf.estf.entidade.processostf.PautaPlenario)
	 */
	@Override
	public List<TemaPauta> pesquisarTemaPauta(PautaPlenario pautaPlenario)
			throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TemaPauta.class);
			c.add(Restrictions.eq("pautaPlenario.id", pautaPlenario.getId()));
			c.addOrder(Order.asc("ordem"));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}

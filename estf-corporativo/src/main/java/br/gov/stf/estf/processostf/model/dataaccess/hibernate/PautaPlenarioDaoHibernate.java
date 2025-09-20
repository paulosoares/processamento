/**
 * 
 */
package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.PautaPlenario;
import br.gov.stf.estf.processostf.model.dataaccess.PautaPlenarioDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 08.08.2011
 */
@Repository
public class PautaPlenarioDaoHibernate extends GenericHibernateDao<PautaPlenario, Long>
		implements PautaPlenarioDao {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8919239972407145705L;

	public PautaPlenarioDaoHibernate() {
		super(PautaPlenario.class);
	}

	/* (non-Javadoc)
	 * @see br.gov.stf.estf.processostf.model.dataaccess.PautaPlenarioDao#recuperarTodos()
	 */
	@Override
	public List<PautaPlenario> recuperarTodos() throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PautaPlenario.class);
			c.addOrder(Order.asc("numero"));
			return c.list();
		} catch(Exception e) {
			throw new DaoException(e);
		}
	}

}

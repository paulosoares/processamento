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

import br.gov.stf.estf.entidade.processostf.SubtemaPauta;
import br.gov.stf.estf.entidade.processostf.TemaPauta;
import br.gov.stf.estf.processostf.model.dataaccess.SubtemaPautaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 08.08.2011
 */
@Repository
public class SubtemaPautaDaoHibernate extends GenericHibernateDao<SubtemaPauta, Long>
		implements SubtemaPautaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 578551939374260000L;

	public SubtemaPautaDaoHibernate() {
		super(SubtemaPauta.class);
	}

	/* (non-Javadoc)
	 * @see br.gov.stf.estf.processostf.model.dataaccess.SubtemaPautaDao#pesquisar(br.gov.stf.estf.entidade.processostf.TemaPauta)
	 */
	@Override
	public List<SubtemaPauta> pesquisar(TemaPauta temaPauta)
			throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(SubtemaPauta.class);
			c.add(Restrictions.eq("temaPauta.id", temaPauta.getId()));
			c.addOrder(Order.asc("ordem"));
			return c.list();
		} catch(Exception e) {
			throw new DaoException(e);
		}
	}

}

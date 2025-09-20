package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ComplementoParte;
import br.gov.stf.estf.entidade.processostf.ComplementoParte.ComplementoParteId;
import br.gov.stf.estf.processostf.model.dataaccess.ComplementoParteDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ComplementoParteDaoHibernate extends GenericHibernateDao<ComplementoParte, ComplementoParteId> 
	implements ComplementoParteDao {

	public ComplementoParteDaoHibernate() {
		super(ComplementoParte.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8895171125621143826L;

	@SuppressWarnings("unchecked")
	public List<ComplementoParte> pesquisar(Long codigoParte)
			throws DaoException {
		List<ComplementoParte> resp = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ComplementoParte.class);
			c.add( Restrictions.eq("id.codigoParte", codigoParte) );
			c.addOrder( Order.asc("nome") );
			resp = c.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return resp;
	}
	

}

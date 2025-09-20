package br.gov.stf.estf.processostf.model.dataaccess.hibernate;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.SumulaIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.SumulaIncidenteDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class SumulaIncidenteDaoHibernate extends GenericHibernateDao<SumulaIncidente, Long>
	implements SumulaIncidenteDao{

	private static final long serialVersionUID = -8240824258629620304L;

	public SumulaIncidenteDaoHibernate() {
		super(SumulaIncidente.class);
	}

	public SumulaIncidente pesquisaSumulaIncidente(Long idSumula,
			Long idProcessoPrecedente) throws DaoException {
		
		SumulaIncidente si = null;
		
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(SumulaIncidente.class);
			c.add(Restrictions.eq("sumula.id", idSumula));
			c.add(Restrictions.eq("objetoIncidente.id", idProcessoPrecedente));
			si = (SumulaIncidente) c.uniqueResult();

		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return si;
	}

}

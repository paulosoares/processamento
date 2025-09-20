package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.processostf.model.dataaccess.ParteDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ParteDaoHibernate extends GenericHibernateDao<Parte, Long> implements ParteDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4207025187086497194L;

	public ParteDaoHibernate() {
		super(Parte.class);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Parte> pesquisarPartes(Long idObjetoIncidente) throws DaoException {
		Session session = retrieveSession();

		List<Parte> listaParte = null;

		try {
			Criteria criteria = session.createCriteria(Parte.class);
			criteria.add(Restrictions.eq("objetoIncidente.id", idObjetoIncidente));
			criteria.addOrder(Order.asc("numeroOrdem"));
			
			listaParte = (List<Parte>) criteria.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaParte;
	}


	public List<Parte> pesquisarPartes(Long idObjetoIncidente,
			List<Long> codigosCategoria) throws DaoException {
		
		List<Parte> listaParte = null;
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		try {
			hql.append(" SELECT p FROM Parte p ");
			hql.append(" WHERE (1=1) ");
			
			if (idObjetoIncidente != null){
				hql.append(" AND p.objetoIncidente.id = :idObjetoIncidente ");
			}
			
			if (codigosCategoria != null && codigosCategoria.size() > 0){
				hql.append(" AND p.categoria IN (:codigosCategoria) "); 
			}
			
			Query q = session.createQuery( hql.toString() );
			
			if (idObjetoIncidente != null){
				q.setLong("idObjetoIncidente", idObjetoIncidente);
			}
			
			if (codigosCategoria != null && codigosCategoria.size() > 0L){
				q.setParameterList("codigosCategoria", codigosCategoria);
			}
			
			listaParte = q.list();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return listaParte;
	}

}

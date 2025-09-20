package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.TipoControle;
import br.gov.stf.estf.processostf.model.dataaccess.TipoControleDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoControleDaoHibernate extends GenericHibernateDao<TipoControle, Long> implements TipoControleDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8356471719357008795L;

	public TipoControleDaoHibernate() {
		super(TipoControle.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoControle> pesquisarTodosControles() throws DaoException {
		
		List<TipoControle> listaControle = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoControle.class);
			c.addOrder(Order.asc("dataAtualizacao"));

			listaControle = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaControle;
	}

	@Override
	public TipoControle recuperarPorSigla(String sigla) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoControle.class);
			c.add(Restrictions.eq("sigla", sigla));
			
			return (TipoControle) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}

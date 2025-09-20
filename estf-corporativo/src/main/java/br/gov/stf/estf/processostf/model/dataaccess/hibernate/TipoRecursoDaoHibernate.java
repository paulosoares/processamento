package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.TipoRecurso;
import br.gov.stf.estf.processostf.model.dataaccess.TipoRecursoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoRecursoDaoHibernate extends GenericHibernateDao<TipoRecurso, Long> implements TipoRecursoDao {
	public TipoRecursoDaoHibernate() {
		super(TipoRecurso.class);
	}

	private static final long serialVersionUID = 1L;

	public TipoRecurso recuperarTipoRecurso(Long id) throws DaoException {
		Session session = retrieveSession();
		TipoRecurso resultado = null;

		try {
			Criteria c = session.createCriteria(TipoRecurso.class);

			if (id != null)
				c.add(Restrictions.eq("id", id));

			resultado = (TipoRecurso) c.uniqueResult();

		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return resultado;
	}

	public TipoRecurso recuperarTipoRecurso(String sigla) throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(TipoRecurso.class);
		criteria.add(Restrictions.eq("sigla", sigla));
		criteria.add(Restrictions.eq("ativo",true));
		return (TipoRecurso) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoRecurso> recuperarTipoRecurso(boolean ativos)
			throws DaoException {
		Session session = retrieveSession();
		List<TipoRecurso> resultado = null;

		try {
			Criteria c = session.createCriteria(TipoRecurso.class);

			c.add(Restrictions.eq("ativo", ativos));
			c.addOrder(Order.asc("descricao"));
			resultado = c.list();

		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return resultado;
	}

}

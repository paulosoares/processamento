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

import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.entidade.processostf.AssuntoProcesso;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.dataaccess.AssuntoProcessoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AssuntoProcessoDaoHibernate extends
		GenericHibernateDao<AssuntoProcesso, Long> implements
		AssuntoProcessoDao {

	public AssuntoProcessoDaoHibernate() {
		super(AssuntoProcesso.class);
	}

	private static final long serialVersionUID = -6996835210272663629L;

	@SuppressWarnings("unchecked")
	public List<AssuntoProcesso> pesquisar(String siglaClasseProcessual,
			Long numeroProcesso) throws DaoException {

		List<AssuntoProcesso> resp = null;

		try {
			Session session = retrieveSession();

			Criteria c = session.createCriteria(AssuntoProcesso.class);

			c.add(Restrictions.eq("processo.id.numeroProcessual",
					numeroProcesso));
			c.add(Restrictions.eq("processo.id.siglaClasseProcessual",
					siglaClasseProcessual));
			c.addOrder(Order.asc("ordem"));

			resp = c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return resp;
	}

	public void persistirAssuntoProcesso(AssuntoProcesso assuntoProcesso)
			throws DaoException {

		Session session = retrieveSession();

		try {
			session.persist(assuntoProcesso);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	public Assunto recuperarAssuntoPrincipal(Processo processo)
			throws DaoException {
		Assunto assunto = null;
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ap.assunto FROM AssuntoProcesso ap ");
			hql.append(" WHERE ap.objetoIncidente.principal.id = :processo ");
			hql.append(" AND ap.ordem = :ordem ");

			Query query = session.createQuery(hql.toString());
			query.setParameter("processo", processo.getId());
			query.setInteger("ordem", 1);

			assunto = (Assunto) query.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return assunto;

	}

	@SuppressWarnings("unchecked")
	public List<Assunto> recuperarListaAssuntosDoProcesso(Processo processo)
			throws DaoException {
		List<Assunto> listaAssunto = null;
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();

			hql.append(" SELECT ap.assunto FROM AssuntoProcesso ap ");
			hql.append(" WHERE ap.objetoIncidente.principal.id = :processo ");

			Query query = session.createQuery(hql.toString());
			query.setParameter("processo", processo.getId());

			listaAssunto = query.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return listaAssunto;

	}
}

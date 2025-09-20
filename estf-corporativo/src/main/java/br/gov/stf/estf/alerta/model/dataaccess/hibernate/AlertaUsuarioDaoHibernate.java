package br.gov.stf.estf.alerta.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.alerta.model.dataaccess.AlertaUsuarioDao;
import br.gov.stf.estf.entidade.alerta.AlertaUsuario;
import br.gov.stf.estf.entidade.alerta.TipoFiltroAlerta;
import br.gov.stf.estf.entidade.alerta.ValorFiltroAlerta;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AlertaUsuarioDaoHibernate extends GenericHibernateDao<AlertaUsuario, Long> implements AlertaUsuarioDao {

	private static final long serialVersionUID = 1L;

	public AlertaUsuarioDaoHibernate() {
		super(AlertaUsuario.class);
	}

	public Boolean persistirAlertaUsuario(AlertaUsuario alertaUsuario) throws DaoException {
		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist(alertaUsuario);
			session.flush();

			alterado = Boolean.TRUE;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return alterado;
	}

	public Boolean excluirAlertaUsuario(AlertaUsuario alertaUsuario) throws DaoException {
		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {
			session.delete(alertaUsuario);
			session.flush();
			alterado = Boolean.TRUE;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return alterado;
	}

	@SuppressWarnings("unchecked")
	public List<AlertaUsuario> pesquisarAlertaUsuario(Andamento andamento, Usuario usuario, Boolean notificarPorEmail,
			Boolean semValorFiltroAlerta, Long idSetor) throws DaoException {

		Session session = retrieveSession();
		List<AlertaUsuario> listaAlertaUsuario = null;

		try {

			StringBuffer hql = new StringBuffer("SELECT au FROM AlertaUsuario au WHERE 1=1 ");

			if (usuario != null) {
				hql.append(" AND au.usuario.id = '" + usuario.getId() + "'");
			}

			if (notificarPorEmail != null) {
				if (notificarPorEmail.equals(Boolean.TRUE)) {
					hql.append(" AND au.notificarPorEmail ='S'");
				} else {
					hql.append(" AND au.notificarPorEmail ='N'");
				}
			}

			if (andamento != null) {

				if (andamento.getId() != null && andamento.getId() > 0) {
					hql.append(" AND au.tipoAndamento.id = " + andamento.getId());
				} else if (andamento.getDescricao() != null && andamento.getDescricao().trim().length() > 0) {
					hql.append(" AND au.tipoAndamento.descricao LIKE '%" + andamento.getDescricao() + "%'");
				}
			}

			if (semValorFiltroAlerta != null && semValorFiltroAlerta) {
				hql.append(" AND (au.id NOT IN (SELECT vc.alertaUsuario.id " + " FROM  ValorFiltroAlerta vc WHERE "
						+ " vc.alertaUsuario.id = au.id)) ");
			}

			if (idSetor != null) {
				hql.append(" AND au.setor.id = " + idSetor);
			}

			hql.append(" ORDER BY au.tipoAndamento.id");
			Query query = session.createQuery(hql.toString());
			listaAlertaUsuario = query.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaAlertaUsuario;
	}

	public Boolean persistirVolorFiltroAlerta(ValorFiltroAlerta valorFiltroAlerta) throws DaoException {
		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist(valorFiltroAlerta);
			session.flush();

			alterado = Boolean.TRUE;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return alterado;
	}

	public Boolean excluirVolorFiltroAlerta(ValorFiltroAlerta valorFiltroAlerta) throws DaoException {
		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {
			session.delete(valorFiltroAlerta);
			session.flush();
			alterado = Boolean.TRUE;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return alterado;
	}

	@SuppressWarnings("unchecked")
	public List<ValorFiltroAlerta> pesquisarValorFiltroAlerta(Andamento andamento, Usuario usuario,
			Boolean notificarPorEmail, Long idTipoFiltroAlerta, Long idSetor) throws DaoException {

		Session session = retrieveSession();
		List<ValorFiltroAlerta> listaAlertaUsuario = null;

		try {

			Criteria criteria = session.createCriteria(ValorFiltroAlerta.class, "au");
			criteria = criteria.createCriteria("au.alertaUsuario", "aut");
			criteria = criteria.createCriteria("aut.tipoAndamento", "ta");

			if (usuario != null) {

				criteria.add(Restrictions.eq("aut.usuario.id", usuario.getId()));
			}

			if (andamento != null) {

				if (andamento.getId() != null && andamento.getId() > 0) {
					criteria.add(Restrictions.eq("ta.id", andamento.getId()));
				} else if (andamento.getDescricao() != null && andamento.getDescricao().trim().length() > 0) {
					criteria.add(Restrictions.ilike("ta.descricao", andamento.getDescricao(), MatchMode.ANYWHERE));
				}
			}

			if (notificarPorEmail != null) {
				if (notificarPorEmail.equals(Boolean.TRUE)) {
					criteria.add(Restrictions.eq("aut.notificarPorEmail", "S"));
				} else {
					criteria.add(Restrictions.eq("aut.notificarPorEmail", "N"));
				}
			}

			if (andamento != null) {

				if (andamento.getId() != null) {
					criteria.add(Restrictions.eq("ta.id", andamento.getId()));
				} else if (andamento.getDescricao() != null && andamento.getDescricao().trim().length() > 0) {
					criteria.add(Restrictions.ilike("ta.descricao", andamento.getDescricao(), MatchMode.ANYWHERE));
				}
			}

			if (idSetor != null) {
				criteria.add(Restrictions.eq("aut.setor.id", idSetor));
			}

			criteria.addOrder(Order.asc("aut.id"));
			listaAlertaUsuario = criteria.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaAlertaUsuario;
	}

	@SuppressWarnings("unchecked")
	public List<TipoFiltroAlerta> pesquisarTipoFiltroAlerta(Long id, String descricao) throws DaoException {
		Session session = retrieveSession();
		List<TipoFiltroAlerta> listaTipoFiltroAlerta = null;
		try {

			Criteria criteria = session.createCriteria(TipoFiltroAlerta.class);
			if (id != null) {
				criteria.add(Restrictions.eq("id", id));
			}

			if (descricao != null && descricao.trim().length() > 0) {
				criteria.add(Restrictions.eq("descricao", descricao));
			}
			criteria.addOrder(Order.asc("descricao"));
			listaTipoFiltroAlerta = criteria.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaTipoFiltroAlerta;
	}

}
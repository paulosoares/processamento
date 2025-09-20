package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.VinculoProcessoTema;
import br.gov.stf.estf.julgamento.model.dataaccess.VinculoProcessoTemaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class VinculoProcessoTemaDaoHibernate extends GenericHibernateDao<VinculoProcessoTema, Long> implements VinculoProcessoTemaDao {

	private static final long	serialVersionUID	= -964831743185822438L;

	public VinculoProcessoTemaDaoHibernate() {
		super(VinculoProcessoTema.class);
	}

	@SuppressWarnings("unchecked")
	public List<VinculoProcessoTema> pesquisarVinculoProcessoTema(Long idTema, Long idObjetoIncidentePrincipal, Long idTipoTema)
			throws DaoException {
		Session session = retrieveSession();

		List<VinculoProcessoTema> lista = null;

		try {

			StringBuilder hql = new StringBuilder("SELECT vpt FROM VinculoProcessoTema vpt ");
			hql.append(" WHERE 1=1  ");

			if (idObjetoIncidentePrincipal != null)
				hql.append(" AND vpt.objetoIncidente.id = :idObjetoIncidente");

			if (idTema != null)
				hql.append(" AND vpt.tema.id = :idTema");

			if (idTipoTema != null)
				hql.append(" AND vpt.tema.tipoTema.id = :idTipoTema");

			Query q = session.createQuery(hql.toString());

			if (idObjetoIncidentePrincipal != null)
				q.setLong("idObjetoIncidente", idObjetoIncidentePrincipal);

			if (idTema != null)
				q.setLong("idTema", idTema);

			if (idTipoTema != null)
				q.setLong("idTipoTema", idTipoTema);

			lista = (List<VinculoProcessoTema>) q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return lista;
	}

}
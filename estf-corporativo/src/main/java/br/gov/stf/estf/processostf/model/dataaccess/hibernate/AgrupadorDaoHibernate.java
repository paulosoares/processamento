package br.gov.stf.estf.processostf.model.dataaccess.hibernate;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.processostf.model.dataaccess.AgrupadorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AgrupadorDaoHibernate extends GenericHibernateDao<Agrupador, Long>
		implements AgrupadorDao {

	private static final long serialVersionUID = -2780650110147028133L;

	public AgrupadorDaoHibernate() {
		super(Agrupador.class);
	}

	@Override
	public List<Agrupador> pesquisarPorSetor(Long idSetor) throws DaoException {
		List<Agrupador> agrupadores = null;
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			
			hql.append(" SELECT a ");
			hql.append(" FROM Agrupador a ");
			hql.append(" WHERE a.setor.id = :idSetor ");
			hql.append(" ORDER BY a.descricao ");
			
			Query q = session.createQuery(hql.toString());
			
			q.setLong("idSetor", idSetor);

			agrupadores = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return agrupadores;
	}
	
	@Override
	public List<Agrupador> pesquisarPorSetor(Long idSetor, String texto) throws DaoException {
		List<Agrupador> agrupadores = null;
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			
			hql.append(" SELECT a ");
			hql.append(" FROM Agrupador a ");
			hql.append(" WHERE a.setor.id = :idSetor ");
			hql.append(" AND lower(a.descricao) like :texto");
			
			Query q = session.createQuery(hql.toString());
			
			q.setLong("idSetor", idSetor);
			q.setString("texto", "%"+texto.toLowerCase()+"%");

			agrupadores = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return agrupadores;
	}
	
	public void inserirObjetoIncidenteNoAgrupador(Long idObjetoIncidente, Long idCategoria) throws DaoException {
		try {
			Session session = retrieveSession();
			
			StringBuffer queryString = new StringBuffer();
			
			queryString.append(" INSERT INTO JUDICIARIO.AGRUPADOR_OBJETO_INCIDENTE ");
			queryString.append(" (SEQ_AGRUPADOR_OBJETO_INCIDENTE, SEQ_AGRUPADOR, SEQ_OBJETO_INCIDENTE) ");
			queryString.append(" VALUES (JUDICIARIO.SEQ_AGRUPADOR_OBJETO_INCIDENTE.NEXTVAL, :idCategoria, :idObjetoIncidente) ");
			
			SQLQuery query = session.createSQLQuery(queryString.toString());
			query.setParameter("idCategoria", idCategoria);
			query.setParameter("idObjetoIncidente", idObjetoIncidente);
			query.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	public void removerObjetoIncidenteDoAgrupador(Long idObjetoIncidente, Long idCategoria) throws DaoException {
		try {
			Session session = retrieveSession();
			
			StringBuffer queryString = new StringBuffer();
			
			queryString.append(" DELETE FROM JUDICIARIO.AGRUPADOR_OBJETO_INCIDENTE ");
			queryString.append(" WHERE ((SEQ_AGRUPADOR=:idCategoria) AND (SEQ_OBJETO_INCIDENTE=:idObjetoIncidente)) ");
			
			SQLQuery query = session.createSQLQuery(queryString.toString());
			query.setParameter("idCategoria", idCategoria);
			query.setParameter("idObjetoIncidente", idObjetoIncidente);
			query.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Agrupador> recuperarCategoriasDoObjetoIncidente(Long idObjetoIncidente) throws DaoException {
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			
			hql.append(" SELECT a ");
			hql.append(" FROM Agrupador a ");
			hql.append(" LEFT OUTER JOIN a.objetosIncidentes as oi");
			hql.append(" WHERE (oi.id=:idObjetoIncidente) ");
			
			Query q = session.createQuery(hql.toString());
			
			q.setLong("idObjetoIncidente", idObjetoIncidente);

			return q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Agrupador> recuperarCategoriasDoObjetoIncidenteNoSetor(Long idObjetoIncidente, Long idSetor) throws DaoException {
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			
			hql.append(" SELECT a ");
			hql.append(" FROM Agrupador a ");
			hql.append(" LEFT OUTER JOIN a.objetosIncidentes as oi");
			hql.append(" WHERE (oi.id=:idObjetoIncidente) AND (a.setor.id=:idSetor)");
			
			Query q = session.createQuery(hql.toString());
			
			q.setLong("idObjetoIncidente", idObjetoIncidente);
			q.setLong("idSetor", idSetor);

			return q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public String getCategoriaDoIncidente(Long idObjetoIncidente, Long idSetor)
			throws DaoException {
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			
			hql.append(" SELECT a.descricao ");
			hql.append(" FROM Agrupador a ");
			hql.append(" JOIN a.objetosIncidentes as oi");
			hql.append(" WHERE (oi.id=:idObjetoIncidente) and (a.setor.id = :idSetor )");
			
			Query q = session.createQuery(hql.toString());
			
			q.setLong("idObjetoIncidente", idObjetoIncidente);
			q.setLong("idSetor", idSetor);
			
			String retorno = (String) q.uniqueResult();
			if (retorno == null)
				return new String("");
			return retorno;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}

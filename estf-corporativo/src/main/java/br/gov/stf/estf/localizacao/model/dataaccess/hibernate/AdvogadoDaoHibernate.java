package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Advogado;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.localizacao.model.dataaccess.AdvogadoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AdvogadoDaoHibernate extends GenericHibernateDao<Advogado, Long> 
	implements AdvogadoDao{

	private static final long serialVersionUID = 8027832714681147773L;
	
	public AdvogadoDaoHibernate () {
		super(Advogado.class);
	}

	public Advogado recuperar(Parte parte) throws DaoException {
		Advogado advogado = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Advogado.class);
			c.add( Restrictions.eq("parte", parte) );
			
			advogado = (Advogado) c.uniqueResult();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return advogado;
	}
	
	
	/**
	 * @see br.gov.stf.estf.processostf.model.dataaccess.IDaoAdvogado#persistirAdvogado(br.gov.stf.estf.processostf.modelo.Advogado)
	 */
	public void incluirAdvogado(Advogado advogado) throws DaoException {
		Session session = retrieveSession();

		try {
			Criteria criteria = session.createCriteria(Advogado.class);
			criteria.setProjection(Projections.max("id"));
			Long maxId = (Long) criteria.uniqueResult();

			advogado.setId(maxId + 1);

			session.save(advogado);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils
					.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}


	/**
	 * @see br.gov.stf.estf.processostf.model.dataaccess.IDaoAdvogado#pesquisarAdvogado(br.gov.stf.estf.processostf.modelo.Advogado)
	 */
	public List<Advogado> pesquisarAdvogado(Advogado advogado) throws DaoException {
		Session session = retrieveSession();

		try {
			Example example = Example.create(advogado).ignoreCase();

			List<Advogado> resultado = session.createCriteria(Advogado.class).add(example).list();

			if (resultado != null) {
				return resultado;
			} else {
				return new ArrayList<Advogado>();
			}
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils
					.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}


	/**
	 * @see br.gov.stf.estf.processostf.model.dataaccess.IDaoAdvogado#validarNumeroOab(java.lang.String)
	 */
	public String validarNumeroOab(String numeroOab) throws DaoException {
		try {
			Session session = retrieveSession();
			Connection con = session.connection();

			String sql = "{? = call CORP.FNC_FORMATA_OAB(?)}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, java.sql.Types.VARCHAR);
			cs.setString(2, numeroOab);
			cs.execute();
			String retorno = cs.getString(1);
			cs.close();
			return retorno;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException("Erro ao executar a função de formatação da OAB: CORP.FNC_FORMATA_OAB("
					+ numeroOab + ")");
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils
					.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}
	
	public List<Advogado> recuperarAdvogadoPorIdOuDescricao(String id) throws DaoException {

		List<Advogado> advogados = null;

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT a FROM Advogado a ");
			hql.append("WHERE a.id LIKE :id OR a.nome LIKE :idUpperCase ");
			
			Query q = session.createQuery(hql.toString());
			q.setParameter("id", "%" + id + "%");
			q.setParameter("idUpperCase", "%" + id.toUpperCase() + "%");
			
/*			SQLQuery query = session.createSQLQuery(
					"SELECT * " +
					"  FROM stf.advogados " +
					" WHERE CONTAINS (nom_adv, :criterio) > 0 AND ROWNUM <= 100").addEntity(Advogado.class);
			query.setString("criterio",id.toString());
*/

//			SQLQuery query = session.createSQLQuery(
//					"SELECT * " +
//					"  FROM stf.advogados " +
//					" WHERE CONTAINS (nom_adv, '" + id.toString() + "') > 0 AND ROWNUM <= 100").addEntity(Advogado.class);
			
			advogados = q.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return advogados;
	}

	@Override
	public List<Advogado> recuperarAdvogadoPorId(Long id) throws DaoException {
		List<Advogado> advogados = null;

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT a FROM Advogado a ");
			hql.append("WHERE a.id LIKE '%" + id + "' OR a.id LIKE '" + id + "%' ");
			hql.append(" ORDER BY a.id ASC ");
			
			Query q = session.createQuery(hql.toString());
			
			advogados = q.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return advogados;
	}

	@Override
	public List<Advogado> recuperarAdvogadoPorDescricao(String id)
			throws DaoException {
		List<Advogado> advogados = null;

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT a FROM Advogado a ");
			hql.append("WHERE upper(a.nome) LIKE upper('%"+id+"%') ");
			hql.append(" ORDER BY a.nome ASC");
			
			Query q = session.createQuery(hql.toString());
			
			advogados = q.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return advogados;
	}

	

}

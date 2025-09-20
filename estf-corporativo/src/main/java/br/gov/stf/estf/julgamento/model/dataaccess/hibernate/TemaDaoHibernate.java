package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.entidade.julgamento.Tema;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia;
import br.gov.stf.estf.entidade.julgamento.TipoTema;
import br.gov.stf.estf.julgamento.model.dataaccess.TemaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TemaDaoHibernate extends GenericHibernateDao<Tema, Long> implements TemaDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4179757243583874133L;



	public TemaDaoHibernate() {
    	super(Tema.class);
    }

	

	public Tema recuperarTema(Long id) throws DaoException {
		Session session = retrieveSession();

		try {

			Criteria criteria = session.createCriteria(Tema.class);

			if( id != null) 
				criteria.add(Restrictions.eq("id", id));

			return (Tema) criteria.uniqueResult();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

	}

	@SuppressWarnings("unchecked")
	public List<TipoOcorrencia> pesquisarTipoOcorrencia(Long codigo,
			String descricao) throws DaoException {
		Session session = retrieveSession();

		List<TipoOcorrencia> lista = null;

		try {
			Criteria criteria = session.createCriteria(TipoOcorrencia.class);
			
			if(codigo != null) 
				criteria.add(Restrictions.eq("id", codigo));
			
			if(descricao != null && descricao.trim().length() > 0) 
				criteria.add(Restrictions.eq("descricao", descricao));
			
			
			lista = (List<TipoOcorrencia>) criteria.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<TipoTema> pesquisarTipoTema(Long codigo, String descricao)
			throws DaoException {
		Session session = retrieveSession();

		List<TipoTema> lista = null;

		try {
			Criteria criteria = session.createCriteria(TipoTema.class);
			
			if(codigo != null) 
				criteria.add(Restrictions.eq("id", codigo));
			
			if(descricao != null && descricao.trim().length() > 0) 
				criteria.add(Restrictions.eq("descricao", descricao));
			
			lista = (List<TipoTema>) criteria.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return lista;
	}
	
	public Boolean persistirTema(Tema tema) throws DaoException {
		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;
		try {
			session.persist(tema);
			session.flush();

			alterado = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return alterado;
	}



	public Boolean excluirTema(Tema tema) throws DaoException {
		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;
		try {
			session.delete(tema);
			session.flush();

			alterado = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return alterado;
	}



	public Boolean excluirProcessoTema(ProcessoTema processoTema)
			throws DaoException {
		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;
		try {
			session.delete(processoTema);
			session.flush();

			alterado = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return alterado;
	}



	@Override
	public Tema recuperarTemas(Long idObjetoIncidente) throws DaoException {
		Tema tema = new Tema();
		Session session = retrieveSession();
		try {
			
			StringBuffer hql = new StringBuffer();			
			hql.append(" SELECT t FROM Tema t , " +
						" ProcessoTema pt, ObjetoIncidente oi ");
			hql.append(" WHERE 1=1 ");
			hql.append(" AND oi.id = pt.objetoIncidente.id ");
			hql.append(" AND pt.tema.id = t.id ");
			hql.append(" AND pt.tipoOcorrencia.id = 1 ");

			if( idObjetoIncidente != null && idObjetoIncidente > 0L){
				hql.append(" AND oi.principal.id = :idObjetoIncidente ");
			}
			
			Query q = session.createQuery(hql.toString());
			
			if( idObjetoIncidente != null && idObjetoIncidente > 0L){
				q.setLong("idObjetoIncidente", idObjetoIncidente);
			}
			
			tema = (Tema) q.uniqueResult();			

		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return tema;
	}



	


    
}
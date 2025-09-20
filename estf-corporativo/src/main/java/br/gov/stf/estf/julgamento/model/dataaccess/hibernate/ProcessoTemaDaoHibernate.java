package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.julgamento.model.dataaccess.ProcessoTemaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ProcessoTemaDaoHibernate extends GenericHibernateDao<ProcessoTema, Long> implements ProcessoTemaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProcessoTemaDaoHibernate() {
    	super(ProcessoTema.class);
    }

	

	@SuppressWarnings("unchecked")
	public List<ProcessoTema> pesquisarProcessoTema(Long idTema,String sigClassePrecesso, Long numeroProcessual, 
			String tipoJulgamento,Long codTipoOcorrencia, Date dataOcorrencia)
			throws DaoException {
		Session session = retrieveSession();

		List<ProcessoTema> lista = null;

		try {
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT pt ");
			hql.append("   FROM ProcessoTema pt ");
			hql.append("   JOIN pt.objetoIncidente oi ");
			hql.append("   JOIN oi.principal pr, ");
			hql.append("        Processo p, ");
			hql.append("        IncidenteJulgamento ij ");
			hql.append("  WHERE pr.id = p.id ");
			hql.append("    AND ij.id = oi.id "); 
			
			
			if(idTema != null)
				hql.append(" AND pt.tema.id = :idTema");
				
			if(sigClassePrecesso != null && sigClassePrecesso.trim().length() > 0) 
				hql.append(" AND p.siglaClasseProcessual = :sigClassePrecesso");
			
			if(numeroProcessual != null && numeroProcessual > 0 ) 
				hql.append(" AND p.numeroProcessual = :numeroProcessual");
			
			if(codTipoOcorrencia != null) 
				hql.append(" AND pt.tipoOcorrencia.id = :codTipoOcorrencia");
			
			if(tipoJulgamento != null && tipoJulgamento.trim().length() > 0 )
				hql.append(" AND ij.tipoJulgamento.sigla = :tipoJulgamento");
			
			if(dataOcorrencia != null) 
				hql.append(" AND pt.dataOcorrencia = :dataOcorrencia");
			
			Query q = session.createQuery(hql.toString());
			
			if(idTema != null)
				q.setLong("idTema", idTema);
				
			if(sigClassePrecesso != null && sigClassePrecesso.trim().length() > 0)
				q.setString("sigClassePrecesso", sigClassePrecesso);
			
			if(numeroProcessual != null && numeroProcessual > 0 )
				q.setLong("numeroProcessual", numeroProcessual);
			
			if(codTipoOcorrencia != null) 
				q.setLong("codTipoOcorrencia", codTipoOcorrencia);
			
			if(tipoJulgamento != null && tipoJulgamento.trim().length() > 0 )
				q.setString("tipoJulgamento", tipoJulgamento);
			
			if(dataOcorrencia != null) 
				q.setDate("dataOcorrencia", dataOcorrencia);
			
			lista = (List<ProcessoTema>) q.list();
			
		}catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return lista;
	}



	@SuppressWarnings("unchecked")
	public List<ProcessoTema> pesquisarProcessoTema(Long idTema,Long idIncidenteJulgamento,
			Long idObjetoIncidentePrincipal,String siglaTipoRecurso) 
			throws DaoException {
		Session session = retrieveSession();

		List<ProcessoTema> lista = null;

		try {
			
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT pt FROM ProcessoTema pt");
			if( (siglaTipoRecurso != null && siglaTipoRecurso.trim().length() > 0)  || 
					idIncidenteJulgamento != null){
				hql.append(" JOIN pt.objetoIncidente oi" +
						   " JOIN oi.principal pr," +
						   "      IncidenteJulgamento ij" +
						   " WHERE ij.id = oi.id");
			}else{
				hql.append(" WHERE (1=1)");
			}
			
			
			if( idTema != null )
				hql.append(" AND pt.id = :idTema");
			
			if( idIncidenteJulgamento != null )
				hql.append(" AND ij.id = :idIncidenteJulgamento");
			
			if( idObjetoIncidentePrincipal != null )
				hql.append(" AND pr.id = :idObjetoIncidentePrincipal");
			
			if( siglaTipoRecurso != null && siglaTipoRecurso.trim().length() > 0 )
				hql.append(" AND ij.tipoJulgamento.sigla = :siglaTipoRecurso");
			
			Query q = session.createQuery(hql.toString());
			
			if( idTema != null )
				q.setLong("idTema", idTema);
			
			if( idIncidenteJulgamento != null )
				q.setLong("idIncidenteJulgamento", idIncidenteJulgamento);
			
			if( idObjetoIncidentePrincipal != null )
				q.setLong("idObjetoIncidentePrincipal", idObjetoIncidentePrincipal);
			
			if( siglaTipoRecurso != null && siglaTipoRecurso.trim().length() > 0 )
				q.setString("siglaTipoRecurso", siglaTipoRecurso);
			
			lista = (List<ProcessoTema>) q.list();
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
	
	@Override
	public List<ProcessoTema> pesquisarProcessoTemaLCase(Long numTema, String sigClassePrecesso, Long numeroProcessual, 
			String tipoJulgamento,Long codTipoOcorrencia)
			throws DaoException {
		Session session = retrieveSession();

		List<ProcessoTema> lista = null;

		try {
			
			StringBuffer hql = new StringBuffer();
					hql.append(" SELECT pt ");
					hql.append("   FROM ProcessoTema pt ");
					hql.append("   JOIN pt.objetoIncidente oi ");
					hql.append("   JOIN oi.principal pr, ");
					hql.append("        Processo p, ");
					hql.append("        IncidenteJulgamento ij ");
					hql.append("  WHERE pr.id = p.id ");
					hql.append("    AND ij.id = oi.id "); 
					//hql.append("    AND pt.tipoOcorrencia.id = 1 ");
			
			
			if(numTema != null)
				hql.append(" AND pt.tema.numeroSequenciaTema = :numTema");
				
			if(sigClassePrecesso != null && sigClassePrecesso.trim().length() > 0) 
				hql.append(" AND p.siglaClasseProcessual = :sigClassePrecesso");
			
			if(numeroProcessual != null && numeroProcessual > 0 ) 
				hql.append(" AND p.numeroProcessual = :numeroProcessual");
			
			if(codTipoOcorrencia != null) 
				hql.append(" AND pt.tipoOcorrencia.id = :codTipoOcorrencia");
			
			if(tipoJulgamento != null && tipoJulgamento.trim().length() > 0 )
				hql.append(" AND ij.tipoJulgamento.sigla = :tipoJulgamento");
		
			
			Query q = session.createQuery(hql.toString());
			
			if(numTema != null)
				q.setLong("numTema", numTema);
				
			if(sigClassePrecesso != null && sigClassePrecesso.trim().length() > 0)
				q.setString("sigClassePrecesso", sigClassePrecesso);
			
			if(numeroProcessual != null && numeroProcessual > 0 )
				q.setLong("numeroProcessual", numeroProcessual);
			
			if(codTipoOcorrencia != null) 
				q.setLong("codTipoOcorrencia", codTipoOcorrencia);
			
			if(tipoJulgamento != null && tipoJulgamento.trim().length() > 0 )
				q.setString("tipoJulgamento", tipoJulgamento);
		
			
			lista = (List<ProcessoTema>) q.list();
			
		}catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return lista;
	}	
		
	@Override
	public void removerProcessoTema(Long idObjetoIncidente, Long numeroTema) throws DaoException {
		try {
			Session session = retrieveSession();
			
			StringBuffer queryString = new StringBuffer();
			
			queryString.append(" DELETE FROM julgamento.processo_tema pt ");
			queryString.append(" where pt.seq_objeto_incidente = :idObjetoIncidente ");
			queryString.append(" and exists (select 1 from judiciario.tema t where t.cod_tipo_tema = 1 and t.seq_tema = pt.seq_tema and t.num_tema = :numeroTema) ");
			
			SQLQuery query = session.createSQLQuery(queryString.toString());
			query.setParameter("idObjetoIncidente", idObjetoIncidente);
			query.setParameter("numeroTema", numeroTema);
			query.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
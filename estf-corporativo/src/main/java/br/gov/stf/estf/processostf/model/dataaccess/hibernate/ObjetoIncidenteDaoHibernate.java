package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.awt.print.Printable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.xerces.dom3.UserDataHandler;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.event.authorization.AuthorizedEvent;
import org.springframework.security.ui.AuthenticationDetails;
import org.springframework.security.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.util.AuthorityUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.configuracao.model.dataaccess.hibernate.Util;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Perfil;
import br.gov.stf.estf.processostf.model.dataaccess.ObjetoIncidenteDao;
import br.gov.stf.estf.processostf.model.util.ConsultaObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.security.user.User;
import br.gov.stf.framework.security.user.UserHolder;

@Repository
public class ObjetoIncidenteDaoHibernate extends GenericHibernateDao<ObjetoIncidente, Long> implements ObjetoIncidenteDao {

	private static final long serialVersionUID = 545209027770773965L;

	public ObjetoIncidenteDaoHibernate() {
		super(ObjetoIncidente.class);
	}

	

	public Protocolo recuperar(Short ano, Long numero) throws DaoException {
		Protocolo protocolo = null;

		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Protocolo.class);
			
			if( ano != null && ano > 0 )
				c.add( Restrictions.eq("anoProtocolo", ano));
			
			if( numero != null && numero > 0 )
				c.add( Restrictions.eq("numeroProtocolo", numero));
			
			protocolo = (Protocolo) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return protocolo;
	}



	public ObjetoIncidente recuperar(ConsultaObjetoIncidente consulta)
			throws DaoException {
		ObjetoIncidente oi = null;
		
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT oi FROM ObjetoIncidente oi join fetch oi.principal WHERE oi.id = ( ");
			
			if ( consulta.isJoin() ) {
				hql.append( consulta.getQueryObjetoIncidente(null) );
			}
			
			hql.append("  ");
			
			Query q = consulta.createQuery(session, hql.toString());
			
			oi = (ObjetoIncidente) q.uniqueResult();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
		return oi;
		
	}
	
	public void registrarLogSistema(ObjetoIncidente objetoIncidente, String dscTransacao, String dscFuncionalidade, Long chaveTabela, String nomeTabela) throws DaoException {
		if(objetoIncidente != null && objetoIncidente.getNumeroNivelSigilo() >= 2 ) {
			try{
				Session session = retrieveSession();
				String sql = "INSERT INTO GLOBAL.LOG_SIGILOSOS  (  SEQ_LOG_SIGILOSOS, SEQ_OBJETO_INCIDENTE,DSC_TRANSACAO,DSC_FUNCIONALIDADE,SIG_SISTEMA,SIG_USUARIO,DAT_LOG, CHV_TABELA,NOM_TABELA) values ( GLOBAL.SEQ_LOG_SIGILOSOS.NEXTVAL, ?,?,?,sys_context('context_global','sig_sistema'),sys_context('context_global','sig_usuario'),sysdate,?,?) ";
				Query query = session.createSQLQuery(sql);
				query.setLong(0, objetoIncidente.getId() );
				query.setString(1, dscTransacao);
				query.setString(2, dscFuncionalidade);
				query.setLong(3, chaveTabela);
				query.setString(4, nomeTabela);
				query.executeUpdate();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void registrarLogSistema(Long idObjetoIncidente, String dscTransacao, String dscFuncionalidade, Long chaveTabela, String nomeTabela) throws DaoException {
		ObjetoIncidente<?> objetoIncidente = recuperarPorId(idObjetoIncidente);
		 registrarLogSistema(objetoIncidente, dscTransacao, dscFuncionalidade, chaveTabela,nomeTabela);
		
	}
	
	public ObjetoIncidente recuperar(Peticao peticao) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		ObjetoIncidente oi = null;
		
		try {
			
			hql.append(" SELECT p.objetoIncidenteVinculado FROM Peticao p ");
			hql.append(" WHERE 1=1 ");
			
			if( peticao != null && peticao.getId() != null )
				hql.append(" AND p.id = :id ");			

			Query q = session.createQuery(hql.toString());
			
			if( peticao != null && peticao.getId() != null )
				q.setLong("id", peticao.getId());
				
			oi = (ObjetoIncidente) q.uniqueResult();
		
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return oi;
	}

	public List<ObjetoIncidente<?>> pesquisar(Long idObjetoIncidentePrincipal, TipoObjetoIncidente... tiposPermitidos) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();

		try {		 
			hql.append(" SELECT oi FROM ObjetoIncidente oi ");

			if (idObjetoIncidentePrincipal != null) {
				hql.append(" WHERE oi.principal.id = :idObjetoIncidentePrincipal ");
			}

			if (!ArrayUtils.isEmpty(tiposPermitidos)) {
				hql.append(" AND oi.tipoObjetoIncidente IN ( :tiposPermitidos ) ");
			}

			Query q = session.createQuery(hql.toString());

			if (idObjetoIncidentePrincipal != null) {
				q.setLong("idObjetoIncidentePrincipal", idObjetoIncidentePrincipal);
			}

			if (!ArrayUtils.isEmpty(tiposPermitidos)) {
				q.setParameterList("tiposPermitidos", converterParaArraySiglas(tiposPermitidos));
			}

			return q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}


	@SuppressWarnings("unchecked")
	public List<ObjetoIncidente<?>> pesquisar(Long idObjetoIncidentePrincipal)
			throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		
		try {
			
			 hql.append(" select oi from ObjetoIncidente oi");
			
			if( idObjetoIncidentePrincipal != null )
				hql.append(" where oi.principal.id = :idObjetoIncidentePrincipal");	

			Query q = session.createQuery(hql.toString());
			
			if( idObjetoIncidentePrincipal != null )
				q.setLong("idObjetoIncidentePrincipal", idObjetoIncidentePrincipal);
				
			return q.list();
		
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	private String[] converterParaArraySiglas(TipoObjetoIncidente[] tipos) {
		String[] tiposStringArray = new String[tipos.length];
		
		for (int i = 0; i < tiposStringArray.length; i++) {
			tiposStringArray[i] = tipos[i].getCodigo();
		}
		
		return tiposStringArray;
	}

	@Override
	public boolean isIncidenteJulgado(Long idObjetoIncidente) throws DaoException {
		try {
			Session session = retrieveSession();
			
			StringBuffer sql = new StringBuffer();

			sql.append(" SELECT 1 FROM JUDICIARIO.OBJETO_INCIDENTE oi ");
			sql.append(" WHERE oi.SEQ_OBJETO_INCIDENTE = :idObjetoIncidente ");
			sql.append(" AND ");
			sql.append(SubQuerySituacaoJulgamento.buildClausulaSituacaoJulgado("oi"));

			SQLQuery query = session.createSQLQuery(sql.toString());
			query.setParameter("idObjetoIncidente", idObjetoIncidente);

			return query.list().size() > 0;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public List<ObjetoIncidente<?>> pesquisarListaImportacaoUsuario(String usuario) throws DaoException {
		Session session = retrieveSession();
		StringBuffer sql = new StringBuffer();
		List<ObjetoIncidente<?>> lista = new ArrayList<ObjetoIncidente<?>>();
		try {
			if(usuario != null && !usuario.trim().isEmpty()){

				sql.append(" SELECT uip.seq_objeto_incidente ");
				sql.append("   FROM egab.usuario_incidente_pesquisa uip  ");
				sql.append("  WHERE uip.sig_usuario_pesquisa = :usuario ");
				
				SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());
				sqlQuery.setString("usuario", usuario);
				
				List<Object> listaResult = sqlQuery.list();
				List<Long> listaIds = new ArrayList<Long>();
				for (Object registro : listaResult) {					
					listaIds.add(((BigDecimal) registro).longValue());
				}	
				
				if(!listaIds.isEmpty()){
					sql = new StringBuffer();
					sql.append(" SELECT oi FROM ObjetoIncidente oi ");
					sql.append(" WHERE oi.id IN ( :idsObjInc ) ");
					Query q = session.createQuery(sql.toString());	
					
					q.setParameterList("idsObjInc", listaIds.toArray());
					
					lista = q.list();
				}

				return lista;
			}		
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return null;
	}	
	
	private Long[] converterParaArrayLong(List<Long> lista) {
		Long[] longsToArray = new Long[lista.size()];
		
		int i=0;
		for(Long x : lista){
			longsToArray[i] = x;
			i++;
		}
		
		return longsToArray;
	}	
}

package br.gov.stf.estf.processostf.model.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class ConsultaObjetoIncidente {
	private String siglaClasse;
	private Long numeroProcesso;
	private Long tipoRecurso;
	private Long tipoJulgamento;
	
	private Long objetoIncidente;
	
	private Map<String, Object> parametros;
	
	private static final String PARAMETRO_SIGLA_CLASSE = "siglaClasse";
	private static final String PARAMETRO_NUMERO_PROCESSO = "numeroProcesso";
	private static final String PARAMETRO_TIPO_RECURSO = "tipoRecurso";
	private static final String PARAMETRO_TIPO_JULGAMENTO = "tipoJulgamento";
	private static final String PARAMETRO_OBJETO_INCIDENTE = "objetoIncidente";
	private static final String PARAMETRO_OBJETO_INCIDENTE1 = "objetoIncidente1";
	private static final String PARAMETRO_OBJETO_INCIDENTE2 = "objetoIncidente2";
	private static final String PARAMETRO_OBJETO_INCIDENTE3 = "objetoIncidente3";
	private static final String PARAMETRO_OBJETO_INCIDENTE4 = "objetoIncidente4";
	
	private boolean pr = true;
	private boolean rc = true;
	private boolean ij = true;
	private boolean oi = false;
	
	public ConsultaObjetoIncidente(String siglaClasse, Long numeroProcesso,
			Long tipoRecurso, Long tipoJulgamento) {
		super();
		this.siglaClasse = siglaClasse;
		this.numeroProcesso = numeroProcesso;
		this.tipoRecurso = tipoRecurso;
		this.tipoJulgamento = tipoJulgamento;
		this.parametros = new HashMap<String, Object>();
		
		if ( this.siglaClasse!=null && this.siglaClasse.trim().length()==0 ) {
			this.siglaClasse = null;
		}
		if ( this.numeroProcesso!=null && this.numeroProcesso==0 ) {
			this.numeroProcesso = null;
		}
		if ( this.tipoRecurso!=null && this.tipoRecurso==0 ) {
			this.tipoRecurso = null;
		}
		if ( this.tipoJulgamento!=null && this.tipoJulgamento==0 ) {
			this.tipoJulgamento = null;
		}
		
		if ( this.siglaClasse==null && this.numeroProcesso==null ) {
			this.pr = false;
		}
		
		if ( this.tipoRecurso==null ) {
			this.rc = false;
		}
		
		if ( this.tipoJulgamento==null ) {
			this.ij = false;
		}
		
	}
	
	public ConsultaObjetoIncidente(Long objetoIncidente) {
		super();
		this.objetoIncidente = objetoIncidente;
		this.parametros = new HashMap<String, Object>();
		
		if ( this.objetoIncidente!=null && this.objetoIncidente==0 ) {
			this.objetoIncidente = null;
		}
		
		if ( this.objetoIncidente!=null ) {
			this.oi = true;
		}
		this.pr = false;
		this.rc = false;
		this.ij = false;
	}
	
	public static void main(String[] args) {
		System.out.println( new ConsultaObjetoIncidente("AI", 178786L, 4L, 51L).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente("ADI", 123456L, 12L, null).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente("ADI", 123456L, null, 9L).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente("ADI", 123456L, null, null).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente("ADI", null, 12L, 9L).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente("ADI", null, 12L, null).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente("ADI", null, null, 9L).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente("ADI", null, null, null).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente(null, 123456L, 12L, 9L).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente(null, 123456L, 12L, null).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente(null, 123456L, null, 9L).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente(null, 123456L, null, null).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente(null, null, 12L, 9L).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente(null, null, 12L, null).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente(null, null, null, 9L).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente(null, null, null, null).getQueryObjetoIncidente("objetoIncidente") );
		System.out.println( new ConsultaObjetoIncidente(3652715L).getQueryObjetoIncidente("objetoIncidente") );
	}
	
//	public List<?> list (Query q) {
//		List<?> resp = null;		
//		setParameters(q);		
//		resp = q.list();
//		return resp;
//	}
//	
//	public Object uniqueResult (Query q) {
//		Object resp = null;		
//		setParameters(q);		
//		resp = q.uniqueResult();
//		return resp;
//	}
	
	public Query createQuery(Session session, String hql) throws HibernateException, SQLException {
		Query q = session.createQuery( hql );
		if ( oi ) {
			montarParamentrosObjetoIncidente(session);
		}
		setParameters(q);
		return q;
	}
	
	private void montarParamentrosObjetoIncidente(Session session) throws HibernateException, SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT oi.seq_objeto_incidente ");
		sql.append(" FROM judiciario.objeto_incidente oi ");
		sql.append(" START WITH oi.seq_objeto_incidente = ? ");
		sql.append(" CONNECT BY PRIOR oi.seq_objeto_incidente = oi.seq_objeto_incidente_pai ");
		
		PreparedStatement ps = session.connection().prepareStatement( sql.toString() );
		ps.setLong(1, objetoIncidente);
		Long qtd = 1L;
		
		ResultSet rs = ps.executeQuery();
		List<Long> seqs = new ArrayList<Long>();
		List<Long> seqs1 = new ArrayList<Long>();
		List<Long> seqs2 = new ArrayList<Long>();
		List<Long> seqs3 = new ArrayList<Long>();
		List<Long> seqs4 = new ArrayList<Long>();
		seqs1.add(-1L);
		seqs2.add(-1L);
		seqs3.add(-1L);
		seqs4.add(-1L);
		while ( rs.next() ) {
			seqs.add( rs.getLong("seq_objeto_incidente") );
			if(qtd > 0    && qtd < 1000) { seqs1.add( rs.getLong("seq_objeto_incidente") );}
			if(qtd > 999  && qtd < 2000) { seqs2.add( rs.getLong("seq_objeto_incidente") );}
			if(qtd > 1999 && qtd < 3000) { seqs3.add( rs.getLong("seq_objeto_incidente") );}
			if(qtd > 2999 && qtd < 4000) { seqs4.add( rs.getLong("seq_objeto_incidente") );}
			qtd++;
		}
		
		rs.close();
		ps.close();
		
		//parametros.put(PARAMETRO_OBJETO_INCIDENTE, seqs);
		parametros.put(PARAMETRO_OBJETO_INCIDENTE1, seqs1);
		parametros.put(PARAMETRO_OBJETO_INCIDENTE2, seqs2);
		parametros.put(PARAMETRO_OBJETO_INCIDENTE3, seqs3);
		parametros.put(PARAMETRO_OBJETO_INCIDENTE4, seqs4);
		
		
	}

	private void setParameters (Query q) {
		Set<String> chaves = parametros.keySet();
		if ( chaves!=null ) {
			for ( String key: chaves ) {
				if ( key.equals( PARAMETRO_OBJETO_INCIDENTE1 ) ) {
					q.setParameterList(PARAMETRO_OBJETO_INCIDENTE1, (Collection) parametros.get(key));
				} else {
					if ( key.equals( PARAMETRO_OBJETO_INCIDENTE2 ) ) {
						q.setParameterList(PARAMETRO_OBJETO_INCIDENTE2, (Collection) parametros.get(key));
					}else {
						if ( key.equals( PARAMETRO_OBJETO_INCIDENTE3 ) ) {
							q.setParameterList(PARAMETRO_OBJETO_INCIDENTE3, (Collection) parametros.get(key));
						}else{
							if ( key.equals( PARAMETRO_OBJETO_INCIDENTE4 ) ) {
								q.setParameterList(PARAMETRO_OBJETO_INCIDENTE4, (Collection) parametros.get(key));
							}else{
								q.setParameter(key, parametros.get(key));
							}
							}
						}
				}
			}
		}
	}
	
	
	public boolean isJoin() {
		boolean join = false;
		if ( siglaClasse!=null || numeroProcesso!=null || tipoRecurso!=null || tipoJulgamento!=null || objetoIncidente!=null ) {
			join = true;
		}
		return join;
	}
	
	public Long getObjetoIncidente (){
		return objetoIncidente;
	}

	public String getQueryObjetoIncidente (String aliasObjetoIncidente) {
		
		if ( ij ) {
			return getQueryIncidenteJulgamento(aliasObjetoIncidente);
		} else if ( rc ) {
			return getQueryRecursoProcesso(aliasObjetoIncidente);
		} else if ( pr ) {
			return getQueryProcesso(aliasObjetoIncidente);
		} else {
			return getQueryCadeiaObjetoIncidente(aliasObjetoIncidente);
		}
		
	}
	
	private String getQueryIncidenteJulgamento (String aliasObjetoIncidente) {
		
		StringBuffer hql = new StringBuffer();
		
		if ( aliasObjetoIncidente!=null ) {
			hql.append(" AND "+aliasObjetoIncidente+".id IN ( ");
		}
		
		hql.append(" SELECT ij.id FROM IncidenteJulgamento ij ");
		
		if ( rc ) {
			hql.append(" , RecursoProcesso rp ");
		}
		if ( pr ) {
			hql.append(" , Processo p ");
		}
		
		hql.append(" WHERE ij.tipoJulgamento.id = :"+PARAMETRO_TIPO_JULGAMENTO+" ");
		parametros.put(PARAMETRO_TIPO_JULGAMENTO, tipoJulgamento);
		
		if ( rc ) {
			hql.append(" AND ij.pai = rp ");
			hql.append(" AND rp.tipoRecursoProcesso.id = :"+PARAMETRO_TIPO_RECURSO+" ");
			parametros.put(PARAMETRO_TIPO_RECURSO, tipoRecurso);
		}
		
		if ( pr ) {
			hql.append(" AND ij.principal = p ");
			if ( siglaClasse!=null ) {
				hql.append(" AND p.classeProcessual = :"+PARAMETRO_SIGLA_CLASSE+" ");
				parametros.put(PARAMETRO_SIGLA_CLASSE, siglaClasse);
			}		
			if ( numeroProcesso!=null ) {
				hql.append(" AND p.numeroProcessual = :"+PARAMETRO_NUMERO_PROCESSO+" ");
				parametros.put(PARAMETRO_NUMERO_PROCESSO, numeroProcesso);
			}
		}
		
		hql.append(" ) ");
		
		
		
		return hql.toString();
		
	}
	
	private String getQueryRecursoProcesso (String aliasObjetoIncidente) {
		StringBuffer hql = new StringBuffer();
		
		if ( aliasObjetoIncidente!=null ) {
			hql.append(" AND "+aliasObjetoIncidente+".id IN ( ");
		}
		
		hql.append(" SELECT rp.id FROM RecursoProcesso rp ");
		
		if ( pr ) {
			hql.append(" , Processo p ");
		}
		
		hql.append(" WHERE rp.tipoRecursoProcesso.id = :"+PARAMETRO_TIPO_RECURSO+" ");
		parametros.put(PARAMETRO_TIPO_RECURSO, tipoRecurso);
		
		if ( pr ) {
			hql.append(" AND rp.principal = p ");
		}
		
		if ( siglaClasse!=null ) {
			hql.append(" AND p.classeProcessual = :"+PARAMETRO_SIGLA_CLASSE+" ");
			parametros.put(PARAMETRO_SIGLA_CLASSE, siglaClasse);
		}		
		if ( numeroProcesso!=null ) {
			hql.append(" AND p.numeroProcessual = :"+PARAMETRO_NUMERO_PROCESSO+" ");
			parametros.put(PARAMETRO_NUMERO_PROCESSO, numeroProcesso);
		}	
		
		hql.append(" ) ");
		
		
		
		return hql.toString();
	}
	
	private String getQueryProcesso (String aliasObjetoIncidente) {
		StringBuffer hql = new StringBuffer();
		
		if ( aliasObjetoIncidente!=null ) {
			hql.append(" AND "+aliasObjetoIncidente+".principal.id IN ( ");
		}
		
		hql.append(" SELECT p.id FROM Processo p ");
		hql.append(" WHERE 1=1 ");
		
		if ( siglaClasse!=null ) {
			hql.append(" AND p.classeProcessual = :"+PARAMETRO_SIGLA_CLASSE+" ");
			parametros.put(PARAMETRO_SIGLA_CLASSE, siglaClasse);
		}		
		if ( numeroProcesso!=null ) {
			hql.append(" AND p.numeroProcessual = :"+PARAMETRO_NUMERO_PROCESSO+" ");
			parametros.put(PARAMETRO_NUMERO_PROCESSO, numeroProcesso);
		}
		
		hql.append(" ) ");
		
		
		
		return hql.toString();
	}
	
	private String getQueryCadeiaObjetoIncidente(String aliasObjetoIncidente) {
		StringBuffer hql = new StringBuffer();		
		hql.append(" AND ("+aliasObjetoIncidente+".id IN ( :"+PARAMETRO_OBJETO_INCIDENTE1+"  ) ");
		hql.append(" OR   "+aliasObjetoIncidente+".id IN ( :"+PARAMETRO_OBJETO_INCIDENTE2+" ) ");
		hql.append(" OR   "+aliasObjetoIncidente+".id IN ( :"+PARAMETRO_OBJETO_INCIDENTE3+" ) ");
		hql.append(" OR   "+aliasObjetoIncidente+".id IN ( :"+PARAMETRO_OBJETO_INCIDENTE4+" ))");
		
		return hql.toString();
	}
	
	
	
	
	
	
	

	

	
	
}

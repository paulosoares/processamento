/**
 * 
 */
package br.gov.stf.estf.jurisdicionado.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.util.JurisdicionadoResult;
import br.gov.stf.estf.entidade.util.Cpf;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.JurisdicionadoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 04.07.2011
 */
@Repository
public class JurisdicionadoDaoHibernate extends GenericHibernateDao<Jurisdicionado, Long>
		implements JurisdicionadoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5134499306418006125L;

	public JurisdicionadoDaoHibernate() {
		super(Jurisdicionado.class);
	}
	
	/**
	 * Recupera os jurisdicionados relacionados aos emprestimos não devolvidos.
	 * @return
	 * @throws DaoException
	 */
	@Override
	public List<JurisdicionadoResult> recuperarJurisdicionadoDosEmprestimosNaoDevolvidos(Object value) throws DaoException {
		StringBuffer sql = new StringBuffer();
		List<Object[]> listaResult = null;
		List<JurisdicionadoResult> listaJurisdicionados = new ArrayList<JurisdicionadoResult>();
		try {
			sql.append("			SELECT DISTINCT COD_ADV, NOME_ADV || ' (' || DSC_PAPEL || ')', CPF_ADV FROM (");
			sql.append("				    SELECT DECODE(J.NOM_JURISDICIONADO, NULL, JURISMEMBRO.NOM_JURISDICIONADO, J.NOM_JURISDICIONADO) AS NOME_ADV,");
			sql.append("				           DECODE(J.NOM_JURISDICIONADO, NULL, JURISMEMBRO.SEQ_JURISDICIONADO, J.SEQ_JURISDICIONADO) AS COD_ADV,");
			sql.append("				           DECODE(J.NOM_JURISDICIONADO, NULL, CPF_MEMBRO.DSC_IDENTIFICACAO, CPF_PRINCIPAL.DSC_IDENTIFICACAO) AS CPF_ADV,");
			sql.append("				           DECODE(J.NOM_JURISDICIONADO, NULL, TIPO_MEMBRO.DSC_TIPO_JURISDICIONADO, TIPO_GRUPO.DSC_TIPO_JURISDICIONADO) AS DSC_PAPEL");

			sql.append("				    FROM JUDICIARIO.EMPRESTIMO_AUTOS_PROCESSO E,");
			sql.append("				         JUDICIARIO.PAPEL_JURISDICIONADO P,");
			sql.append("				         JUDICIARIO.PAPEL_JURISDICIONADO MEMBRO,");
			sql.append("				         JUDICIARIO.JURISDICIONADO J,");
			sql.append("				         JUDICIARIO.ASSOCIACAO_JURISDICIONADO A,");
			sql.append("				         JUDICIARIO.JURISDICIONADO JURISMEMBRO,");
			sql.append("				         JUDICIARIO.IDENTIFICACAO_PESSOA CPF_MEMBRO,");
			sql.append("				         JUDICIARIO.IDENTIFICACAO_PESSOA CPF_PRINCIPAL,");
			sql.append("				         JUDICIARIO.TIPO_JURISDICIONADO TIPO_MEMBRO,");
			sql.append("				         JUDICIARIO.TIPO_JURISDICIONADO TIPO_GRUPO");
			
			sql.append("				    WHERE E.SEQ_PAPEL_JURIS_ADVOGADO = P.SEQ_PAPEL_JURISDICIONADO (+)");
			sql.append("				      AND E.SEQ_ASSOCIACAO_JURISDICIONADO = A.SEQ_ASSOCIACAO_JURISDICIONADO (+)");
			sql.append("				      AND P.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO (+)");
			sql.append("				      AND A.SEQ_ASSOCIADO_MEMBRO = MEMBRO.SEQ_PAPEL_JURISDICIONADO (+)");
			sql.append("				      AND MEMBRO.SEQ_JURISDICIONADO = JURISMEMBRO.SEQ_JURISDICIONADO (+)");
				      
			sql.append("				      AND JURISMEMBRO.SEQ_JURISDICIONADO = CPF_MEMBRO.SEQ_JURISDICIONADO (+)");
			sql.append("				      AND J.SEQ_JURISDICIONADO = CPF_PRINCIPAL.SEQ_JURISDICIONADO (+)");
			sql.append("				      AND CPF_MEMBRO.SEQ_TIPO_IDENTIFICACAO (+) = 2 ");
			sql.append("				      AND CPF_PRINCIPAL.SEQ_TIPO_IDENTIFICACAO (+) = 2");
			sql.append("				      AND E.SEQ_DESLOCAMENTO_DEVOLUCAO IS NULL");
			sql.append("				      AND P.SEQ_TIPO_JURISDICIONADO = TIPO_GRUPO.SEQ_TIPO_JURISDICIONADO (+)");
			sql.append("				      AND MEMBRO.SEQ_TIPO_JURISDICIONADO = TIPO_MEMBRO.SEQ_TIPO_JURISDICIONADO (+)");
			sql.append("				)");
			
			if (soNumeros(value.toString())) {
				if (Cpf.validaCPF(value.toString())) {
					sql.append(" WHERE CPF_ADV = " + value);
				} else {
					sql.append(" WHERE COD_ADV = " + value);
				}
			} else {
				sql.append(" WHERE UPPER(NOME_ADV) like '%" + value.toString().toUpperCase() + "%'");
			}

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			listaResult = sqlQuery.list();
			for (Object[] registro : listaResult) {
				JurisdicionadoResult jurisdicionadoResult = new JurisdicionadoResult();
				jurisdicionadoResult.setId( NumberUtils.createLong(registro[0].toString()) );
				jurisdicionadoResult.setNome((String) registro[1]);
				jurisdicionadoResult.setCpf((String) registro[2]);
				listaJurisdicionados.add(jurisdicionadoResult);
			}
			return listaJurisdicionados;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public JurisdicionadoResult pesquisarResultEntidadeGovernamentalPorId(Long id) throws DaoException {
		List<Object[]> listaJurisdicionados = null;
		StringBuffer sql = new StringBuffer();
		List<JurisdicionadoResult> listaResult = new ArrayList<JurisdicionadoResult>();

		try {

			sql.append(" select J.SEQ_JURISDICIONADO, J.NOM_JURISDICIONADO, IP.DSC_IDENTIFICACAO, T.DSC_TIPO_JURISDICIONADO, P.SEQ_PAPEL_JURISDICIONADO"); 
			sql.append("	from  JUDICIARIO.PAPEL_JURISDICIONADO P,"); 
			sql.append("      JUDICIARIO.JURISDICIONADO j, ");
		    sql.append("      JUDICIARIO.IDENTIFICACAO_PESSOA ip, ");
		    sql.append("      JUDICIARIO.TIPO_JURISDICIONADO t ");
		    sql.append("  where J.SEQ_JURISDICIONADO = IP.SEQ_JURISDICIONADO (+)");
		    sql.append("    and T.SIG_TIPO_JURISDICIONADO in ('PART','ADV')");
		    sql.append("    and P.SEQ_TIPO_JURISDICIONADO = T.SEQ_TIPO_JURISDICIONADO");
		    sql.append("    and P.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO");
		    
		    sql.append(" and J.SEQ_JURISDICIONADO = " + id );

	        SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

	        listaJurisdicionados = sqlQuery.list();
	        
			for (Object[] registro : listaJurisdicionados) {
				JurisdicionadoResult result = new JurisdicionadoResult();
				if (registro[0] != null && NumberUtils.isNumber(registro[0].toString())) {
					result.setId( NumberUtils.createLong(registro[0].toString()) );
				}
				result.setNome((String) registro[1]);
				result.setCpf((String) registro[2]);
				result.setPapel((String) registro[3]);					
				if (registro[4] != null && NumberUtils.isNumber(registro[4].toString())) {
					result.setIdPapel( NumberUtils.createLong(registro[4].toString()) );
				}
				result.setEntidadeGovernamental(true);
				listaResult.add(result);
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaResult.get(0);
	}
	
	/**
	 * Recupera um jurisdicionado que possui um dos três papeis: advogado, estagiário ou preposto.
	 */
	@Override
	public JurisdicionadoResult pesquisarResultPorId(Long id) throws DaoException {
		List<Object[]> listaJurisdicionados = null;
		StringBuffer sql = new StringBuffer();
		List<JurisdicionadoResult> listaResult = new ArrayList<JurisdicionadoResult>();

		try {

			sql.append(" select J.SEQ_JURISDICIONADO, J.NOM_JURISDICIONADO, IP.DSC_IDENTIFICACAO, T.DSC_TIPO_JURISDICIONADO, P.SEQ_PAPEL_JURISDICIONADO"); 
			sql.append("	from  JUDICIARIO.PAPEL_JURISDICIONADO P,"); 
			sql.append("      JUDICIARIO.JURISDICIONADO j, ");
		    sql.append("      JUDICIARIO.IDENTIFICACAO_PESSOA ip, ");
		    sql.append("      JUDICIARIO.TIPO_JURISDICIONADO t ");
		    sql.append("  where J.SEQ_JURISDICIONADO = IP.SEQ_JURISDICIONADO");
		    sql.append("    and IP.SEQ_TIPO_IDENTIFICACAO = 2");
		    sql.append("    and T.SIG_TIPO_JURISDICIONADO in ('ADV','EST','PREPO')");
		    sql.append("    and P.SEQ_TIPO_JURISDICIONADO = T.SEQ_TIPO_JURISDICIONADO");
		    sql.append("    and P.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO");
		    
		    sql.append(" and J.SEQ_JURISDICIONADO = " + id );

	        SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

	        listaJurisdicionados = sqlQuery.list();
	        
			for (Object[] registro : listaJurisdicionados) {
				JurisdicionadoResult result = new JurisdicionadoResult();
				if (registro[0] != null && NumberUtils.isNumber(registro[0].toString())) {
					result.setId( NumberUtils.createLong(registro[0].toString()) );
				}
				result.setNome((String) registro[1]);
				result.setCpf((String) registro[2]);
				result.setPapel((String) registro[3]);					
				if (registro[4] != null && NumberUtils.isNumber(registro[4].toString())) {
					result.setIdPapel( NumberUtils.createLong(registro[4].toString()) );
				}
				result.setEntidadeGovernamental(false);
				listaResult.add(result);
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaResult.get(0);
	}
	
	@Override
	public List<JurisdicionadoResult> pesquisarResult(Object value) throws DaoException {
		List<Object[]> listaJuriscionados = null;
		List<JurisdicionadoResult> listaResult = new ArrayList<JurisdicionadoResult>();
		StringBuffer sql = new StringBuffer();

		try {

			sql.append(" select J.SEQ_JURISDICIONADO, J.NOM_JURISDICIONADO, IP.DSC_IDENTIFICACAO, T.DSC_TIPO_JURISDICIONADO, P.SEQ_PAPEL_JURISDICIONADO, IP.SEQ_TIPO_IDENTIFICACAO, T.SIG_TIPO_JURISDICIONADO"); 
			sql.append("	from  JUDICIARIO.PAPEL_JURISDICIONADO P,"); 
			sql.append("      JUDICIARIO.JURISDICIONADO j, ");
		    sql.append("      JUDICIARIO.IDENTIFICACAO_PESSOA ip, ");
		    sql.append("      JUDICIARIO.TIPO_JURISDICIONADO t ");
		    sql.append("  where J.SEQ_JURISDICIONADO = IP.SEQ_JURISDICIONADO");

		    sql.append("    and IP.SEQ_TIPO_IDENTIFICACAO = 2");
		    // estabelece o filtro para advogados, estagiários e prepostos
	    	sql.append("    and T.SIG_TIPO_JURISDICIONADO in ('ADV','EST','PREPO')");
		    
		   	sql.append("    and P.SEQ_TIPO_JURISDICIONADO = T.SEQ_TIPO_JURISDICIONADO");
		    sql.append("    and P.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO");
		    
		    if (soNumeros(value.toString())) {
		    	if (Cpf.validaCPF(value.toString())) {
				    sql.append(" and IP.DSC_IDENTIFICACAO = '" + value + "'");
		    	} else {
				    sql.append(" and J.SEQ_JURISDICIONADO = " + value);
		    	}
		    } else {
			    sql.append(" and UPPER(J.NOM_JURISDICIONADO) like '" + value.toString().toUpperCase() + "%'");
		    }

		    sql.append(" and rownum <= 50");
	        sql.append(" order by j.nom_jurisdicionado");

	        SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

	        listaJuriscionados = sqlQuery.list();
	        
			for (Object[] registro : listaJuriscionados) {
				JurisdicionadoResult result = new JurisdicionadoResult();
				if (registro[0] != null && NumberUtils.isNumber(registro[0].toString())) {
					result.setId( NumberUtils.createLong(registro[0].toString()) );
				}
				result.setNome((String) registro[1]);
				result.setCpf((String) registro[2]);
				result.setPapel((String) registro[3]);
				if (registro[4] != null && NumberUtils.isNumber(registro[4].toString())) {
					result.setIdPapel( NumberUtils.createLong(registro[4].toString()) );
				}
				result.setEntidadeGovernamental(false);
				listaResult.add(result);
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaResult;
	}
	
	@Override
	public List<JurisdicionadoResult> pesquisarResultJurisdicionadoIncidente(List<Long>listaSeqObjetosIncidentes)  throws DaoException {
		List<Object[]> listaJuriscionados = null;
		List<JurisdicionadoResult> listaResult = new ArrayList<JurisdicionadoResult>();
		StringBuffer sql = new StringBuffer();
		
		String inObjs = "(";
		for (Long seqOi: listaSeqObjetosIncidentes) {
			inObjs = inObjs + seqOi + ",";
		}
		inObjs = inObjs.substring(0, inObjs.length()-1);
		inObjs = inObjs + ")";

		sql.append("		SELECT J.SEQ_JURISDICIONADO,");
		sql.append("	       J.NOM_JURISDICIONADO,");
		sql.append("	       IP.DSC_IDENTIFICACAO,");
		sql.append("	       T.DSC_TIPO_JURISDICIONADO,");
		sql.append("	       P.SEQ_PAPEL_JURISDICIONADO,");
		sql.append("	       IP.SEQ_TIPO_IDENTIFICACAO,");
		sql.append("	       T.SIG_TIPO_JURISDICIONADO");
		sql.append("	FROM JUDICIARIO.JURISDICIONADO_INCIDENTE ji,");
		sql.append("	     JUDICIARIO.JURISDICIONADO j,");
		sql.append("	     JUDICIARIO.PAPEL_JURISDICIONADO P,");
		sql.append("	     JUDICIARIO.PROCESSO pr,");
		sql.append("	     JUDICIARIO.IDENTIFICACAO_PESSOA ip,");
		sql.append("	     JUDICIARIO.TIPO_JURISDICIONADO t");
		sql.append("	WHERE JI.SEQ_PAPEL_JURISDICIONADO = P.SEQ_PAPEL_JURISDICIONADO");
		sql.append("	  AND P.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO");
		sql.append("	  AND PR.SEQ_OBJETO_INCIDENTE = ji.SEQ_OBJETO_INCIDENTE");
		sql.append("	  AND PR.SEQ_OBJETO_INCIDENTE IN " + inObjs);
		sql.append("	  AND J.SEQ_JURISDICIONADO = IP.SEQ_JURISDICIONADO (+)");
		sql.append("	  AND P.SEQ_TIPO_JURISDICIONADO = T.SEQ_TIPO_JURISDICIONADO (+)");
		sql.append("	  AND P.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO (+)");
		sql.append("	order by j.nom_jurisdicionado");
		
		try {
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			listaJuriscionados = sqlQuery.list();
        
			for (Object[] registro : listaJuriscionados) {
				JurisdicionadoResult result = new JurisdicionadoResult();
				if (registro[0] != null && NumberUtils.isNumber(registro[0].toString())) {
					result.setId( NumberUtils.createLong(registro[0].toString()) );
				}
				result.setNome((String) registro[1]);
				result.setCpf((String) registro[2]);
				result.setPapel((String) registro[3]);
				if (registro[4] != null && NumberUtils.isNumber(registro[4].toString())) {
					result.setIdPapel( NumberUtils.createLong(registro[4].toString()) );
				}
				result.setEntidadeGovernamental(true);
				listaResult.add(result);
			}
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaResult;
	}
	
	
	@Override
	public List<JurisdicionadoResult> pesquisarResultEntidadeGovernamental(Object value, List<Long>listaSeqObjetosIncidentes)  throws DaoException {
		List<Object[]> listaJuriscionados = null;
		List<JurisdicionadoResult> listaResult = new ArrayList<JurisdicionadoResult>();
		StringBuffer sql = new StringBuffer();
		
//		if (listaSeqObjetosIncidentes == null || listaSeqObjetosIncidentes.size() == 0) {
//			return null;
//		}
		
		String inObjs = "(";
		if (listaSeqObjetosIncidentes != null) {
			for (Long seqOi: listaSeqObjetosIncidentes) {
				inObjs = inObjs + seqOi + ",";
			}
			inObjs = inObjs.substring(0, inObjs.length()-1);
			inObjs = inObjs + ")";
		}

		sql.append("		SELECT J.SEQ_JURISDICIONADO,");
		sql.append("	       J.NOM_JURISDICIONADO,");
		sql.append("	       IP.DSC_IDENTIFICACAO,");
		sql.append("	       T.DSC_TIPO_JURISDICIONADO,");
		sql.append("	       P.SEQ_PAPEL_JURISDICIONADO,");
		sql.append("	       IP.SEQ_TIPO_IDENTIFICACAO,");
		sql.append("	       T.SIG_TIPO_JURISDICIONADO");
		sql.append("	FROM JUDICIARIO.JURISDICIONADO j,");
		if (listaSeqObjetosIncidentes != null) {
			sql.append("	     JUDICIARIO.JURISDICIONADO_INCIDENTE ji,");
			sql.append("	     JUDICIARIO.PROCESSO pr,");
		}
		sql.append("	     JUDICIARIO.PAPEL_JURISDICIONADO P,");
		sql.append("	     JUDICIARIO.IDENTIFICACAO_PESSOA ip,");
		sql.append("	     JUDICIARIO.TIPO_JURISDICIONADO t");
		sql.append("	WHERE 1=1");
		sql.append("	  AND P.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO");
		if (listaSeqObjetosIncidentes != null && listaSeqObjetosIncidentes.size() > 0) {
			sql.append("	  AND PR.SEQ_OBJETO_INCIDENTE = ji.SEQ_OBJETO_INCIDENTE");
			sql.append("	  AND PR.SEQ_OBJETO_INCIDENTE IN " + inObjs);
			sql.append("	  AND JI.SEQ_PAPEL_JURISDICIONADO = P.SEQ_PAPEL_JURISDICIONADO");
		}
		sql.append("	  AND J.FLG_ENTIDADE_GOVERNAMENTAL = 'S'");
		sql.append("	  AND J.SEQ_JURISDICIONADO = IP.SEQ_JURISDICIONADO (+)");
		sql.append("	  AND P.SEQ_TIPO_JURISDICIONADO = T.SEQ_TIPO_JURISDICIONADO (+)");
		sql.append("	  AND P.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO (+)");

	    if (!soNumeros(value.toString())) {
	    	sql.append("	  and upper(J.NOM_JURISDICIONADO) like '%" + value.toString().toUpperCase() + "%'");
	    } else {
	    	sql.append("	  and J.SEQ_JURISDICIONADO = " + value);
	    }

		sql.append("	and rownum <= 50");
		sql.append("	order by j.nom_jurisdicionado");
		
		try {
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			listaJuriscionados = sqlQuery.list();
        
			for (Object[] registro : listaJuriscionados) {
				JurisdicionadoResult result = new JurisdicionadoResult();
				if (registro[0] != null && NumberUtils.isNumber(registro[0].toString())) {
					result.setId( NumberUtils.createLong(registro[0].toString()) );
				}
				result.setNome((String) registro[1]);
				result.setCpf((String) registro[2]);
				result.setPapel((String) registro[3]);
				if (registro[4] != null && NumberUtils.isNumber(registro[4].toString())) {
					result.setIdPapel( NumberUtils.createLong(registro[4].toString()) );
				}
				result.setEntidadeGovernamental(true);
				listaResult.add(result);
			}
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaResult;
	}
	
	@Override
	public List<JurisdicionadoResult> pesquisarResultCadastro(Object value) throws DaoException {
		List<Object[]> listaJuriscionados = null;
		List<JurisdicionadoResult> listaResult = new ArrayList<JurisdicionadoResult>();
		StringBuffer sql = new StringBuffer();
		//Session session = retrieveSession();

		try {

			sql.append(" select DISTINCT J.SEQ_JURISDICIONADO, J.NOM_JURISDICIONADO, DECODE (IP.seq_tipo_identificacao,2, ip.dsc_identificacao, ' ') ");
			sql.append("	FROM  JUDICIARIO.PAPEL_JURISDICIONADO P,"); 
			sql.append("      JUDICIARIO.JURISDICIONADO j, ");
		    sql.append("      JUDICIARIO.IDENTIFICACAO_PESSOA ip, ");
		    sql.append("      JUDICIARIO.TIPO_JURISDICIONADO t ");
		    sql.append("  WHERE IP.SEQ_JURISDICIONADO(+) = J.SEQ_JURISDICIONADO ");
		    sql.append("    AND P.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO");
		    sql.append("    AND P.SEQ_TIPO_JURISDICIONADO = T.SEQ_TIPO_JURISDICIONADO");
		   	sql.append("    AND T.SIG_TIPO_JURISDICIONADO in ('ADV','EST','PREPO')");
		    sql.append("    AND IP.seq_tipo_identificacao(+) = 2 ");
		    
		    if (soNumeros(value.toString())) {
			    sql.append(" and (J.SEQ_JURISDICIONADO = " + value + " OR (IP.DSC_IDENTIFICACAO like '%" + value + "%' AND IP.SEQ_TIPO_IDENTIFICACAO = 2))");
		    } else {
			    sql.append(" and UPPER(J.NOM_JURISDICIONADO) like '" + value.toString().toUpperCase() + "%'");
		    }

		    sql.append(" and rownum <= 100");
	        sql.append(" order by j.nom_jurisdicionado");

	        SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

	        listaJuriscionados = sqlQuery.list();
	        
			for (Object[] registro : listaJuriscionados) {
				JurisdicionadoResult result = new JurisdicionadoResult();
				if (registro[0] != null && NumberUtils.isNumber(registro[0].toString())) {
					result.setId( NumberUtils.createLong(registro[0].toString()) );
				}
				result.setNome((String) registro[1]);
				result.setCpf((String) registro[2]);
				
				listaResult.add(result);
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaResult;
	}

	@Override
	public List<Jurisdicionado> pesquisar(String sugestaoNome)
			throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Jurisdicionado.class);
			c.add(Restrictions.ilike("nome", sugestaoNome.toLowerCase() + "%"));
			c.add(Restrictions.eq("ativo", Boolean.TRUE));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Jurisdicionado> pesquisar(Long idJurisdicionado, String oab, String idUf) throws DaoException {
		
		List<Jurisdicionado> jurisdicionados = null;
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT j FROM Jurisdicionado j ");
			if (oab != null && oab.trim().length() > 0){
				hql.append(" , IdentificacaoPessoa ip ");
			}
			hql.append(" WHERE (1=1)  ");
			
			if (idJurisdicionado != null){
				hql.append(" and j.id =:id  ");
			}
			
			if (oab != null && oab.trim().length() > 0){
				hql.append(" and j.id = ip.jurisdicionado  ");
				hql.append(" and ip.tipoIdentificacao.descricaoTipoIdentificacao = 'OAB'  ");
				hql.append(" and ip.descricaoIdentificacao =:oab  ");
			}
			
			if (idUf != null && idUf.trim().length() > 0){
				hql.append(" and ip.siglaUfOrgaoExpedidor =:idUF ");
			}
			
			hql.append( " AND ROWNUM < 50 ");
			
			Query q = session.createQuery(hql.toString());
			
			if(idJurisdicionado != null){
				q.setLong("id",  idJurisdicionado);
			}
			
			if (oab!= null && oab.trim().length() > 0){
				q.setString("oab", oab);
			}
			
			if (idUf != null && idUf.trim().length() > 0){
				q.setString("idUF", idUf);
			}
			
			
			jurisdicionados = q.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return jurisdicionados;
	}
	
	/**
	 * Verifica se uma String na sua totalidade tem somente números.<br />
	 */
	
	private boolean soNumeros(String valor) {
		
		if(valor == null || valor.length() == 0 ){
			return false;
		}
		
		for(int i = 0 ; i < valor.length() ; i++){
			if(!Character.isDigit(valor.charAt(i))){
				return false;
			}
		}
		
		return true;
	}
}

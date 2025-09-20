/**
 * 
 */
package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.processostf.model.dataaccess.PreListaJulgamentoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import io.jsonwebtoken.lang.Collections;

@Repository
public class PreListaJulgamentoDaoHibernate extends GenericHibernateDao<PreListaJulgamento, Long> implements PreListaJulgamentoDao{

	private static final long serialVersionUID = -1284591112480055968L;
	public static final Long LIMITE_PRE_LISTA_DESTAQUES_CANCELADOS = 40L;

	public PreListaJulgamentoDaoHibernate() {
		super(PreListaJulgamento.class);
	}
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<PreListaJulgamento> listarPreListasJulgamentoDoSetor(Setor setor, Boolean ordenarPorId) throws DaoException {
//		List<PreListaJulgamento> listaRetorno = null;
//		
//	    try {	    	
//	        Session session = retrieveSession();
//	        
//	        String sql = "SELECT l.SEQ_LISTA, l.NOM_LISTA, l.COD_SETOR, l.SEQ_LISTA_JULGAMENTO, l.DOC_CABECALHO "
//	        		+ " FROM JUDICIARIO.LISTA l "
//	        		+ " WHERE 1=1 ";
//	        
//	        // Remove processos ocultos
//	        sql += "AND l.SEQ_LISTA NOT IN ("
//	        		+ "	SELECT loi.SEQ_LISTA "
//	        		+ "	FROM JUDICIARIO.LISTA_OBJETO_INCIDENTE loi "
//	        		+ "	WHERE loi.SEQ_OBJETO_INCIDENTE NOT IN ("
//	        		+ "		SELECT oi.SEQ_OBJETO_INCIDENTE FROM JUDICIARIO.OBJETO_INCIDENTE oi"
//	        		+ "	)"
//	        		+ ")";
//	        
//	        if (setor.getId() != null)
//		          sql += " AND l.COD_SETOR = " + setor.getId();
//	        
//	        sql += " ORDER BY (CASE WHEN l.SEQ_LISTA <= " + LIMITE_PRE_LISTA_DESTAQUES_CANCELADOS.intValue() + " THEN l.SEQ_LISTA END) ASC, ";
//	        
//	        if (ordenarPorId != null && ordenarPorId.equals(Boolean.TRUE))
//	          sql += " l.SEQ_LISTA DESC";
//	        else
//	          sql += " l.NOM_LISTA ";
//	        
//	        Query q = session.createSQLQuery(sql).addEntity(PreListaJulgamento.class);
//	        listaRetorno = (List<PreListaJulgamento>)q.list();
//			
//		} catch (DaoException e) {
//			e.printStackTrace();
//			throw new DaoException(e);
//		}
//		return listaRetorno;
//	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PreListaJulgamento> listarPreListasJulgamentoDoSetor(Setor setor, Boolean ordenarPorId) throws DaoException {
	    try {	    	
	        String hql = "SELECT FROM PreListaJulgamento plj "
	        				+ " LEFT FETCH JOIN plj.objetosIncidentes listaObjetos "
	        				+ " LEFT FETCH JOIN listaObjetos.objetoIncidente oi "
	        				+ " LEFT FETCH JOIN oi.principal principal "
	        				+ " LEFT FETCH JOIN principal.observacaoProcesso op "
	        				+ " WHERE 1=1 ";
	        
	        // Remove processos ocultos
	        hql += " AND plj NOT IN (SELECT loi FROM PreListaJulgamentoObjetoIncidente loi WHERE loi.objetoIncidente NOT IN (SELECT oip FROM ObjetoIncidente oip))";

	        if (setor.getId() != null)
		          hql += " AND plj.setor.id = " + setor.getId();
	        
	        hql += " ORDER BY (CASE WHEN plj.id <= " + LIMITE_PRE_LISTA_DESTAQUES_CANCELADOS.intValue() + " THEN plj.id END) ASC, ";
	        
	        if (ordenarPorId != null && ordenarPorId.equals(Boolean.TRUE))
	          hql += " plj.id DESC";
	        else
	          hql += " plj.nome ";
	        
	        List<PreListaJulgamento> result = retrieveSession().createQuery(hql).list();
			Object[] treeSet = new LinkedHashSet<PreListaJulgamento>(result).toArray();
			return Collections.arrayToList(treeSet);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}
	@Override
	public PreListaJulgamento recuperarPorListaJulgamento(ListaJulgamento listaJulgamento) throws DaoException {
		PreListaJulgamento preListaJulgamento = null;
		
	    try {	    	
	    	Session session = retrieveSession();
	    	
	    	StringBuilder hql = new StringBuilder("SELECT c FROM PreListaJulgamento c");
	    	hql.append(" WHERE 1=1 ");
	    	
	    	if (listaJulgamento != null && listaJulgamento.getId() != null ){
	    		hql.append(" AND c.listaJulgamento.id = " + listaJulgamento.getId() );
	    	}
	    	
			Query q = session.createQuery(hql.toString());
			
			if (q.list() != null && q.list().size() == 1)
				preListaJulgamento = (PreListaJulgamento) q.list().get(0);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	    
		return preListaJulgamento;
	}

	@Override
	public PreListaJulgamento recuperarPreListaPorCategoria(Agrupador agrupador) throws DaoException {
		PreListaJulgamento preListaJulgamento = null;

		try {	    
	    	
	    	Session session = retrieveSession();
	    	StringBuilder hql = new StringBuilder("SELECT c FROM PreListaJulgamento c join c.categorias cat ");

	    	if (agrupador != null && agrupador.getId() != null ){
	    		hql.append(" WHERE cat.id = " + agrupador.getId() );
	    	}
	    	
			Query q = session.createQuery(hql.toString());
			
			if (q.list() != null && q.list().size() == 1)
				preListaJulgamento = (PreListaJulgamento) q.list().get(0);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return preListaJulgamento;
	}
	
	public Long getProximoSequencialParaNomeLista(Integer ano, Long codMinistro, Boolean avulso) throws DaoException{
		Long contador = 0L;
		try {	    
	    	
	    	Session session = retrieveSession();
	    	StringBuilder hql = new StringBuilder("select MAX(TO_NUMBER(tabela.sequencial)) from ");
	    	
	    	if (avulso) {
	    		hql.append("(select regexp_substr( (regexp_substr(l.dsc_lista_processo, '[^-]+', 1, 1)), '[^.]+', 1, 2) sequencial, regexp_substr(l.dsc_lista_processo, '[^-]+',1, 2) ano ");
	    		hql.append("from Julgamento.LISTA_JULGAMENTO l where INSTR(l.dsc_lista_processo, 'AVULSO', 1, 1) > 0 and cod_ministro = " + codMinistro + " ");
	    		hql.append("and regexp_substr(l.dsc_lista_processo, '[^-]+',1, 2) = '" + ano + "' and ");
	    		hql.append("REGEXP_LIKE(regexp_substr( (regexp_substr(l.dsc_lista_processo, '[^-]+', 1, 1)), '[^.]+', 1, 2), '^[[:digit:]]+$')  ) tabela ");
	    	} else {
	    		hql.append("(select regexp_substr(l.dsc_lista_processo, '[^-]+', 1, 1) sequencial, regexp_substr(l.dsc_lista_processo, '[^-]+', 1, 2) ano ");
	    		hql.append("from Julgamento.LISTA_JULGAMENTO l where INSTR(l.dsc_lista_processo, 'AVULSO', 1, 1) = 0 and cod_ministro = " + codMinistro + " ");
	    		hql.append("and regexp_substr(l.dsc_lista_processo, '[^-]+', 1, 2) = '" + ano + "' and ");
	    		hql.append("REGEXP_LIKE(regexp_substr( l.dsc_lista_processo, '[^-]+', 1, 1), '^[[:digit:]]+$') ) tabela ");
	    	}

	    	SQLQuery q = session.createSQLQuery(hql.toString());
	    	Object uniqueResult = q.uniqueResult();
	    	if (uniqueResult == null)
	    		contador = 0L;
	    	else {
	    		contador = Long.parseLong(String.valueOf( ((BigDecimal) uniqueResult).intValue() ));
	    	}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return ++contador;
	}
}

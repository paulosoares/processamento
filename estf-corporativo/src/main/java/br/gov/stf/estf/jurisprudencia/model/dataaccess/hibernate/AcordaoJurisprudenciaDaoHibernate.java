/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.AcordaoJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.AcordaoJurisprudenciaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 20.08.2012
 */
@Repository
public class AcordaoJurisprudenciaDaoHibernate extends GenericHibernateDao<AcordaoJurisprudencia, Long> implements
		AcordaoJurisprudenciaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6814493405850566110L;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public AcordaoJurisprudenciaDaoHibernate() {
		super(AcordaoJurisprudencia.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void inserirProcessoPrincipal (IncidenteAnalise incidenteAnalise) throws DaoException {
		CallableStatement stmt = null;
		
		try {
			if(incidenteAnalise != null && incidenteAnalise.getId() != null){
				Session session = retrieveSession(); 
				
				Connection connection = session.connection();
				stmt = connection.prepareCall(
						"{call jurisprudencia.pkg_carrega_brs_sjur.prc_insere_processo_principal(?)}");
				
				stmt.setLong(1, incidenteAnalise.getId());
				stmt.execute();
				
				stmt.close();
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void ordenarItensLegislacao (Long idLegislacao) throws DaoException {
		CallableStatement stmt = null;
		
		try {
			if(idLegislacao != null ){
				Session session = retrieveSession(); 
				
				Connection connection = session.connection();
				stmt = connection.prepareCall(
						"{call jurisprudencia.pkg_carrega_brs_sjur.prc_ordena_item_legislacao(?)}");
				
				stmt.setLong(1, idLegislacao);
				stmt.execute();
				
				stmt.close();
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	@Override
	public boolean hasJurisprudenciaRelevante(AcordaoJurisprudencia acordaoJurisprudencia) throws DaoException{
		Long total = 0L;
		
		try {
			if(acordaoJurisprudencia != null && acordaoJurisprudencia.getId() != null){
				Session session = retrieveSession();
				
				Query query = session.createSQLQuery(
						"SELECT count(*) num FROM JURISPRUDENCIA.JURISPRUDENCIA_RELEVANTE jr " +
						"where " +
						" JR.SEQ_SJUR = "+acordaoJurisprudencia.getId())
						.addScalar("num", Hibernate.LONG);

				total = (Long) query.list().get(0);
			}
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		if(total > 0) return true;
		
		return false;
	}
	
	@Override
	public boolean hasConferenciaAcordao(AcordaoJurisprudencia acordaoJurisprudencia) throws DaoException{
		Long total = 0L;
		
		try {
			if(acordaoJurisprudencia != null && acordaoJurisprudencia.getId() != null){
				Session session = retrieveSession();
				
				Query query = session.createSQLQuery(
						"SELECT count(*) num FROM JURISPRUDENCIA.CONFERENCIA_ACORDAO ca " +
						"where " +
						" CA.SEQ_SJUR = "+acordaoJurisprudencia.getId())
						.addScalar("num", Hibernate.LONG);

				total = (Long) query.list().get(0);
			}
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		if(total > 0) return true;
		
		return false;
	}
	
	public int pesquisarPorObjetoIncidente(Long id) throws PersistenceException {		
		String sql = "select sj.seq_sjur from brs.sjur sj where sj.seq_objeto_incidente = "+id;
		
		int res = jdbcTemplate.queryForInt(sql);
		
		return res;		
	}
	
	@Override
	public void salvarPublicacao(Long idAcordaoJurisprudencia, String txtPublicacao) throws DaoException {
		try {
			Session session = retrieveSession();
				
			Query query = session.createSQLQuery(
						"UPDATE BRS.SJUR SET DSC_INFORMACAO_PUBLICACAO = '" + txtPublicacao + "' " +
						" where " +
						" SEQ_SJUR = " + idAcordaoJurisprudencia);

			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}

	@Override
	public AcordaoJurisprudencia recuperarPorObjetoIncidente(
			ObjetoIncidente<?> oi) throws DaoException {

		try {
			Session session = retrieveSession();
			
			Criteria c = session.createCriteria(AcordaoJurisprudencia.class);
			
			c.add(Restrictions.eq("objetoIncidente", oi));
			
			return (AcordaoJurisprudencia) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}

	}

	@Override
	public Integer quantidadeAcordaosMesmoSentidoBrs(Long objetoIncidenteId){
		
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT REGEXP_COUNT (s.doc_acordao_mesmo_sentido, '^[A-Z]{1,3}\\s{1,}', 1, 'm') AS QDE_BRS ")
			  .append("FROM brs.sjur s ")
			  .append("WHERE s.SEQ_OBJETO_INCIDENTE = ")
			  .append(objetoIncidenteId);
			
			try {
				return jdbcTemplate.queryForInt(sb.toString());
			}
			
			catch (EmptyResultDataAccessException e) {
				return 0;
			}
			
	}
}

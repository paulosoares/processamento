/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Types;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.IncidenteAnaliseDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
@Repository
public class IncidenteAnaliseDaoHibernate extends GenericHibernateDao<IncidenteAnalise, Long> implements IncidenteAnaliseDao {

	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4901657289037611914L;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public IncidenteAnaliseDaoHibernate() {
		super(IncidenteAnalise.class);
	}

	@Override
	public IncidenteAnalise recuperar(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(IncidenteAnalise.class);
			c.add(Restrictions.eq("objetoIncidente", objetoIncidente));
			
			return (IncidenteAnalise) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public byte[] recuperarVisualizacaoSucessivo(IncidenteAnalise incidenteAnalise) throws DaoException {
		try {
			SessionImplementor session = (SessionImplementor) retrieveSession();
			
			CallableStatement stmt = session.getBatcher().prepareCallableStatement(
					"{? = call JURISPRUDENCIA.PKG_CARREGA_BRS_SJUR.FNC_VISUALIZAR_DOC_SUCESSIVO ( ? )}");
			
			stmt.registerOutParameter(1, Types.CLOB);
			stmt.setLong(2, incidenteAnalise.getId());
			stmt.execute();
			
			Clob clob = stmt.getClob(1);
			if (clob == null) {
				return null;
			
			}
			byte[] retorno = IOUtils.toByteArray( new InputStreamReader( clob.getAsciiStream() ), "UTF-8");

			session.getBatcher().closeStatement(stmt);			
			return retorno;
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	@Override
	public String recuperarInformacaoPublicacao(IncidenteAnalise incidenteAnalise) throws DaoException {
		try {
			SessionImplementor session = (SessionImplementor) retrieveSession();
			
			CallableStatement stmt = session.getBatcher().prepareCallableStatement(
					"{? = call JURISPRUDENCIA.PKG_CARREGA_BRS_SJUR.FNC_VISUALIZAR_PUBLICACAO ( ?, ? )}");
			
			stmt.registerOutParameter(1, Types.VARCHAR);
			stmt.setLong(2, incidenteAnalise.getId());
			stmt.setString(3, incidenteAnalise.getTipoClassificacao().getSigla());
			stmt.execute();
			
			String retorno = stmt.getString(1);

			session.getBatcher().closeStatement(stmt);
			return retorno;
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IncidenteAnalise> pesquisarSucessivos(IncidenteAnalise incidenteAnalise) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(IncidenteAnalise.class);
			c.add(Restrictions.eq("incidenteAnalisePai.id", incidenteAnalise.getId()));
			c.addOrder(Order.asc("id"));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public byte[] recuperarVisualizacaoIndexacaoPrincipal(IncidenteAnalise incidenteAnalise) throws DaoException {
		try {
			SessionImplementor session = (SessionImplementor) retrieveSession();
			
			CallableStatement stmt = session.getBatcher().prepareCallableStatement(
					"{? = call JURISPRUDENCIA.PKG_CARREGA_BRS_SJUR.FNC_VISUALIZAR_INDEX_PRINCIPAL ( ?, ? )}");
			
			stmt.registerOutParameter(1, Types.CLOB);
			stmt.setLong(2, incidenteAnalise.getId());
			stmt.setString(3, "S");
			stmt.execute();
			
			Clob clob = stmt.getClob(1);
			if (clob == null) {
				return null;
			}
			byte[] retorno = IOUtils.toByteArray( new InputStreamReader( clob.getAsciiStream() ), "UTF-8");

			session.getBatcher().closeStatement(stmt);			
			return retorno;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public byte[] recuperarVisualizacaoIndexacaoParagrafo(IncidenteAnalise incidenteAnalise, Long idParagrafo) throws DaoException {
		try {
			SessionImplementor session = (SessionImplementor) retrieveSession();
			
			CallableStatement stmt = session.getBatcher().prepareCallableStatement(
					"{? = call JURISPRUDENCIA.PKG_CARREGA_BRS_SJUR.FNC_VISUALIZAR_INDEX_PARAGRAFO ( ?, ?, ? )}");
			
			stmt.registerOutParameter(1, Types.CLOB);
			stmt.setLong(2, incidenteAnalise.getId());
			stmt.setLong(3, idParagrafo);
			stmt.setString(4, "S");
			stmt.execute();
			
			Clob clob = stmt.getClob(1);
			if (clob == null) {
				return null;
			}
			byte[] retorno = IOUtils.toByteArray( new InputStreamReader( clob.getAsciiStream() ), "UTF-8");

			session.getBatcher().closeStatement(stmt);			
			return retorno;
		} catch (Exception e) {
			
			throw new DaoException(e);
		}
	}

	@Override
	public void carregarAcordaoPublicado(Long seqObjetoIncidente, Long seqDataPublicacao) throws DaoException {
		try {
			SessionImplementor session = (SessionImplementor) retrieveSession();
			
			CallableStatement stmt = session.getBatcher().prepareCallableStatement(
					"{call JUDICIARIO.PKG_INSERE_ITEM_CONTROLE.PRC_ANALISE_JURISP_AC_OI ( ?, ? )}");
			
			stmt.setLong(1, seqObjetoIncidente);
			stmt.setLong(2, seqDataPublicacao);
			stmt.execute();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public byte[] recuperarVisualizacaoLegislacoes(IncidenteAnalise incidenteAnalise) throws DaoException {
		try {
			SessionImplementor session = (SessionImplementor) retrieveSession();
			
			int identacao = 20;
			
			CallableStatement stmt = session.getBatcher().prepareCallableStatement(
					"{? = call JURISPRUDENCIA.PKG_CARREGA_BRS_SJUR.FNC_RET_LEGISLACAO_PRINCIPAL ( ?, ? )}");
			
			stmt.registerOutParameter(1, Types.CLOB);
			stmt.setLong(2, incidenteAnalise.getId());
			stmt.setInt(3, identacao);
			stmt.execute();
			
			Clob clob = stmt.getClob(1);
			if (clob == null) {
				return null;
			}
			byte[] retorno = IOUtils.toByteArray( new InputStreamReader( clob.getAsciiStream() ), "UTF-8");

			session.getBatcher().closeStatement(stmt);			
			return retorno;
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}

	@Override
	public Boolean temRepercussaoGeral(Long objetoIncidenteId) throws DaoException {

		//TODO _Não consegue ver a tabela
		//return false;

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(*) FROM BRS.REPERCUSSAO_GERAL ").
		append("where SEQ_OBJETO_INCIDENTE = ").
		append(objetoIncidenteId.toString());
		
		int res = jdbcTemplate.queryForInt(sql.toString());
		
		if(res == 1){
			return Boolean.TRUE;
		}
		
		if(res == 0){
			return Boolean.FALSE;
		}
		
		throw new DaoException("Inconsistência de dados. Há mais de um Objeto Incidente representando a mesma informação na base de jurisprudência.");
		
	}

	@Override
	public Integer quantidadeAcordaosMesmoSentido(Long incidenteAnaliseId) {

		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT COUNT(*) as QDE_INCIDENTES ")
		  .append("FROM jurisprudencia.incidente_analise ia ")
		  .append("WHERE ia.tip_classificacao  = 'AS' ")
		  .append("AND ia.SEQ_INCIDENTE_ANALISE_PAI = ")
		  .append(incidenteAnaliseId);
		
		return jdbcTemplate.queryForInt(sb.toString());
	}
}

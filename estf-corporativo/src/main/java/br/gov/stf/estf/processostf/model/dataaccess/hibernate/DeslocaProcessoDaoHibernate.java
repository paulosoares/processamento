package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso.DeslocaProcessoId;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.dataaccess.DeslocaProcessoDao;
import br.gov.stf.estf.processostf.model.util.DeslocaProcessoDynamicQuery;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.util.query.QueryBuilder;

@Repository
public class DeslocaProcessoDaoHibernate extends
		GenericHibernateDao<DeslocaProcesso, DeslocaProcessoId> implements
		DeslocaProcessoDao {

	/**
	 * 
	 */

	private static final long serialVersionUID = -7399589001838097860L;

	private static final String CODIGO_DESLOCA_INCIDENTE_TEMP = "{call JUDICIARIO.prc_desloca_incidente_temp (?, ?, ?, ?, ?, ?)}";

	public List<DeslocaProcesso> consultaDeslocamentoOrdenadoGuiaDecrescente(
			String classeDoProcesso, Long numeroDoProcesso) throws DaoException {
		Criteria criteria = retrieveSession().createCriteria(
				DeslocaProcesso.class, "dp");
		criteria.add(Restrictions.eq("dp.id.siglaClasseProces",
				classeDoProcesso));
		criteria.add(Restrictions.eq("dp.id.numeroProcesso", numeroDoProcesso));
		criteria.addOrder(Order.desc("dp.id.anoGuia"));
		criteria.addOrder(Order.desc("dp.id.numeroGuia"));

		return criteria.list();
	}

	@Override
	public Long pesquisarSetorUltimoDeslocamento(Processo processo)
			throws DaoException {

		Session session = retrieveSession();

		String sql = "select COD_ORGAO_DESTINO from (select COD_ORGAO_DESTINO from STF.DESLOCA_PROCESSOS where FLG_ULTIMO_DESLOCAMENTO = 'S' AND SIG_CLASSE_PROCES = :sigla and NUM_PROCESSO = :numero ORDER BY SEQ_DESLOCA_PROCESSOS DESC) where ROWNUM = 1";

		SQLQuery query = session.createSQLQuery(sql);

		query.setParameter("sigla", processo.getSiglaClasseProcessual());
		query.setParameter("numero", processo.getNumeroProcessual());

		Number setor = (Number) query.uniqueResult();

		return setor == null ? null : setor.longValue();
	}
	
	@Override
	public Long pesquisarSetorUltimoDeslocamento(Long seqObjetoIncidente) throws DaoException {
		try{
			Session session = retrieveSession();
	
			String sql = "select COD_ORGAO_DESTINO from (select COD_ORGAO_DESTINO from STF.DESLOCA_PROCESSOS where SEQ_OBJETO_INCIDENTE = :seqObjetoIncidente ORDER BY SEQ_DESLOCA_PROCESSOS DESC) where ROWNUM = 1";
			
			SQLQuery query = session.createSQLQuery(sql);
			query.setParameter("seqObjetoIncidente", seqObjetoIncidente);
	
			Number setor = (Number) query.uniqueResult();

			return setor == null ? null : setor.longValue();
		}catch(Exception e){
			e.printStackTrace();
			return null;	
		}
	}	

	public Boolean deslocarProcesso(Processo processo) throws DaoException {
		return null;
	}

	public DeslocaProcessoDaoHibernate() {
		super(DeslocaProcesso.class);
	}

	public List<DeslocaProcesso> pesquisar(
			DeslocaProcessoDynamicQuery consultaDinamica) throws DaoException {
		Query query = montaQueryDaConsultaDinamica(consultaDinamica);
		return query.list();
	}

	private Query montaQueryDaConsultaDinamica(
			DeslocaProcessoDynamicQuery consultaDinamica) throws DaoException {
		Session session = retrieveSession();
		QueryBuilder queryBuilder = new QueryBuilder(session,
				DeslocaProcesso.class,
				DeslocaProcessoDynamicQuery.ALIAS_DESLOCA_PROCESSO,
				consultaDinamica);
		return queryBuilder.getQuery();

	}

	public DeslocaProcesso recuperarUltimoDeslocamento(
			DeslocaProcessoDynamicQuery consultaDinamica) throws DaoException {
		Query query = montaQueryDaConsultaDinamica(consultaDinamica);
		return (DeslocaProcesso) query.uniqueResult();
	}

	public void persistirDeslocamentoProcesso(
			DeslocaProcesso deslocamentoProcesso) throws DaoException {

		Session session = retrieveSession();
		/*
		 * if (deslocamentoProcesso.getCodigoLocalizacaoDestino()!=null){ long
		 * numGuia=
		 * getNumeroGuia(deslocamentoProcesso.getCodigoLocalizacaoOrigem(),
		 * deslocamentoProcesso.getCodigoLocalizacaoDestino(),
		 * deslocamentoProcesso.getAnoGuia());
		 * 
		 * deslocamentoProcesso.setNumeroGuia(numGuia); }
		 */

		try {
			session.save(deslocamentoProcesso);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}
	
	@Override
	public Integer recuperarUltimaSequencia(Guia guia) throws DaoException {
		try {
			String sql = "SELECT MAX(D.NUM_SEQUENCIA) ULTIMA_SEQUENCIA FROM STF.DESLOCA_PROCESSOS D" +
					     " WHERE D.NUM_GUIA = " + guia.getId().getNumeroGuia() +
					     " AND D.ANO_GUIA = " + guia.getId().getAnoGuia() +
					     " AND D.COD_ORGAO_ORIGEM = " + guia.getId().getCodigoOrgaoOrigem();

			SQLQuery q = retrieveSession().createSQLQuery(sql.toString());
			Integer sequencia = NumberUtils.createInteger( q.uniqueResult().toString() );
			return sequencia;
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}

	public Long getNumeroGuia(long cod_orgao_origem, long cod_orgao_destino,
			short ano_guia) throws DaoException {

		Session session = retrieveSession();
		DeslocaProcesso DeslocaProcesso = new DeslocaProcesso();

		StringBuffer hql = new StringBuffer(" FROM DeslocaProcesso "
				+ " where cod_orgao_origem = '" + cod_orgao_origem
				+ "' and cod_orgao_destino = '" + cod_orgao_destino
				+ "' and ano_guia = '" + ano_guia + "' order by num_guia desc ");

		Query query = session.createQuery(hql.toString());

		Iterator iterator = query.iterate();
		long novoNumGuia = 0;
		if (iterator.hasNext()) {

			DeslocaProcesso = (DeslocaProcesso) iterator.next();

			novoNumGuia = DeslocaProcesso.getId().getNumeroGuia();
			return novoNumGuia++;
		}

		return novoNumGuia;

	}

	// retorna o status do deslocamento: data de recebimento ou não recebido
	/*
	 * public List<DeslocaProcesso> recuperarDeslocamentoProcesso(Guia guia)
	 * throws DaoException { Session session = retrieveSession();
	 * 
	 * try { Criteria c = session.createCriteria(DeslocaProcesso.class); c.add(
	 * Restrictions.eq("id.anoGuia", guia.getId().getAnoGuia()) ); c.add(
	 * Restrictions.eq("id.numeroGuia", guia.getId().getNumeroGuia()) ); c.add(
	 * Restrictions.eq("id.codigoOrgaoOrigem",
	 * guia.getId().getCodigoOrgaoOrigem()) );
	 * 
	 * List <DeslocaProcesso> deslocaProcessos = c.list();
	 * 
	 * return deslocaProcessos; } catch (HibernateException e) { throw new
	 * DaoException("HibernateException", SessionFactoryUtils
	 * .convertHibernateAccessException(e)); } catch (RuntimeException e) {
	 * throw new DaoException("RuntimeException", e); } }
	 */
	/*
	 * String sql = ""; try { Session session = retrieveSession(); Connection
	 * connection = session.connection();
	 * 
	 * sql = "select dp.* " + "	from stf.desloca_processos dp, stf.guias g " +
	 * "	where dp.num_guia = g.num_guia " + "	and dp.ano_guia = g.ano_guia " +
	 * "	and dp.cod_orgao_origem = ?" + "	and dp.num_guia = ?" +
	 * "	and dp.ano_guia = ?" + "	and dp.cod_orgao_origem = g.cod_orgao_origem"
	 * + "	order by dp.num_guia, dp.ano_guia, dp.cod_orgao_origem;";
	 * 
	 * SQLQuery query = session.createSQLQuery(sql).addEntity("dp",
	 * DeslocaProcesso.class); query.setLong(0,
	 * guia.getId().getCodigoOrgaoOrigem()); query.setLong(1,
	 * guia.getId().getNumeroGuia()); query.setInteger(2,
	 * guia.getId().getAnoGuia()); return (DeslocaProcesso)
	 * query.uniqueResult();
	 * 
	 * 
	 * } catch (Exception e) { e.printStackTrace(); throw new DaoException(e); }
	 * 
	 * }
	 */

	// executa o recebimento de um processo: atualização da data de recebimento
	public void receberProcesso(DeslocaProcesso deslocamentoProcesso)
			throws DaoException {

		Session session = retrieveSession();
		try {
			session.save(deslocamentoProcesso);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	// recupera o(s) deslocamento(s) de uma guia
	public List<DeslocaProcesso> recuperarDeslocamentoProcessos(Guia guia)
			throws DaoException {
		Session session = retrieveSession();

		session.clear();

		StringBuffer sql = new StringBuffer();
		List<DeslocaProcesso> deslocaProcessos = null;

		try {
			sql.append("FROM DeslocaProcesso dp WHERE dp.id.anoGuia = :anoGuia "
					+ "AND dp.id.numeroGuia = :numeroGuia "
					+ "AND dp.id.codigoOrgaoOrigem = :orgaoOrigem AND ROWNUM <500 order by dp.numeroSequencia");

			Query sqlQuery = session.createQuery(sql.toString());
			sqlQuery.setShort("anoGuia", guia.getId().getAnoGuia());
			sqlQuery.setLong("numeroGuia", guia.getId().getNumeroGuia());
			sqlQuery.setLong("orgaoOrigem", guia.getId().getCodigoOrgaoOrigem());

			if (guia.getPaginacao()) {
				sqlQuery.setFirstResult(guia.getFirstValue());
				sqlQuery.setMaxResults(guia.getMaxValue());
			}
			deslocaProcessos = sqlQuery.list();
			return deslocaProcessos;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	@Override
	public List<DeslocaProcesso> recuperarDeslocamentoProcessosRecebimentoExterno(
			Guia guia) throws DaoException {
		Session session = retrieveSession();
		try {

			Criteria c = session.createCriteria(DeslocaProcesso.class);
			c.add(Restrictions.eq("id.anoGuia", guia.getId().getAnoGuia()));
			c.add(Restrictions
					.eq("id.numeroGuia", guia.getId().getNumeroGuia()));
			c.add(Restrictions.eq("id.codigoOrgaoOrigem", guia.getId()
					.getCodigoOrgaoOrigem()));
			c.add(Restrictions.isNull("dataRecebimento"));
		    c.addOrder(Order.asc("numeroSequencia"));


			List<DeslocaProcesso> deslocaProcessos = c.list();

			return deslocaProcessos;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	@Override
	public void removerProcesso(DeslocaProcesso processo) throws DaoException {
		Session session = retrieveSession();
		try {
			session.delete(processo);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
	}

	@Override
	public List<DeslocaProcesso> pesquisarDataRecebimentoGuiaProcesso(Guia guia)
			throws DaoException {
		Session session = retrieveSession();
		try {

			Criteria criteria = session.createCriteria(DeslocaProcesso.class);
			criteria.add(Restrictions.eq("id.anoGuia", guia.getId()
					.getAnoGuia()));
			criteria.add(Restrictions.eq("id.numeroGuia", guia.getId()
					.getNumeroGuia()));
			criteria.add(Restrictions.eq("id.codigoOrgaoOrigem", guia.getId()
					.getCodigoOrgaoOrigem()));
			criteria.add(Restrictions.isNotNull("dataRecebimento"));
			criteria.add(Restrictions.sqlRestriction("rownum < 500" ));
			return criteria.list();
		} catch (Exception e) {
			throw new DaoException("HibernateException", e);
		}
	}

	/*
	 * (non-Javadoc) Recupera o último deslocamento (seja ele recebido ou em
	 * trânsito) de um determinado processo.
	 */
	@Override
	public DeslocaProcesso recuperarUltimoDeslocamentoProcesso(Processo processo)
			throws DaoException {

		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(DeslocaProcesso.class);
		criteria.add(Restrictions.eq("numeroProcesso",
				processo.getNumeroProcessual()));
		criteria.add(Restrictions.eq("classeProcesso",
				processo.getSiglaClasseProcessual()));
		criteria.add(Restrictions.eq("ultimoDeslocamento", true));
		if (criteria.list().size() == 0) {
			return null;
		} else {
			return (DeslocaProcesso) criteria.list().get(0);
		}
	}

	@Override
	public List<DeslocaProcesso> recuperarProcessosPeloSetor(Long codigoSetor)
			throws DaoException {
		Session session = retrieveSession();

		try {
			Criteria criteria = session.createCriteria(DeslocaProcesso.class);
			criteria.add(Restrictions.eq("codigoOrgaoDestino", codigoSetor));
			criteria.add(Restrictions.eq("ultimoDeslocamento", true));
			criteria.add(Restrictions.isNotNull("dataRecebimento"));
			criteria.add(Restrictions.isNotNull("id.processo.id"));
			return criteria.list();
		} catch (Exception e) {
			throw new DaoException("HibernateException", e);
		}
	}

	@Override
	public void deslocaProcessoSetor(Long P_COD_ORGAO_ORIGEM,
			Long P_COD_ORGAO_DESTINO, String P_TIP_ORGAO_ORIGEM,
			String P_TIP_ORGAO_DESTINO, Long P_SEQ_OBJETO_INCIDENTE)
			throws DaoException {
		Session session = retrieveSession();
		try {

			SessionImplementor callableSession = (SessionImplementor) session;
			CallableStatement stmt = callableSession
					.getBatcher()
					.prepareCallableStatement(
							"{call JUDICIARIO.PRC_DESLOCA_INCIDENTE (?,?,?,?,?,?)}");
			stmt.setLong(1, P_COD_ORGAO_ORIGEM);
			stmt.setLong(2, P_COD_ORGAO_DESTINO);
			stmt.setString(3, P_TIP_ORGAO_ORIGEM);
			stmt.setString(4, P_TIP_ORGAO_DESTINO);
			stmt.setLong(5, P_SEQ_OBJETO_INCIDENTE);
			stmt.setString(6, "N");
			stmt.execute();
		} catch (Exception e) {
			throw new DaoException("HibernateException", e);
		} finally {
			session.flush();
		}
	}

	@Override
	public DeslocaProcesso recuperaDeslocamentoProcessoRecebimento(
			DeslocaProcesso deslocaProcesso, Long setorUsuario)
			throws DaoException {
		Session session = retrieveSession();
		try {
			Criteria criteria = session.createCriteria(DeslocaProcesso.class);
			criteria.add(Restrictions.eq("codigoOrgaoDestino", setorUsuario));
			criteria.add(Restrictions.eq("classeProcesso",
					deslocaProcesso.getClasseProcesso()));
			criteria.add(Restrictions.eq("numeroProcesso",
					deslocaProcesso.getNumeroProcesso()));
			criteria.add(Restrictions.isNull("dataRecebimento"));
			return (DeslocaProcesso) criteria.list().get(0);
		} catch (Exception e) {
			throw new DaoException("HibernateException", e);
		}

	}

	public void deslocar(DeslocaProcesso deslocaProcesso) throws DaoException {
		Session session = retrieveSession();
		try {
			Criteria criteria = session.createCriteria(DeslocaProcesso.class);
			// criteria.add(Restrictions.eq("codigoOrgaoDestino",
			// setorUsuario));
			criteria.add(Restrictions.eq("classeProcesso",
					deslocaProcesso.getClasseProcesso()));
			criteria.add(Restrictions.eq("numeroProcesso",
					deslocaProcesso.getNumeroProcesso()));
			criteria.add(Restrictions.isNull("dataRecebimento"));
		} catch (Exception e) {
			throw new DaoException("HibernateException", e);
		}
	}

	public void deslocarProcesso(Processo processo,
			Long codigoOrgaoSetorOrigem, Long codigoOrgaoSetorDestino,
			Integer tipoOrgaoSetorOrigem, Integer tipoOrgaoSetorDestino)
			throws DaoException {
		try {
			CallableStatement stmt = montaChamadaDaProcedure(CODIGO_DESLOCA_INCIDENTE_TEMP);
			stmt.setLong(1, codigoOrgaoSetorOrigem);
			stmt.setLong(2, codigoOrgaoSetorDestino);
			stmt.setString(3, tipoOrgaoSetorOrigem.toString());
			stmt.setString(4, tipoOrgaoSetorDestino.toString());
			stmt.setLong(5, processo.getId());
			stmt.setString(6, "N");
			//
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			throw new DaoException(e);
		}

	}

	private CallableStatement montaChamadaDaProcedure(String codigoDaProcedure)
			throws DaoException, SQLException {
		SessionImplementor session = (SessionImplementor) retrieveSession();
		CallableStatement stmt = session.getBatcher().prepareCallableStatement(
				codigoDaProcedure);
		return stmt;
	}

	@Override
	public List<DeslocaProcesso> recuperaPorProcessoOrigemExterna(
			Processo processo) throws DaoException {
		List<DeslocaProcesso> deslocaProcessos = new ArrayList<DeslocaProcesso>();

		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append("SELECT dp FROM DeslocaProcesso dp,  Origem o ");
			hql.append("WHERE dp.codigoOrgaoDestino = o.id ");
			hql.append("AND dp.classeProcesso = :siglaProcesso ");
			hql.append("AND dp.numeroProcesso = :numProcesso ");

			Query query = session.createQuery(hql.toString());
			query.setString("siglaProcesso",
					processo.getSiglaClasseProcessual());
			query.setLong("numProcesso", processo.getNumeroProcessual());

			deslocaProcessos = query.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return deslocaProcessos;
	}

	@Override
	public void  atualizaAndamento(DeslocaProcesso deslocaProcesso, Long idAndamentoProcesso)
			throws DaoException {
		try {
			Session session = retrieveSession();

			StringBuffer sql = new StringBuffer();
			
			sql.append("UPDATE STF.DESLOCA_PROCESSOS ");
			sql.append(" SET seq_andamento_processo  = :idDeslocamentoProcesso  ");			
			sql.append(" WHERE num_guia = :numGuia ");
			sql.append(" AND  ano_guia  = :anoGuia ");
			sql.append(" AND  cod_orgao_origem = :orgaoOrigem ");
			sql.append(" AND  sig_classe_proces = :classeProcessual ");
			sql.append(" AND  num_processo = :numeroProcesso ");
			
			SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
			
			sqlQuery.setLong("idDeslocamentoProcesso", idAndamentoProcesso);
			sqlQuery.setLong("numGuia", deslocaProcesso.getGuia().getNumeroGuia());
			sqlQuery.setLong("anoGuia",deslocaProcesso.getGuia().getAnoGuia());
			sqlQuery.setLong("orgaoOrigem",deslocaProcesso.getGuia().getCodigoOrgaoOrigem());
			sqlQuery.setString("classeProcessual", deslocaProcesso.getClasseProcesso());
			sqlQuery.setLong("numeroProcesso",deslocaProcesso.getNumeroProcesso());

			sqlQuery.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	@Override
	public void alterarPkDeslocaProcesso(DeslocaProcesso deslocaProcesso, Guia novaGuia,AndamentoProcesso andamentoProcesso,Integer numSequencia)
			throws DaoException {
		try {
			Session session = retrieveSession();

			StringBuffer sql = new StringBuffer();
			
			sql.append("UPDATE STF.DESLOCA_PROCESSOS ");
			sql.append("SET NUM_GUIA = :novoNumGuia, ");
			if(andamentoProcesso != null){
				sql.append("SEQ_ANDAMENTO_PROCESSO = :novoAndamentoProcesso, ");
			}
			if(numSequencia != null && numSequencia > 0){
				sql.append("NUM_SEQUENCIA = :novoNumSequencia, ");
			}
			sql.append("ANO_GUIA = :novoAnoGuia, ");
			sql.append("COD_ORGAO_ORIGEM = :novoCodOrigem, ");
			sql.append("COD_ORGAO_DESTINO = :novoCodDestino ");
			
			sql.append("WHERE SEQ_OBJETO_INCIDENTE= :objetoIncidente ");
			sql.append("AND COD_ORGAO_ORIGEM= :codOrigem ");
			sql.append("AND NUM_GUIA= :numGuia ");
			sql.append("AND ANO_GUIA= :anoGuia ");
			
			SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
			
			sqlQuery.setLong("novoNumGuia", novaGuia.getId().getNumeroGuia());
			sqlQuery.setLong("novoAnoGuia", novaGuia.getId().getAnoGuia());
			sqlQuery.setLong("novoCodOrigem", novaGuia.getId().getCodigoOrgaoOrigem());
			sqlQuery.setLong("novoCodDestino", novaGuia.getCodigoOrgaoDestino());
			
			if(andamentoProcesso != null){
				sqlQuery.setLong("novoAndamentoProcesso", andamentoProcesso.getId());
			}
			if(numSequencia != null && numSequencia > 0){
				sqlQuery.setInteger("novoNumSequencia", numSequencia);
			}
			sqlQuery.setLong("objetoIncidente", deslocaProcesso.getId().getProcesso().getId());
			sqlQuery.setLong("codOrigem", deslocaProcesso.getId().getCodigoOrgaoOrigem());
			sqlQuery.setLong("numGuia", deslocaProcesso.getId().getNumeroGuia());
			sqlQuery.setLong("anoGuia", deslocaProcesso.getId().getAnoGuia());
			
			sqlQuery.executeUpdate();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	@Override
	public void  atualizarDeslocamento(Long  idDeslocaProcesso, Long idProcesso)
			throws DaoException {
		try {
			if(idDeslocaProcesso != null && idProcesso != null) {
				Session session = retrieveSession();
	
				StringBuffer sql = new StringBuffer();
				
				sql.append("UPDATE STF.DESLOCA_PROCESSOS ");
				sql.append(" SET seq_objeto_incidente  = :idProcesso  ");			
				sql.append(" WHERE seq_desloca_processos = :idDeslocaProcesso and seq_objeto_incidente is null ");
				
				SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
				
				sqlQuery.setLong("idProcesso", idProcesso);
				sqlQuery.setLong("idDeslocaProcesso",idDeslocaProcesso);
	
				sqlQuery.executeUpdate();
			}
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	@Override
	public boolean isBaixadoParaOrigem(Processo processo, Origem origem, Andamento andamento) throws DaoException {

		Session session = retrieveSession();
		String sql = new String();
	
		sql = "SELECT count(*) from STF.DESLOCA_PROCESSOS dp, STF.ANDAMENTO_PROCESSOS ap " +
			  "where AP.SEQ_ANDAMENTO_PROCESSO = DP.SEQ_ANDAMENTO_PROCESSO and AP.COD_ANDAMENTO = :codAndamento and AP.flg_lancamento_indevido <> 'S' " +
			  "and DP.SIG_CLASSE_PROCES = :siglaProcesso and DP.NUM_PROCESSO = :numeroProcesso and dp.COD_ORGAO_DESTINO = :codOrgaoDestino";

		Query q = session.createSQLQuery(sql);
		q.setLong("codAndamento", andamento.getId());
		q.setString("siglaProcesso", processo.getSiglaClasseProcessual());
		q.setLong("numeroProcesso", processo.getNumeroProcessual());
		q.setLong("codOrgaoDestino", origem.getId());

		List baixas = q.list();
		
		return baixas != null && ((java.math.BigDecimal)baixas.get(0)).longValue() > 0;
	}
	
	/**
	 * Recupera a última remessa do processo. Não é o último recebimento, mas sim o deslocamento "em trânsito"
	 * @param siglaProcesso
	 * @param numeroProcesso
	 * @return
	 * @throws DaoException
	 */
	@Override
	public DeslocaProcesso recuperarUltimaRemessaProcesso(String siglaProcesso, Long numeroProcesso) throws DaoException {

		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(DeslocaProcesso.class);
		criteria.add(Restrictions.eq("numeroProcesso", numeroProcesso));
		criteria.add(Restrictions.eq("classeProcesso", siglaProcesso));
//		criteria.add(Restrictions.isNull("dataRecebimento"));
//		return (DeslocaProcesso) criteria.uniqueResult();
		if (criteria.list().size() == 0) {
			return null;
		} else {
			return (DeslocaProcesso) criteria.list().get(0);
		}
	}

}

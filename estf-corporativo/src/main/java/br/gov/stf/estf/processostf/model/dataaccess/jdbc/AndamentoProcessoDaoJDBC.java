package br.gov.stf.estf.processostf.model.dataaccess.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoProcessoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AndamentoProcessoDaoJDBC extends
		GenericHibernateDao<AndamentoProcesso, Long> implements
		AndamentoProcessoDao {

	public AndamentoProcessoDaoJDBC() {
		super(AndamentoProcesso.class);
	}

	private static final long serialVersionUID = -913909262894804422L;

	public Long persistirAndamentoProcesso(AndamentoProcesso andamentoProcesso)
			throws DaoException {
		Session session = retrieveSession();
		
		try {
			session.saveOrUpdate(andamentoProcesso);
			session.flush();
			session.connection().commit();
			
			return andamentoProcesso.getId();
		}

		catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		}
		catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	public List pesquisarAndamentoProcesso(String sigla, Long numero,
			Long codigoTipoAndamento) throws DaoException {
		Session session = retrieveSession();
		List andamentos = null;
		try {
			Criteria criteria = session.createCriteria(AndamentoProcesso.class);

			criteria.add(Restrictions.eq("siglaClasseProcessual", sigla));
			criteria.add(Restrictions.eq("numeroProcessual", numero));
			criteria.add(Restrictions.eq("tipoAndamento.codigo",
					codigoTipoAndamento));

			andamentos = criteria.list();
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return andamentos;

	}

	public AndamentoProcesso recuperarAndamentoProcesso(String sigla,
			Long numero, Long codigoTipoAndamento) throws DaoException {

		Session session = retrieveSession();

		AndamentoProcesso andamentoProcesso = null;

		try {
			/*
			 * AndamentoProcesso.AndamentoProcessoId andamentoProcessoId = new
			 * AndamentoProcesso.AndamentoProcessoId();
			 * andamentoProcessoId.setNumeroProcessual(numero);
			 * andamentoProcessoId.setSiglaClasseProcessual(sigla);
			 */
			Criteria criteria = session.createCriteria(AndamentoProcesso.class);
			// criteria.add(Restrictions.idEq(andamentoProcessoId));

			criteria.add(Restrictions.eq("siglaClasseProcessual", sigla));
			criteria.add(Restrictions.eq("numeroProcessual", numero));
			criteria.add(Restrictions.eq("tipoAndamento.codigo",
					codigoTipoAndamento));

			andamentoProcesso = (AndamentoProcesso) criteria.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return andamentoProcesso;
	}

	public List<AndamentoProcesso> pesquisarAndamentoProcessoSetor(
			String sigla, Long numero, Long setor) throws DaoException {

		Session session = retrieveSession();
		List<AndamentoProcesso> andamentos = null;
		try {

			Criteria criteria = session.createCriteria(AndamentoProcesso.class);

			criteria.add(Restrictions.eq("siglaClasseProcessual", sigla));
			criteria.add(Restrictions.eq("numeroProcessual", numero));
			criteria.add(Restrictions.eq("codigoSetor", setor));

			andamentos = criteria.list();
			;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return andamentos;

	}

	@SuppressWarnings("unchecked")
	public List<AndamentoProcesso> pesquisarAndamentoProcesso(String sigla, Long numero) throws DaoException {

		List<AndamentoProcesso> andamentos = null;
		StringBuffer sql = new StringBuffer();
		
		try {
			sql.append(" SELECT ap FROM AndamentoProcesso ap LEFT OUTER JOIN FETCH ap.listaTextoAndamentoProcessos ");
			sql.append(" WHERE ");
			
			if (sigla != null){
				sql.append(" ap.sigClasseProces = :sigla ");
			}
			
			if (numero != null){
				sql.append(" AND ap.numProcesso = :numero ");
			}
			
			sql.append( " ORDER BY ap.numeroSequencia ASC ");
			
			Query q = retrieveSession().createQuery(sql.toString());
			
			if(sigla != null){
				q.setParameter("sigla",  sigla);
			}
			
			if (numero!= null){
				q.setParameter("numero", numero);
			}
			
			andamentos = q.list();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return andamentos;
	}
	
	

	public Long recuperarUltimaSequenciaAndamento(String sigla, Long numero)
			throws DaoException {
		Session session = retrieveSession();

		try {

			StringBuffer hql = new StringBuffer(
					" SELECT MAX(a.numeroSequencia) "
							+ " FROM AndamentoProcesso a " + " WHERE (1=1) ");

			if (sigla != null && sigla.trim().length() > 0) {
				hql.append(" AND a.siglaClasseProcessual = '" + sigla + "'");
			}

			if (numero != null) {
				hql.append(" AND a.numeroProcessual = " + numero);
			}

			Query query = session.createQuery(hql.toString());
			if (query != null) {
				Long sequencia = (Long) query.uniqueResult();
				if (sequencia == null) {
					return (long) 0;
				} else {
					return sequencia;
				}
			}

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return null;
	}

	public OrigemAndamentoDecisao recuperarOrigemAndamentoDecisao(Long id,
			String descricao, Long codigoSetor, Long codigoMinistro,
			Boolean ativo) throws DaoException {
		Session session = retrieveSession();

		OrigemAndamentoDecisao origem = null;

		try {

			Criteria criteria = session
					.createCriteria(OrigemAndamentoDecisao.class);

			if (id != null) {
				criteria.add(Restrictions.eq("id", id));
			}

			if (descricao != null && descricao.trim().length() > 0) {
				criteria.add(Restrictions.eq("descricao", descricao));
			}

			if (codigoSetor != null) {
				criteria.add(Restrictions.eq("setor.id", codigoSetor));
			}

			if (codigoMinistro != null) {
				criteria.add(Restrictions.eq("ministro.id", codigoMinistro));
			}

			if (ativo != null) {
				criteria.add(Restrictions.eq("ativo", ativo));
			}

			origem = (OrigemAndamentoDecisao) criteria.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return origem;
	}

	public AndamentoProcesso recuperarUltimoAndamentoProcesso(
			String siglaClasse, Long numero) throws DaoException {
		Session session = retrieveSession();

		AndamentoProcesso andamentoProcesso = null;

		try {

			Criteria criteria = session.createCriteria(AndamentoProcesso.class);

			criteria.add(Restrictions.eq("siglaClasseProcessual", siglaClasse));
			criteria.add(Restrictions.eq("numeroProcessual", numero));
			criteria.addOrder(Order.desc("dataHoraSistema"));

			criteria.setMaxResults(1);

			andamentoProcesso = (AndamentoProcesso) criteria.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return andamentoProcesso;
	}

	public Boolean verificaAndamentoProcesso(String siglaProcessual,
			Long numeroProcessual, Long codigoAndamento) throws DaoException {

		Session session = retrieveSession();
		AndamentoProcesso andamento = null;
		try {

			Criteria criteria = session.createCriteria(AndamentoProcesso.class);

			criteria.add(Restrictions.eq("sigClasseProces",
					siglaProcessual));
			criteria.add(Restrictions.eq("numProcesso", numeroProcessual));
			criteria.add(Restrictions.eq("tipoAndamento.id",
					codigoAndamento));

			andamento = (AndamentoProcesso) criteria.uniqueResult();

			if (andamento != null) {
				return true;
			}
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return false;
	}

	/*
	 * Esse método é utilizado para o IntegradorWS.
	 */

	public List<AndamentoProcesso> pesquisarAndamentoProcesso(
			Long codigoAndamento, Date dataInicial, Date dataFinal)
			throws DaoException {

		Session session = retrieveSession();

		try {
			Criteria criteria = session.createCriteria(AndamentoProcesso.class);
			if (codigoAndamento != null) {
				criteria.add(Restrictions
						.eq("codigoAndamento", codigoAndamento));
			}

			if (dataInicial != null) {
				String dataInicialFormatada = new SimpleDateFormat("dd/MM/yyyy")
						.format(dataInicial);
				Date dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
						.parse(dataInicialFormatada + " 00:00:00");
				criteria.add(Restrictions.ge("dataHoraSistema", dataInicio));
			}

			String dataFinalFormatada;
			if (dataFinal != null) {
				dataFinalFormatada = new SimpleDateFormat("dd/MM/yyyy")
						.format(dataFinal);
				Date datatFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
						.parse(dataFinalFormatada + " 23:59:59");
				criteria.add(Restrictions.le("dataHoraSistema", datatFim));

			}

			List<AndamentoProcesso> andamentoProcesso = criteria.list();

			return andamentoProcesso;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (ParseException e) {
			throw new DaoException("ParseException", e);
		}

	}
	
	public String recuperarObsInterna(Long idAndamentoProcesso) throws DaoException {
		Session session = retrieveSession();
		String sql = "select A.DSC_OBS_INTERNA from STF.ANDAMENTO_PROCESSOS a where A.SEQ_ANDAMENTO_PROCESSO = " + idAndamentoProcesso;
		Query query = session.createSQLQuery(sql);
		return (String) query.uniqueResult();
	}

	public AndamentoProcesso recuperarAndamentoProcesso(Long seqAndamentoProcesso) throws DaoException {

		Session session = retrieveSession();

		try {
			Criteria criteria = session.createCriteria(AndamentoProcesso.class);
			criteria.setMaxResults(1);

			criteria.add(Restrictions.eq("id",seqAndamentoProcesso));

			AndamentoProcesso andamentoProcesso = (AndamentoProcesso) criteria.uniqueResult();

			// System.out.println(andamentoProcesso);
			return andamentoProcesso;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	private Long recuperarUltimaSeqAndamento(String siglaProcessual,
			Long numeroProcessual) throws DaoException {
		Session session = retrieveSession();
		Long ordemAtual = null;
		try {

			String hql = " SELECT MAX(a.idAndamentoProcesso) FROM AndamentoProcesso a"
					+ " WHERE ( 1 = 1 ) AND "
					+ " a.numeroProcessual = "
					+ numeroProcessual
					+ " AND a.siglaClasseProcessual = '"
					+ siglaProcessual + "'";

			ordemAtual = (Long) session.createQuery(hql).uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		if (ordemAtual != null) {
			return ordemAtual;
		} else {
			return 1L;
		}
	}

	public void atualizarAndamentoProcesso(AndamentoProcesso andamentoProcesso)
			throws DaoException {

		Session session = retrieveSession();

		try {
			session.update(andamentoProcesso);
			session.flush();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	public Long recuperarUltimoNumeroSequencia(Processo processo)
			throws DaoException {
		Long seq = null;
		try {
			/*
			 * Session session = retrieveSession();
			 * 
			 * Criteria c = session.createCriteria(AndamentoProcesso.class);
			 * c.add( Restrictions.eq("processo.id.numeroProcessual",
			 * processo.getId().getNumeroProcessual()) ); c.add(
			 * Restrictions.eq("processo.id.siglaClasseProcessual",
			 * processo.getId().getSiglaClasseProcessual()) ); c.setProjection(
			 * Projections.max("numeroSequencia") );
			 * 
			 * seq = (Long) c.uniqueResult();
			 */

			StringBuffer sql = new StringBuffer();
			sql.append(" select max(num_sequencia) as num_seq from stf.andamento_processos ");
			sql.append(" where sig_classe_proces = ? ");
			sql.append(" and num_processo = ? ");

			PreparedStatement stm = retrieveSession().connection().prepareStatement(sql.toString());
			stm.setString(1, processo.getClasseProcessual().getId());
			stm.setLong(2, processo.getNumeroProcessual());

			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				seq = rs.getLong("num_seq");
			}
			stm.close();
			rs.close();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return seq;
	}

	private Long getNextSeq(Connection con, String nomeSeq)
			throws SQLException, HibernateException, DaoException {
		StringBuffer sql = new StringBuffer(" select " + nomeSeq
				+ ".NEXTVAL from dual ");
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery(sql.toString());
		Long nextVal = null;
		if (rs.next()) {
			nextVal = rs.getLong("NEXTVAL");
		}
		stm.close();
		rs.close();
		return nextVal;
	}

	@Override
	public AndamentoProcesso incluir(AndamentoProcesso andamentoProcesso) throws DaoException {
		try {

			Connection con = retrieveSession().connection();
			Long origemDecisao = null;

			StringBuffer sql = new StringBuffer();

			if (andamentoProcesso.getOrigemAndamentoDecisao() != null) {
				sql.append(" insert into stf.andamento_processos (cod_andamento, dat_andamento, dat_hora_sistema, dsc_obser_and, num_sequencia, cod_setor, cod_usuario, seq_andamento_processo, seq_objeto_incidente, seq_origem_decisao) ");
				sql.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ");
			} else {
				sql.append(" insert into stf.andamento_processos (cod_andamento, dat_andamento, dat_hora_sistema, dsc_obser_and, num_sequencia, cod_setor, cod_usuario, seq_andamento_processo, seq_objeto_incidente) ");
				sql.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?) ");
			}

			PreparedStatement ps = retrieveSession().connection().prepareStatement(sql.toString());
			java.sql.Date dataAtual = new java.sql.Date(new java.util.Date().getTime());
			ps.setLong(1, andamentoProcesso.getCodigoAndamento());
			ps.setDate(2, dataAtual);
			ps.setDate(3, dataAtual);
			ps.setString(4, andamentoProcesso.getDescricaoObservacaoAndamento());
			ps.setLong(5, andamentoProcesso.getNumeroSequencia());
			ps.setLong(6, andamentoProcesso.getSetor().getId());
			ps.setString(7, andamentoProcesso.getCodigoUsuario().toUpperCase());
			Long seqAndamentoProcesso = getNextSeq(con, "stf.seq_andamento_processo");
			ps.setLong(8, seqAndamentoProcesso);
			ps.setLong(9, andamentoProcesso.getObjetoIncidente().getId());
			if (andamentoProcesso.getOrigemAndamentoDecisao() != null) {
				origemDecisao = andamentoProcesso.getOrigemAndamentoDecisao().getId();
				ps.setLong(10, origemDecisao);
			}
			ps.executeUpdate();

			ps.close();

			andamentoProcesso.setId(seqAndamentoProcesso);

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return andamentoProcesso;
	}


	public Long recuperarUltimoNumeroSequencia(ObjetoIncidente objetoIncidente)
			throws DaoException {
		Long seq = null;
		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT MAX(ap.numeroSequencia) ");
			hql.append("   FROM AndamentoProcesso ap, ObjetoIncidente oi ");
			hql.append("  WHERE ap.objetoIncidente.principal.id = oi.principal.id ");
			hql.append("    AND oi.id = :oiid ");

			Query c = session.createQuery(hql.toString());
			c.setLong("oiid", objetoIncidente.getId());

			/*
			 * Criteria c = session.createCriteria(AndamentoProcesso.class);
			 * c.add(Restrictions.eq("objetoIncidente.id", processo.getId()));
			 * c.setProjection(Projections.max("numeroSequencia"));
			 */

			seq = (Long) c.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return seq;
	}

	public AndamentoProcesso recuperarUltimoAndamento(Processo processo)
			throws DaoException {
		AndamentoProcesso ap = null;
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ap ");
			hql.append("   FROM AndamentoProcesso ap ");
			hql.append("  WHERE ap.objetoIncidente.principal.id = :processo ");
			hql.append("    AND ap.ultimoAndamento = :sim ");
			hql.append("    AND ap.lancamentoIndevido = :nao ");

			Query q = session.createQuery(hql.toString());
			q.setLong("processo", processo.getId());
			q.setString("sim", "S");
			q.setString("nao", "N");

			ap = (AndamentoProcesso) q.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return ap;
	}

	/* Verifica se o Processo possui o andamento e ele não seja indevido */
	public Boolean verificarAndamentoProcessoNaoIndevido(String siglaProcessual, Long numeroProcessual, Long codigoAndamento) throws DaoException {

		Session session = retrieveSession();
		AndamentoProcesso andamento = null;
		final String naoEhLancamentoIndevido = "S";

		try {

			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ap ");
			hql.append("   FROM AndamentoProcesso ap ");
			hql.append("  WHERE ap.codigoAndamento = :codigoAndamento ");
			hql.append("    AND ap.lancamentoIndevido <> :naoEhLancamentoIndevido ");
			hql.append("    AND ap.sigClasseProces = :siglaProcessual ");
			hql.append("    AND ap.numProcesso = :numeroProcessual ");

			Query q = session.createQuery(hql.toString());

			q.setLong("codigoAndamento", codigoAndamento);
			q.setString("siglaProcessual", siglaProcessual);
			q.setLong("numeroProcessual", numeroProcessual);
			q.setString("naoEhLancamentoIndevido", naoEhLancamentoIndevido);
			
			if (q.list() == null){
				return false;
			}
			if (q.list().size() == 0){
				return false;
			}
			andamento = (AndamentoProcesso) q.list().get(0);

//			andamento = (AndamentoProcesso) q.uniqueResult();

			if (andamento != null) {
				return true;
			}
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return false;
	}
	
	

	@Override
	public Long recuperarQuantidadeAndamentoProcesso(String siglaProcessual,
			Long numeroProcessual, Long codigoAndamento,
			Boolean incluirIndevidos) throws DaoException {
		
		Session session = retrieveSession();
				
		try {

			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT COUNT(ap) ");
			hql.append("   FROM AndamentoProcesso ap ");
			hql.append("  WHERE ap.codigoAndamento = :codigoAndamento ");			
			hql.append("    AND ap.sigClasseProces = :siglaProcessual ");
			hql.append("    AND ap.numProcesso = :numeroProcessual ");
			if (!incluirIndevidos)
				hql.append("    AND ap.lancamentoIndevido <> :lancamentoIndevido ");

			Query q = session.createQuery(hql.toString());

			q.setLong("codigoAndamento", codigoAndamento);
			q.setString("siglaProcessual", siglaProcessual);
			q.setLong("numeroProcessual", numeroProcessual);
			q.setString("lancamentoIndevido", "S");
			
			q.list();
			return (Long)q.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}		
	}

	public boolean possuiAndamentoJulgamentoOuMerito(Processo processo) throws DaoException {

		List<Long> andamentos = new ArrayList<Long>();
		andamentos.add(Andamento.Andamentos.SUBSTITUIDO_JULGAMENTO_REPERCUSSAO_GERAL.getId());
		andamentos.add(Andamento.Andamentos.MERITO_REPERCUSSAO_GERAL.getId());
		
		Session session = retrieveSession();
		String sql = "select count(*) from stf.andamento_processos ap where ap.SIG_CLASSE_PROCES = :sigla and ap.NUM_PROCESSO = :numero and " +
		"ap.FLG_LANCAMENTO_INDEVIDO = 'N' and ap.COD_ANDAMENTO in (:andamentos)";
		
		Query query = session.createSQLQuery(sql);
		query.setString("sigla", processo.getSiglaClasseProcessual());
		query.setLong("numero", processo.getNumeroProcessual());
		query.setParameterList("andamentos", andamentos);
		
		Number count = (Number)query.uniqueResult();
		
		return count.longValue() > 0;
	}

	@Override
	public boolean isLancadoPorDispositivo(AndamentoProcesso andamentoProcesso) throws DaoException {
		
		Session session = retrieveSession();
		String sql = "select count(*) from stf.andamento_processos ap, judiciario.lancamento_dispositivo ld where ap.seq_andamento_processo = ld.seq_andamento_processo " +
					 "and ap.seq_andamento_processo = :idAndamentoProcesso";
		
		Query query = session.createSQLQuery(sql);
		query.setLong("idAndamentoProcesso", andamentoProcesso.getId());

		Number count = (Number)query.uniqueResult();
		
		return count.longValue() > 0;
	}

	@Override
	public List<AndamentoProcesso> pesquisarAvisosNaoCriados(Long andamento,
			String observacao, Boolean processoOriginario, Date dataInicial,
			Date dataFinal, Boolean andamentoExpedito, String siglaProcesso,
			Long numProcesso) throws DaoException {
		List<AndamentoProcesso> listaAndamentoProcesso = new ArrayList<AndamentoProcesso>();
		try{
			Session session = retrieveSession();
			StringBuffer sql = new StringBuffer();
			
			sql.append("SELECT ap.seq_andamento_processo " +
						"FROM STF.ANDAMENTO_PROCESSOS ap, " +
							"(SELECT o.seq_objeto_incidente, o.seq_objeto_incidente_principal " +
							" FROM judiciario.processo p1, judiciario.objeto_incidente o " +
							" WHERE tip_meio_processo = 'E' " +
							" AND o.seq_objeto_incidente_principal = p1.seq_objeto_incidente ) o " +
						"WHERE ap.seq_objeto_incidente = o.seq_objeto_incidente " +
						"AND ap.flg_lancamento_indevido = 'N' " +
						"AND ap.seq_andamento_processo NOT IN " +
								"(SELECT pi.seq_andamento_processo FROM estf.processo_integracao pi) ");

			if(dataInicial != null && dataFinal != null){
				if (dataFinal.after(dataInicial) || dataFinal.equals(dataInicial)) {
					sql.append("AND ap.DAT_ANDAMENTO >= ");
					sql.append(" to_date('"+(new SimpleDateFormat("dd/MM/yyyy").format(dataInicial).toString() + " 00:00:00','DD/MM/YYYY HH24:MI:SS') "));
					sql.append("AND ap.DAT_ANDAMENTO <= ");
					sql.append(" to_date('" + (new SimpleDateFormat("dd/MM/yyyy").format(dataFinal).toString() + " 23:59:59','DD/MM/YYYY HH24:MI:SS') "));
				}
			}
			
			if(processoOriginario != null ){
				if(processoOriginario){
					sql.append("AND ap.SIG_CLASSE_PROCES IN ('ARE','RE','AI') ");
				}else{
					sql.append("AND ap.SIG_CLASSE_PROCES NOT IN ('ARE','RE','AI') ");
				}
			}else if((siglaProcesso != null && !siglaProcesso.trim().isEmpty()) && (numProcesso != null && numProcesso > 0L)){
				sql.append("AND upper(ap.SIG_CLASSE_PROCES) = upper(:siglaProcesso) ");
				sql.append("AND ap.NUM_PROCESSO = :numProcesso ");
			}
			
			if(observacao != null && !observacao.isEmpty() ){
				sql.append("AND upper(ap.DSC_OBSER_AND) LIKE upper('%" + observacao + "%') ");
			}
			
			if((andamento != null && andamento > 0)  && (andamentoExpedito == null || !andamentoExpedito)){
				sql.append("AND ap.COD_ANDAMENTO = :andamento ");
			}else if(andamentoExpedito == null || !andamentoExpedito){
				sql.append("AND ap.COD_ANDAMENTO IN (7002, 7101, 7104, 7108)  ");
			}
			
			if(andamentoExpedito != null && andamentoExpedito){
				sql.append("AND ap.COD_ANDAMENTO IN (7101, 7104, 7108) ");
				sql.append("AND ap.FLG_ULTIMO_ANDAMENTO = 'N' ");
				sql.append("AND EXISTS " +
									"(SELECT NULL " +
									 "FROM STF.ANDAMENTO_PROCESSOS ap2, " +
									 	  "JUDICIARIO.OBJETO_INCIDENTE o2 " +
									 "WHERE ap2.seq_objeto_incidente = o2.seq_objeto_incidente " +
									 "AND o2.seq_objeto_incidente_principal = o.seq_objeto_incidente_principal " +
									 "AND ap2.cod_andamento IN (7315, 7317) " +
									 "AND ap2.flg_lancamento_indevido = 'N' " +
									 "AND ap2.dat_andamento > ap.dat_andamento ) ");

			}
			
			sql.append("AND ROWNUM < 1001");
			
			Query q = session.createSQLQuery(sql.toString());
			
			if((andamento != null && andamento > 0)  && (andamentoExpedito == null || !andamentoExpedito)){
				q.setLong("andamento", andamento);
			}
			
			if((siglaProcesso != null && !siglaProcesso.trim().isEmpty()) && (numProcesso != null && numProcesso > 0L)){
				q.setString("siglaProcesso", siglaProcesso);
				q.setLong("numProcesso", numProcesso);
			}
			
			List<Long> listaId = q.list();
			
			if(listaId.size() >= 1){
				StringBuffer hql = new StringBuffer("SELECT ap FROM AndamentoProcesso ap WHERE ap.id IN ( :ids ) ");
				Query query = session.createQuery(hql.toString());
				query.setParameterList("ids", listaId);
				listaAndamentoProcesso = query.list();
			}
		}catch(DaoException e){
			throw new DaoException(e);
		}
		
		return listaAndamentoProcesso;
	}

	@Override
	public AndamentoProcesso recuperarUltimoAndamentoSelecionado(
			Long numeroProcesso, String classeProcesso, Long codigoAndamento)
			throws DaoException {
		
		Session session = retrieveSession();
		List<AndamentoProcesso> andamentos = null;
		
		try {
			Criteria criteria = session.createCriteria(AndamentoProcesso.class);

			criteria.add(Restrictions.eq("sigClasseProces", classeProcesso));
			criteria.add(Restrictions.eq("numProcesso", numeroProcesso));
			criteria.add(Restrictions.eq("tipoAndamento.id",
					codigoAndamento));
			criteria.addOrder(Order.desc("numeroSequencia"));

			andamentos = criteria.list();
			
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return andamentos.get(0);

	}
	
	public AndamentoProcesso recuperarUltimoAndamentoSelecionadoData(Long idProcesso, Long codigoAndamento, Date dataInicial, Date dataFinal)
			throws DaoException {
		
		Session session = retrieveSession();
		List<AndamentoProcesso> andamentos = null;
		
		try {
			Criteria criteria = session.createCriteria(AndamentoProcesso.class);

			if (dataInicial != null) {
				String dataInicialFormatada = new SimpleDateFormat("dd/MM/yyyy")
						.format(dataInicial);
				Date dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
						.parse(dataInicialFormatada + " 00:00:00");
				criteria.add(Restrictions.ge("dataHoraSistema", dataInicio));
			}

			String dataFinalFormatada;
			if (dataFinal != null) {
				dataFinalFormatada = new SimpleDateFormat("dd/MM/yyyy")
						.format(dataFinal);
				Date datatFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
						.parse(dataFinalFormatada + " 23:59:59");
				criteria.add(Restrictions.le("dataHoraSistema", datatFim));

			}
			
			
			criteria.add(Restrictions.eq("objetoIncidente.id", idProcesso));
			criteria.add(Restrictions.eq("tipoAndamento.id",
					codigoAndamento));
			criteria.addOrder(Order.desc("numeroSequencia"));

			andamentos = criteria.list();
			
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!andamentos.isEmpty()) {
			return andamentos.get(0);
		}else {
			return null;
		}

	}
	
	@Override
	public void alterarAndamento(AndamentoProcesso ap) throws DaoException {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE STF.ANDAMENTO_PROCESSOS ap");
			sql.append(" WHERE ap.id = :id ");
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery( sql.toString() );
	
			sqlQuery.setLong("id", ap.getId());
			
			sqlQuery.executeUpdate();
			
			retrieveSession().clear();

		} catch ( Exception e ) {
			throw new DaoException(e);
		}
	
	}
	
	@Override
	public void alterarObsAndamento(Long seqAndamento, String obs) throws DaoException {
		try {
			if(seqAndamento != null) {
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE STF.ANDAMENTO_PROCESSOS ap SET DSC_OBSER_AND = :obs ");
			sql.append(" WHERE ap.SEQ_ANDAMENTO_PROCESSO = :id ");
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery( sql.toString() );
	
			sqlQuery.setLong("id", seqAndamento);
			sqlQuery.setString("obs", obs);
			sqlQuery.executeUpdate();
			
			retrieveSession().clear();
			}

		} catch ( Exception e ) {
			throw new DaoException(e);
		}
	
	}
	
	public Long recuperarCodAndamentoPorNumeroSequencia(Processo processo, Long numeroSequenciaErrado) throws DaoException {
		Long codAndamento = null;
		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ap.codigoAndamento ");
			hql.append("   FROM AndamentoProcesso ap ");
			hql.append("  WHERE ap.objetoIncidente.principal.id = :processo ");
			hql.append("    AND ap.numeroSequencia = :numeroSequenciaErrado ");

			Query c = session.createQuery(hql.toString());
			c.setLong("processo", processo.getId());
			c.setLong("numeroSequenciaErrado", numeroSequenciaErrado);
		
			codAndamento = (Long) c.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		if (codAndamento != null) {
			return codAndamento;
		} else {
			return 0L;
		}

	}

	@Override
	public List<AndamentoProcesso> recuperarTodosAndamentos(ObjetoIncidente<?> referendo) throws DaoException {
		List<AndamentoProcesso> andamentos = new ArrayList<AndamentoProcesso>();

		Criteria criteria = retrieveSession().createCriteria(AndamentoProcesso.class);
		criteria.add(Restrictions.eq("objetoIncidente", referendo));
		andamentos = criteria.list();

		return andamentos;
	}
}

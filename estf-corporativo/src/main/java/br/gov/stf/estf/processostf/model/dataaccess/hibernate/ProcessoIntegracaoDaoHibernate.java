package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoIntegracao;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoIntegracaoDao;
import br.gov.stf.estf.processostf.model.util.ProcessoIntegracaoEnum;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ProcessoIntegracaoDaoHibernate extends
		GenericHibernateDao<ProcessoIntegracao, Long> implements
		ProcessoIntegracaoDao {
	public ProcessoIntegracaoDaoHibernate() {
		super(ProcessoIntegracao.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public ProcessoIntegracao pesquisar(Long idAndamentoProcesso,
			String classeProcesso, Long numeroProcesso) throws DaoException {

		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(ProcessoIntegracao.class);
		criteria.add(Restrictions
				.eq("idAndamentoProcesso", idAndamentoProcesso));
		criteria.add(Restrictions.eq("siglaClasseProcesso", classeProcesso));
		criteria.add(Restrictions.eq("numeroProcesso", numeroProcesso));

		List<ProcessoIntegracao> processosIntegracao = criteria.list();

		if (processosIntegracao.size() > 1)
			throw new DaoException(
					"Mais de um processo integração foi encontrado para o andamento "
							+ idAndamentoProcesso + " / " + classeProcesso
							+ " " + numeroProcesso);

		return processosIntegracao == null || processosIntegracao.size() == 0 ? null
				: processosIntegracao.get(0);
	}

	@Override
	public void excluir(ProcessoIntegracao processoIntegracao)
			throws DaoException {

		if (processoIntegracao == null || processoIntegracao.getId() == null) {
			throw new DaoException("Não é possível com o objeto nulo.");
		}
		try {
			Session session = retrieveSession();
			String sql;
			/** Excluir os filhos **/
			excluirFilhoLOG(processoIntegracao);
			excluirFilhoParametro(processoIntegracao);

			/** Excluir o pai **/
			
			sql = "DELETE FROM ESTF.processo_integracao lpi where lpi.seq_processo_integracao = :seqProcessoIntegracao";
			Query query = session.createSQLQuery(sql.toString());
			query.setLong("seqProcessoIntegracao", processoIntegracao.getId());
			query.executeUpdate();
			
			excluirProcessoIntegracaoUsuarios(processoIntegracao);

			
		} catch (HibernateException e) {
			throw new DaoException(e);
		}
	}
	
	public Long pesquisarQtdAvisosLidos(Integer seqUsuarioExterno)
			throws DaoException {
		Session session = retrieveSession();
		StringBuilder sql = new StringBuilder();
		Long total;
		
		try
		{		
			sql.append("SELECT count(*) from estf.processo_integracao pi	where pi.COD_ORGAO in ( ");
			sql.append(" SELECT cod_origem from ESTF.USUARIO_EXTERNO_ORIGEM a ");
			sql.append(" where a.SEQ_USUARIO_EXTERNO = :seqUsuarioExterno) ");
			sql.append("  and pi.COD_TIPO_COMUNICACAO in (101,102,103) ");
			sql.append("  and pi.SEQ_PROCESSO_INTEGRACAO not in (" );
			sql.append(" SELECT piu.SEQ_PROCESSO_INTEGRACAO FROM ESTF.PROCESSO_INTEGRACAO_USUARIO piu ");
			sql.append(" where piu.seq_processo_integracao = pi.seq_processo_integracao ) ");
			
			Query q = session.createSQLQuery(sql.toString());
			q.setLong("seqUsuarioExterno", seqUsuarioExterno);
	
			total = NumberUtils.createLong(q.uniqueResult().toString());
		
		}
		catch (Exception e)
		{
			throw new DaoException(e);
		}	
		return total;
	}
	
	
	private void excluirFilhoLOG(ProcessoIntegracao processoIntegracao)
			throws DaoException {
		Session session = retrieveSession();
		String sql;
		sql = "DELETE FROM ESTF.log_processo_integracao lpi where lpi.seq_processo_integracao = :seqProcessoIntegracao";
		Query query = session.createSQLQuery(sql);
		query.setLong("seqProcessoIntegracao", processoIntegracao.getId());
		query.executeUpdate();
	}
	
	private void excluirFilhoParametro(ProcessoIntegracao processoIntegracao)
			throws DaoException {
		Session session = retrieveSession();
		String sql;
		sql = "DELETE FROM ESTF.parametro_integracao pi where pi.seq_processo_integracao = :seqProcessoIntegracao";
		Query query = session.createSQLQuery(sql);
		query.setLong("seqProcessoIntegracao", processoIntegracao.getId());
		query.executeUpdate();
	}

	@Override
	public List<ProcessoIntegracao> pesquisar(Integer codOrgao, String tipoSituacao,
			Date dataInicial, Date dataFinal, Integer numProcesso,
			String siglaProcesso, Integer... tipoComunicacao) throws DaoException {
		List<ProcessoIntegracao> processoIntegracao = new ArrayList<ProcessoIntegracao>();
		Session session = retrieveSession();

		try {
			StringBuffer sql = new StringBuffer();

			sql.append("SELECT pi.SEQ_PROCESSO_INTEGRACAO "
					+ "FROM ESTF.processo_integracao pi, "
					+ "STF.andamento_processos ap, "
					+ "JUDICIARIO.processo p, "
					+ "ESTF.usuario_externo_origem ueo "
					+ "WHERE p.TIP_MEIO_PROCESSO = 'E' "
					+ "AND ap.SEQ_OBJETO_INCIDENTE = p.SEQ_OBJETO_INCIDENTE "
					+ "AND p.SEQ_OBJETO_INCIDENTE = pi.SEQ_OBJETO_INCIDENTE "
					+ "AND ap.SEQ_ANDAMENTO_PROCESSO = pi.SEQ_ANDAMENTO_PROCESSO "
					+ "AND pi.COD_ORGAO = ueo.COD_ORIGEM(+) ");

			if (codOrgao != null && codOrgao > 0) {
				sql.append("AND pi.COD_ORGAO IN "
						+ "(SELECT ueo.COD_ORIGEM FROM ESTF.usuario_externo_origem ueo "
						+ "WHERE ueo.SEQ_USUARIO_EXTERNO = :codOrgao) ");

				if (tipoSituacao != null) {
					if (tipoSituacao.equals(ProcessoIntegracaoEnum.ENVIADO
							.getCodigo())) {
						sql.append("AND pi.SEQ_PROCESSO_INTEGRACAO IN "
								+ "(SELECT piu.SEQ_PROCESSO_INTEGRACAO "
								+ "FROM ESTF.PROCESSO_INTEGRACAO_USUARIO piu "
								+ "WHERE piu.SEQ_USUARIO_EXTERNO = :usuarioExterno )");
					}

					if (tipoSituacao.equals(ProcessoIntegracaoEnum.NAO_ENVIADO.getCodigo())) {
						sql.append("AND pi.SEQ_PROCESSO_INTEGRACAO NOT IN "
								+ "(SELECT piu.SEQ_PROCESSO_INTEGRACAO "
								+ "FROM ESTF.PROCESSO_INTEGRACAO_USUARIO piu "
								+ "WHERE piu.SEQ_USUARIO_EXTERNO = :usuarioExterno )");
					}

				}
			}
			else {
				if (tipoSituacao != null) {
					if (tipoSituacao.equals(ProcessoIntegracaoEnum.ENVIADO
							.getCodigo())) {
						sql.append("AND pi.SEQ_PROCESSO_INTEGRACAO IN "
								+ "(SELECT piu.SEQ_PROCESSO_INTEGRACAO "
								+ "FROM ESTF.PROCESSO_INTEGRACAO_USUARIO piu )");
					}
					if (tipoSituacao.equals(ProcessoIntegracaoEnum.NAO_ENVIADO
							.getCodigo())) {
						sql.append("AND pi.SEQ_PROCESSO_INTEGRACAO NOT IN "
								+ "(SELECT piu.SEQ_PROCESSO_INTEGRACAO "
								+ "FROM ESTF.PROCESSO_INTEGRACAO_USUARIO piu )");
					}
					
					if (tipoSituacao.equals(ProcessoIntegracaoEnum.TODOS
							.getCodigo())) {
						sql.append("AND 1=1 ");
					}
				}
			}

			if (dataInicial != null && dataFinal != null) {
				if (dataFinal.after(dataInicial)
						|| dataFinal.equals(dataInicial)) {
					sql.append("AND pi.DAT_INCLUSAO >= ");
					sql.append(" to_date('"
							+ (new SimpleDateFormat("dd/MM/yyyy").format(
									dataInicial).toString() + " 00:00:00','DD/MM/YYYY HH24:MI:SS') "));
					sql.append("AND pi.DAT_INCLUSAO <= ");
					sql.append(" to_date('"
							+ (new SimpleDateFormat("dd/MM/yyyy").format(
									dataFinal).toString() + " 23:59:59','DD/MM/YYYY HH24:MI:SS') "));
				}
			}

			if (tipoComunicacao != null && tipoComunicacao.length > 0) {
				sql.append("AND pi.COD_TIPO_COMUNICACAO IN ( :tipoComunicacao )");
			}

			if ((siglaProcesso != null && !siglaProcesso.isEmpty())
					&& (numProcesso != null && numProcesso > 0)) {
				sql.append("AND upper(pi.SIG_CLASSE_PROCES) = upper(:siglaProcesso) ");
				sql.append("and pi.NUM_PROCESSO = :numProcesso ");
			}
						
			sql.append(" AND ROWNUM < 1001 ");

			Query q = session.createSQLQuery(sql.toString());

			if (codOrgao != null && codOrgao > 0) {
				q.setInteger("codOrgao", codOrgao);

				if (tipoSituacao != null
						&& !tipoSituacao.equals(ProcessoIntegracaoEnum.TODOS
								.getCodigo())) {
					q.setInteger("usuarioExterno", codOrgao);
				}
			}

			if (tipoComunicacao != null && tipoComunicacao.length > 0) {
				q.setParameterList("tipoComunicacao", tipoComunicacao);
			}

			if ((siglaProcesso != null && !siglaProcesso.isEmpty())
					&& (numProcesso != null && numProcesso > 0)) {
				q.setString("siglaProcesso", siglaProcesso);
				q.setInteger("numProcesso", numProcesso);
			}

			List<Long> listaId = q.list();
			
			if(listaId.size() >= 1){
				StringBuffer hql = new StringBuffer();
				hql.append("SELECT pi FROM ProcessoIntegracao pi WHERE pi.id IN ( :ids )");
				
				Query query = session.createQuery(hql.toString());
				query.setParameterList("ids", listaId);
				processoIntegracao = query.list();
			}
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return processoIntegracao;
	}

	@Override
	public boolean isAvisoLido(AndamentoProcesso andamentoProcesso) throws DaoException {

		Session session = retrieveSession();
		String sql = new String();
	
		sql = "select count(*) from ESTF.PROCESSO_INTEGRACAO_USUARIO piu where PIU.SEQ_PROCESSO_INTEGRACAO in (" +
        	  "SELECT PI.SEQ_PROCESSO_INTEGRACAO as c FROM estf.processo_integracao pi where PI.SEQ_ANDAMENTO_PROCESSO = :idAndamentoProcesso)";

		Query q = session.createSQLQuery(sql);
		q.setLong("idAndamentoProcesso", andamentoProcesso.getId());

		List countAviso = q.list();
		
		return countAviso != null && ((java.math.BigDecimal)countAviso.get(0)).longValue() > 0;
	}

	@Override
	public List<ProcessoIntegracao> pesquisar(AndamentoProcesso andamentoProcesso) throws DaoException {
		
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(ProcessoIntegracao.class);
		criteria.add(Restrictions.eq("idAndamentoProcesso", andamentoProcesso.getId()));

		List<ProcessoIntegracao> processosIntegracao = criteria.list();

		return processosIntegracao;
	}

	@Override
	public void inserirProcessoIntegracaoUsuario(ProcessoIntegracao processoIntegracao, Long usuarioExternoESTF)
			throws DaoException {
		try{
			Session session = retrieveSession();
			String sql = "INSERT INTO ESTF.PROCESSO_INTEGRACAO_USUARIO ( SEQ_USUARIO_EXTERNO, SEQ_PROCESSO_INTEGRACAO) values (?, ?) ";
			Query query = session.createSQLQuery(sql);
			query.setLong(0, usuarioExternoESTF );
			query.setLong(1, processoIntegracao.getId());
			
			query.executeUpdate();
		} catch (HibernateException e) {
				throw new DaoException(e);
		}
	}
	
	@Override
	public void inserirProcessoIntegracaoLog(ProcessoIntegracao processoIntegracao, String descricao) throws DaoException{
			try{
				Session session = retrieveSession();
				
				Query q = session.createSQLQuery("SELECT ESTF.SEQ_LOG_PROCESSO_INTEGRACAO.NEXTVAL FROM DUAL");
				Long seqLog = Long.parseLong(q.uniqueResult().toString());
				
				String sql = "INSERT INTO ESTF.LOG_PROCESSO_INTEGRACAO(SEQ_LOG_PROCESSO_INTEGRACAO, DSC_LOG_PROCESSO_INTEGRACAO,DAT_LOG_PROCESSO_INTEGRACAO, SEQ_PROCESSO_INTEGRACAO) values (?,?,?,?)";
				Query query = session.createSQLQuery(sql);
				query.setLong(0, seqLog);
				query.setString(1, descricao);
				query.setDate(2, new Date());
				query.setLong(3, processoIntegracao.getId());
				
				query.executeUpdate();
			} catch (HibernateException e) {
					throw new DaoException(e);
			}
	}

	@Override
	public void chamaPrcPreparaInterop(ObjetoIncidente<?> objetoIncidente)
			throws DaoException {
		try{
			Session session = retrieveSession();
			SessionImplementor sessao = (SessionImplementor) session;
			
			CallableStatement stmt = sessao.getBatcher().prepareCallableStatement("{call JUDICIARIO.PRC_PREPARA_INTEROP( ? )}");
			
			stmt.setLong(1, objetoIncidente.getId());			
			
			stmt.execute();
			
			sessao.getBatcher().closeStatement(stmt);
			
		}catch (Exception e) {
			throw new DaoException(e.getLocalizedMessage(), e);
		}
		
	}
	
    public void excluirAvisosAndamentoDeBaixa(Long IdAndamentoProcesso) throws DaoException{
        Session session = retrieveSession();
        //exclui da processo integração
        String sql = "DELETE FROM ESTF.processo_integracao WHERE SEQ_ANDAMENTO_PROCESSO = :id";
        Query query = session.createSQLQuery(sql);
        query.setLong("id", IdAndamentoProcesso);
        
        query.executeUpdate();
    }

    public void excluirProcessoIntegracaoUsuarios(ProcessoIntegracao pi) throws DaoException{
        Session session = retrieveSession();
        //exclui da processo integração
        String sql = "DELETE FROM ESTF.PROCESSO_INTEGRACAO_USUARIO WHERE SEQ_PROCESSO_INTEGRACAO = :id";
        Query query = session.createSQLQuery(sql);
        query.setLong("id", pi.getId());
        
        query.executeUpdate();
    }

    public void excluirParametroIntegracao(ProcessoIntegracao pi) throws DaoException{
        Session session = retrieveSession();
        //exclui da processo integração
        String sql = "DELETE FROM ESTF.PARAMETRO_INTEGRACAO WHERE SEQ_PROCESSO_INTEGRACAO = :id";
        Query query = session.createSQLQuery(sql);
        query.setLong("id", pi.getId());
        
        query.executeUpdate();
    }

	
}

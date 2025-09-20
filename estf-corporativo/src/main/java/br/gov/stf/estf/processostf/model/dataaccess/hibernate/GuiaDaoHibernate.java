package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.EnderecoDestinatario;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.GuiaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class GuiaDaoHibernate extends GenericHibernateDao<Guia, Guia.GuiaId> implements GuiaDao {
	public GuiaDaoHibernate() {
		super(Guia.class);
	}

	private static final long serialVersionUID = 1L;
	
	
	public List<Guia> recuperarGuia(Guia guia) throws DaoException {
		List<Guia> guias = null;

		try {
			Session session = retrieveSession();

			Criteria criteria = session.createCriteria(Guia.class);
			if (guia.getCodigoOrgaoDestino() != null && guia.getCodigoOrgaoDestino() != 0) {
				criteria.add(Restrictions.eq("codigoOrgaoDestino", guia.getCodigoOrgaoDestino()));
			}
			if (guia.getId().getNumeroGuia() != null) {
			    criteria.add(Restrictions.eq("id.numeroGuia", guia.getId().getNumeroGuia()));
			}
			if (guia.getId().getAnoGuia() != null) {
			    criteria.add(Restrictions.eq("id.anoGuia", guia.getId().getAnoGuia()));
			}
			if (guia.getDataRemessa() != null) {
			    criteria.add(Restrictions.eq("dataRemessa", guia.getDataRemessa()));
			}

			guias = criteria.list();
			

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return guias;
		
	}
	
	public List<Guia> recuperarGuia(Guia guia, Processo processo, boolean todosProcessos) throws DaoException {
		List<Guia> guias = null;

		try {
			Session session = retrieveSession();
			StringBuilder hql = new StringBuilder();

			hql.append("select distinct g from Guia g, DeslocaProcesso d ");
			hql.append("where g.id.anoGuia = d.guia.id.anoGuia ");
			hql.append("and g.id.codigoOrgaoOrigem = d.guia.id.codigoOrgaoOrigem ");
			hql.append("and g.id.numeroGuia = d.guia.id.numeroGuia");
			if (guia.getId().getNumeroGuia() != null) {
				hql.append(" and g.id.numeroGuia = :numeroGuia"); 
			}
			if (guia.getId().getAnoGuia() != null) {
				hql.append(" and g.id.anoGuia = :anoGuia"); 
			}
			
			///
			if (guia.getId().getCodigoOrgaoOrigem() != null) {
				hql.append(" and g.id.codigoOrgaoOrigem = :codigoOrgaoOrigem"); 
			}
			
			///
			if (guia.getDataRemessa() != null) {
				//hql.append(" and g.dataRemessa = :dataRemessa");
				hql.append(" and TO_CHAR(g.dataRemessa, 'dd-MM-yyyy') = TO_CHAR( :dataRemessa, 'dd-MM-yyyy')"); 
			}
			if (guia.getCodigoOrgaoDestino() != null) {
				hql.append(" and g.codigoOrgaoDestino = :codigoOrgaoDestino"); 
			}
			if (processo != null) {
				if (processo.getNumeroProcessual() != null) {
					hql.append(" and d.id.processo.numeroProcessual = :numeroProcesso"); 
				}
				if (processo.getSiglaClasseProcessual() != null) {
					hql.append(" and d.id.processo.classeProcessual = :classeProcesso"); 
				}
			}
			// se todosProcessos, então não deve restringir para somente os pendentes.
			if (todosProcessos == false) {
				hql.append(" and d.dataRecebimento is null"); 
			}
			Query query = session.createQuery(hql.toString());

			if (guia.getId().getNumeroGuia() != null) {
				query.setLong("numeroGuia",guia.getId().getNumeroGuia());
			}
			if (guia.getId().getAnoGuia() != null) {
				query.setShort("anoGuia", guia.getId().getAnoGuia());
			}
			if (guia.getDataRemessa() != null) {
				query.setDate("dataRemessa", guia.getDataRemessa());
			}
			if (guia.getCodigoOrgaoDestino() != null){
				query.setLong("codigoOrgaoDestino", guia.getCodigoOrgaoDestino());
			}
			
			///
			if (guia.getId().getCodigoOrgaoOrigem() != null){
				query.setLong("codigoOrgaoOrigem", guia.getId().getCodigoOrgaoOrigem());
			}
			
			////
			if (processo != null) {
				if (processo.getNumeroProcessual() != null) {
					query.setLong("numeroProcesso", processo.getNumeroProcessual());
				}
				if (processo.getSiglaClasseProcessual() != null) {
					query.setString("classeProcesso", processo.getSiglaClasseProcessual());
				}
			}

			guias = query.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return guias;
	}

	@Override
	public List<Guia> recuperarGuia(Guia guia, Peticao peticao, boolean todasPeticoes) throws DaoException {
		List<Guia> guias = null;

		try {
			Session session = retrieveSession();
			StringBuilder hql = new StringBuilder();

			hql.append("select distinct g ");
			hql.append("from Guia g, DeslocaPeticao d ");
			hql.append("where g.id.anoGuia = d.guia.id.anoGuia ");
			hql.append("and g.id.codigoOrgaoOrigem = d.guia.id.codigoOrgaoOrigem ");
			hql.append("and g.id.numeroGuia = d.guia.id.numeroGuia");

			if (guia.getId().getNumeroGuia() != null) {
				hql.append(" and g.id.numeroGuia = :numeroGuia"); 
			}
			if (guia.getId().getAnoGuia() != null) {
				hql.append(" and g.id.anoGuia = :anoGuia"); 
			}
			
			///
			if (guia.getId().getCodigoOrgaoOrigem() != null) {
				hql.append(" and g.id.codigoOrgaoOrigem = :codigoOrgaoOrigem"); 
			}
			
			///
			if (guia.getDataRemessa() != null) {
//				hql.append(" and g.dataRemessa = :dataRemessa"); 
				hql.append(" and TO_CHAR(g.dataRemessa, 'dd-MM-yyyy') = TO_CHAR( :dataRemessa, 'dd-MM-yyyy')"); 
			}
			if (guia.getCodigoOrgaoDestino() != null) {
				hql.append(" and g.codigoOrgaoDestino = :codigoOrgaoDestino"); 
			}
			if (peticao != null) {
				if (peticao.getNumeroPeticao() != null) {
					hql.append(" and d.id.peticao.numeroPeticao = :numeroPeticao"); 
				}
				if (peticao.getAnoPeticao() != null) {
					hql.append(" and d.id.peticao.anoPeticao = :anoPeticao"); 
				}
			}
			// se todosProcessos, então não deve restringir para somente os pendentes.
			if (todasPeticoes == false) {
				hql.append(" and d.dataRecebimento is null"); 
			}
			Query query = session.createQuery(hql.toString());

			if (guia.getId().getNumeroGuia() != null) {
				query.setLong("numeroGuia",guia.getId().getNumeroGuia());
			}
			if (guia.getId().getAnoGuia() != null) {
				query.setShort("anoGuia", guia.getId().getAnoGuia());
			}
			if (guia.getDataRemessa() != null) {
				query.setDate("dataRemessa", guia.getDataRemessa());
			}
			if (guia.getCodigoOrgaoDestino() != null){
				query.setLong("codigoOrgaoDestino", guia.getCodigoOrgaoDestino());
			}
			///
			if (guia.getId().getCodigoOrgaoOrigem() != null){
				query.setLong("codigoOrgaoOrigem", guia.getId().getCodigoOrgaoOrigem());
			}
			
			////
			if (peticao != null) {
				if (peticao.getNumeroPeticao() != null){
					query.setLong("numeroPeticao", peticao.getNumeroPeticao());
				}
				if (peticao.getAnoPeticao() != null){
					query.setShort("anoPeticao", peticao.getAnoPeticao());
				}
			}

			guias = query.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return guias;
	}
	
	/**
	 * Recupera o total de petições de uma guia.
	 * @param guia  - a guia em questão
	 * @return o total de petições de uma guia, se não exister petições na guia (guia de processo), deverá retornar zero.
	 * @throws DaoException
	 * @author RicardoLe
	 */
	@Override
	public Long recuperarTotalPeticao(Guia guia) throws DaoException {
		Long totalPeticoes = null;

		try {
			Session session = retrieveSession();
			StringBuilder hql = new StringBuilder();

			hql.append("select count(*) from Guia g, DeslocaPeticao d ");
			hql.append("where g.id.anoGuia = d.guia.id.anoGuia ");
			hql.append("and g.id.codigoOrgaoOrigem = d.guia.id.codigoOrgaoOrigem ");
			hql.append("and g.id.numeroGuia = d.guia.id.numeroGuia");

			if (guia.getId().getNumeroGuia() != null) {
				hql.append(" and g.id.numeroGuia = :numeroGuia"); 
			}
			if (guia.getId().getAnoGuia() != null) {
				hql.append(" and g.id.anoGuia = :anoGuia"); 
			}
			if (guia.getId().getCodigoOrgaoOrigem() != null) {
				hql.append(" and g.id.codigoOrgaoOrigem = :codigoOrgaoOrigem"); 
			}
			
			Query query = session.createQuery(hql.toString());

			if (guia.getId().getNumeroGuia() != null) {
				query.setLong("numeroGuia",guia.getId().getNumeroGuia());
			}
			if (guia.getId().getAnoGuia() != null) {
				query.setShort("anoGuia", guia.getId().getAnoGuia());
			}
			if (guia.getId().getCodigoOrgaoOrigem() != null){
				query.setLong("codigoOrgaoOrigem", guia.getId().getCodigoOrgaoOrigem());
			}
			totalPeticoes = (Long)query.uniqueResult();
			if (totalPeticoes == null) {
				totalPeticoes = new Long(0);
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return totalPeticoes;
	}
	
	/**
	 * Recupera o total de processos de uma guia.
	 * @param guia  - a guia em questão
	 * @return o total de petições de uma guia, se não exister petições na guia (guia de processo), deverá retornar zero.
	 * @throws DaoException
	 * @author RicardoLe
	 */
	@Override
	public Long recuperarTotalProcesso(Guia guia) throws DaoException {
		Long totalProcessos = null;

		try {
			Session session = retrieveSession();
			StringBuilder hql = new StringBuilder();

			hql.append("select count(*) from Guia g, DeslocaProcesso d ");
			hql.append("where g.id.anoGuia = d.guia.id.anoGuia ");
			hql.append("and g.id.codigoOrgaoOrigem = d.guia.id.codigoOrgaoOrigem ");
			hql.append("and g.id.numeroGuia = d.guia.id.numeroGuia");

			if (guia.getId().getNumeroGuia() != null) {
				hql.append(" and g.id.numeroGuia = :numeroGuia"); 
			}
			if (guia.getId().getAnoGuia() != null) {
				hql.append(" and g.id.anoGuia = :anoGuia"); 
			}
			if (guia.getId().getCodigoOrgaoOrigem() != null) {
				hql.append(" and g.id.codigoOrgaoOrigem = :codigoOrgaoOrigem"); 
			}
			
			Query query = session.createQuery(hql.toString());

			if (guia.getId().getNumeroGuia() != null) {
				query.setLong("numeroGuia",guia.getId().getNumeroGuia());
			}
			if (guia.getId().getAnoGuia() != null) {
				query.setShort("anoGuia", guia.getId().getAnoGuia());
			}
			if (guia.getId().getCodigoOrgaoOrigem() != null){
				query.setLong("codigoOrgaoOrigem", guia.getId().getCodigoOrgaoOrigem());
			}
			totalProcessos = (Long)query.uniqueResult();
			if (totalProcessos == null) {
				totalProcessos = new Long(0);
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return totalProcessos;
	}
	
	
	public Long persistirGuia(Guia guia) throws DaoException {
		
		Session session = retrieveSession();
		
		Long numGuia = recuperarProximoNumeroGuia();
		
		guia.getId().setNumeroGuia(numGuia);

		try {
			session.save(guia);
			session.flush();
			return numGuia;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	public Long recuperarProximoNumeroGuia() throws DaoException {

		Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);

		// Obter o último valor da NUM_GUIA
		String sql = "select STF.SEQ_GUIA_EXT.NEXTVAL from dual";

		Long numGuia = (Long) session.createSQLQuery(sql).addScalar("nextval", Hibernate.LONG).uniqueResult();

		return numGuia;
	}

	public String callDeslocamento(Guia guia, Long codigoSetorUsuario) throws DaoException, PersistenceException {
	    	
		return callDeslocamento(guia, codigoSetorUsuario, false);
	}
	
	@Override
	public String callDeslocamento(Guia guia, Long codigoSetorUsuario, Boolean recebimentoAutomatico) throws DaoException, PersistenceException {
			String numAnoGuia = "";
			try {
				Session session = retrieveSession();
				SessionImplementor sessao = (SessionImplementor) session;
			CallableStatement stmt = sessao.getBatcher().prepareCallableStatement("{call JUDICIARIO.PKG_DESLOCA_INCIDENTE.PRC_DESLOCA_INCIDENTE(?,?,?,?,?,?,?,?)}");

				stmt.setLong("p_cod_orgao_origem", 0);
				stmt.setLong("p_cod_orgao_destino", guia.getCodigoOrgaoDestino());
				stmt.setInt("p_tip_orgao_origem", 0);
				stmt.setInt("p_tip_orgao_destino", guia.getTipoOrgaoDestino());
			stmt.setString("p_deslocamento_automatico", recebimentoAutomatico ? "S" : "N");
				stmt.setString("p_observacao_guia", guia.getObservacao());
				stmt.setLong("p_cod_setor_usuario", codigoSetorUsuario);
				stmt.registerOutParameter("p_num_ano_guia", java.sql.Types.VARCHAR);

				stmt.execute();
				numAnoGuia = stmt.getString("p_num_ano_guia");

				sessao.getBatcher().closeStatement(stmt);
				return numAnoGuia;
			} catch (Exception e) {
				throw new DaoException(e.getMessage(), e);
			}
		}
	
	@Override
	public List<Guia> getListarDocumentosGuia(GuiaId guiaId) throws DaoException {
		Criteria criteria = retrieveSession().createCriteria(Guia.class);
		criteria.add(Restrictions.sqlRestriction("rownum<5"));
		// Criterion restricaoAno = Restrictions.eq("id", guiaId.getAnoGuia());
		// Criterion restricaoNumeroGuia = Restrictions.eq("id",
		// guiaId.getNumeroGuia());
		// LogicalExpression codition = Restrictions.and(restricaoAno,
		// restricaoNumeroGuia);
		// criteria.add(codition);
		return criteria.list();
	}

	@Override
	public void callInserirProcessoPeticaoNaGuia(Guia guia, String tipoObjetoIncidente) throws DaoException, SQLException {
		Session session = retrieveSession();
		SessionImplementor sessao = (SessionImplementor) session;
		CallableStatement stmt = sessao.getBatcher().prepareCallableStatement("{call JUDICIARIO.PKG_DESLOCA_INCIDENTE.PRC_ALTERA_INCIDENTE_GUIA(?,?,?,?,?)}");
		try {
			stmt.setLong("p_num_guia", guia.getId().getNumeroGuia());
			stmt.setShort("p_ano_guia", guia.getId().getAnoGuia());
			stmt.setLong("p_cod_orgao_origem", guia.getId().getCodigoOrgaoOrigem());
			stmt.setString("p_tip_objeto_incidente", tipoObjetoIncidente);
			stmt.setString("p_tip_operacao", "I");

			stmt.execute();
		} catch (Exception e) {
			throw new DaoException(e.getMessage(), e);
		}
		sessao.getBatcher().closeStatement(stmt);
	}

	@Override
	public void callRemoverProcessoPeticaoNaGuia(Guia guia, String tipoObjetoIncidente) throws DaoException, SQLException {
		Session session = retrieveSession();
		SessionImplementor sessao = (SessionImplementor) session;
		CallableStatement stmt = sessao.getBatcher().prepareCallableStatement("{call JUDICIARIO.PKG_DESLOCA_INCIDENTE.PRC_ALTERA_INCIDENTE_GUIA(?,?,?,?,?)}");
		try {
			stmt.setLong("p_num_guia", guia.getId().getNumeroGuia());
			stmt.setShort("p_ano_guia", guia.getId().getAnoGuia());
			stmt.setLong("p_cod_orgao_origem", guia.getId().getCodigoOrgaoOrigem());
			stmt.setString("p_tip_objeto_incidente", tipoObjetoIncidente);
			stmt.setString("p_tip_operacao", "D");
			session.flush();
		} catch (Exception e) {
			throw new DaoException(e.getMessage(), e);
		}

		stmt.execute();
		sessao.getBatcher().closeStatement(stmt);

	}

	@Override
	public void alterarGuia(Guia guia) throws DaoException {
		Session session = retrieveSession();
		try {
			session.saveOrUpdate(guia);
			session.flush();
			session.refresh(guia);
		} catch (Exception e) {
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * Método responsável por gerar uma guia vazia utilizada no deslocamento.
	 * Este método utiliza um procecedure no banco de dados PRC_GERA_GUIA.
	 * 
		CREATE OR REPLACE PROCEDURE JUDICIARIO.prc_gera_guia (p_cod_orgao_origem      stf.guias.cod_orgao_origem%TYPE,
		                                                      p_cod_orgao_destino     stf.guias.cod_orgao_destino%TYPE,
		                                                      p_tip_orgao_origem      stf.guias.tip_orgao_origem%TYPE,
		                                                      p_tip_orgao_destino     stf.guias.tip_orgao_destino%TYPE,
		                                                      p_dsc_observacao        stf.guias.dsc_observacao%TYPE,
		                                                      p_ano_guia out          stf.guias.ano_guia%TYPE,
		                                                      p_num_guia out          stf.guias.num_guia%TYPE
	 * 
	 */
	
	@Override
	public Guia geraGuiaVazia(Guia guia) throws DaoException, PersistenceException {
			Short anoGuia = null;
			Long numGuia = null;
			try {
				
				if (guia.getId() == null) {
					throw new DaoException("Parâmetros incompletos para a geração da guia");
				}
				
				Session session = retrieveSession();
				SessionImplementor sessao = (SessionImplementor) session;
				CallableStatement stmt = sessao.getBatcher().prepareCallableStatement("{call JUDICIARIO.PRC_GERA_GUIA(?,?,?,?,?,?,?)}");

				stmt.setLong("p_cod_orgao_origem", guia.getId().getCodigoOrgaoOrigem());
				stmt.setLong("p_cod_orgao_destino", guia.getCodigoOrgaoDestino());
				stmt.setInt("p_tip_orgao_origem", guia.getTipoOrgaoOrigem());
				stmt.setInt("p_tip_orgao_destino", guia.getTipoOrgaoDestino());
				stmt.setString("p_dsc_observacao", guia.getObservacao());
				stmt.registerOutParameter("p_ano_guia", java.sql.Types.VARCHAR);
				stmt.registerOutParameter("p_num_guia", java.sql.Types.VARCHAR);

				stmt.execute();
				anoGuia = stmt.getShort("p_ano_guia");
				numGuia = stmt.getLong("p_num_guia");
				
				Guia novaGuia = guia;
				novaGuia.getId().setAnoGuia(anoGuia);
				novaGuia.getId().setNumeroGuia(numGuia);

				sessao.getBatcher().closeStatement(stmt);
//				sessao.flush();
				return novaGuia;
			} catch (Exception e) {
				throw new DaoException(e.getMessage(), e);
			}
	}
	
	@Override
	public boolean isEletronico(Guia guia) throws DaoException {
		try {
			Session session = retrieveSession();
			StringBuilder hql = new StringBuilder();

			hql.append("select distinct p.tipoMeioProcesso from DeslocaProcesso d, Processo p, Guia g ");
			hql.append("where g.id.anoGuia = d.guia.id.anoGuia ");
			hql.append("and g.id.numeroGuia = d.guia.id.numeroGuia ");
			hql.append("and d.id.processo.siglaClasseProcessual = p.siglaClasseProcessual ");
			hql.append("and d.id.processo.numeroProcessual = p.numeroProcessual ");
			hql.append("and d.guia.id.anoGuia = :anoGuia ");
			hql.append("and d.guia.id.numeroGuia = :numeroGuia ");
			hql.append("and d.guia.id.codigoOrgaoOrigem = :codigoOrgaoOrigem ");
			
			Query query = session.createQuery(hql.toString());

			if (guia.getId().getAnoGuia() != null) {
				query.setShort("anoGuia", guia.getId().getAnoGuia());
			}
			if (guia.getId().getNumeroGuia() != null) {
				query.setLong("numeroGuia",guia.getId().getNumeroGuia());
			}
			if (guia.getId().getCodigoOrgaoOrigem() != null){
				query.setLong("codigoOrgaoOrigem", guia.getId().getCodigoOrgaoOrigem());
			}
			TipoMeioProcesso tipoMeioProcesso = (TipoMeioProcesso)query.uniqueResult();
//			TipoMeioProcesso tipoMeioProcesso = processo.getTipoMeioProcesso();
			if (tipoMeioProcesso.getCodigo().equals("E")) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	/**
	 * Exclui os processos de uma guia
	 * @param guia
	 * @throws DaoException 
	 */
	@Override
	public void cancelarGuiaProcesso(DeslocaProcesso processo) throws DaoException {
		
		Session session = retrieveSession();
		String sql = "DELETE FROM STF.DESLOCA_PROCESSOS WHERE NUM_GUIA = :num_guia" +
				" AND ANO_GUIA = :ano_guia AND COD_ORGAO_ORIGEM = :cod_origem" +
				" AND NUM_PROCESSO = :num_processo AND SIG_CLASSE_PROCES = :classe ";

		Query query = session.createSQLQuery(sql);
		query.setLong("num_guia",processo.getId().getNumeroGuia());
		query.setShort("ano_guia",processo.getId().getAnoGuia());
		query.setLong("cod_origem",processo.getId().getCodigoOrgaoOrigem());
		query.setLong("num_processo",processo.getNumeroProcesso());
		query.setString("classe",processo.getClasseProcesso());
		
		if(query.executeUpdate() == 0){
			throw new DaoException("A guia " + processo.getId().getNumeroGuia() + "/" + processo.getId().getAnoGuia() + " não possui registros para exclusão.");
		}
		
		session.flush();
	}
	
	/**
	 * Exclui as petições de uma guia
	 * @param guia
	 * @throws DaoException 
	 */
	@Override
	public void cancelarGuiaPeticao(DeslocaPeticao peticao) throws DaoException {
		
		Session session = retrieveSession();
		String sql = "DELETE FROM STF.DESLOCA_PETICAOS WHERE NUM_GUIA = :num_guia" +
			  " AND ANO_GUIA = :ano_guia AND COD_ORGAO_ORIGEM = :cod_origem" +
			  " AND NUM_PETICAO = :num_peticao  AND ANO_PETICAO = :ano_peticao";
			  
		Query query = session.createSQLQuery(sql);
		query.setLong("num_guia",peticao.getId().getNumeroGuia());
		query.setShort("ano_guia",peticao.getId().getAnoGuia());
		query.setLong("cod_origem",peticao.getId().getCodigoOrgaoOrigem());
		query.setLong("num_peticao",peticao.getNumeroPeticao());
		query.setLong("ano_peticao", peticao.getAnoPeticao());

		if (query.executeUpdate() == 0) {
			throw new DaoException("A guia " + peticao.getId().getNumeroGuia() + "/" + peticao.getId().getAnoGuia() + " não possui registros para exclusão.");
		}
		session.flush();
		
/*		Session session = retrieveSession();
		String sql = "DELETE FROM STF.DESLOCA_PETICAOS WHERE NUM_GUIA = :num_guia" +
				" AND ANO_GUIA = :ano_guia AND COD_ORGAO_ORIGEM = :cod_origem" +
				" AND NUM_PROCESSO = :num_processo AND SIG_CLASSE_PROCES = :classe ";

		Query query = session.createSQLQuery(sql);
		query.setLong("num_guia",processo.getGuia().getNumeroGuia());
		query.setShort("ano_guia",processo.getGuia().getAnoGuia());
		query.setLong("cod_origem",processo.getGuia().getCodigoOrgaoOrigem());
		query.setLong("num_processo",processo.getNumeroProcesso());
		query.setString("classe",processo.getClasseProcesso());
*/
	}
	
	public Boolean existeEndereco(EnderecoDestinatario end) throws DaoException{
		Session session = retrieveSession();
		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT g from Guia g ");
		hql.append(" WHERE g.enderecoDestinatario.id = :id ");
		
		Query query = session.createQuery(hql.toString());

		if (end.getId() != null) {
			query.setLong("id",end.getId());
		}
		
		Guia guia = (Guia)query.uniqueResult();
		
		if (guia != null && guia.getId() != null){
			return true;
		}
		return false;
		
	}
	
	@Override
	public Boolean temPermissaoAlterarGuia(Guia guia) throws DaoException {
		StringBuilder sql = new StringBuilder();

		if (guia.getTipoGuia().equals("PRO")) {
			sql.append("SELECT D.DAT_RECEBIMENTO, G.TIP_ORGAO_DESTINO from STF.DESLOCA_PROCESSOS d, STF.GUIAS g ");
		} else {
			sql.append("SELECT D.DAT_RECEBIMENTO, G.TIP_ORGAO_DESTINO from STF.DESLOCA_PETICAOS d, STF.GUIAS g ");
		}
		sql.append(" WHERE D.ANO_GUIA = G.ANO_GUIA");
		sql.append("  and D.NUM_GUIA = G.NUM_GUIA");
		sql.append("  and D.COD_ORGAO_ORIGEM = G.COD_ORGAO_ORIGEM");
		sql.append("  and D.COD_ORGAO_ORIGEM = " + guia.getId().getCodigoOrgaoOrigem());
		sql.append("  and ((G.TIP_ORGAO_DESTINO = 3 and D.FLG_ULTIMO_DESLOCAMENTO = 'S') OR");
		sql.append("	   (G.TIP_ORGAO_DESTINO = 2 AND D.DAT_RECEBIMENTO IS NULL))");
		sql.append("  AND D.NUM_GUIA = " + guia.getId().getNumeroGuia());
		sql.append("  AND D.ANO_GUIA = " + guia.getId().getAnoGuia());
		SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());
		if (sqlQuery.list() == null || sqlQuery.list().size() == 0) {
			return false;
		}
		Object[] result = (Object[]) sqlQuery.list().get(0);
		// verifica as duas situações em que a guia pode ser alterada.
		Long tipoDestino = NumberUtils.createLong(result[1].toString());
		if (result[0] == null) { // data recebimento vazia e destino setor do stf - guia em transito
			if (tipoDestino.equals((long) 2 )) {
				return true;
			}
		} else { // existe a data recebimento, mas o destino é externo - guia com remessa externa sem recebimento (ver cláusula WHERE)
			if (tipoDestino.equals((long) 3 )) {
				return true;
			}
		}
		return false;
	}
	
}

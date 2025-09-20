package br.gov.stf.estf.tarefa.model.dataaccess.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.tarefa.CampoTarefaValor;
import br.gov.stf.estf.entidade.tarefa.ClassificacaoTipoCampoTarefa;
import br.gov.stf.estf.entidade.tarefa.Contato;
import br.gov.stf.estf.entidade.tarefa.Contato.TipoContato;
import br.gov.stf.estf.entidade.tarefa.RelatorioAnaliticoTarefaSetor;
import br.gov.stf.estf.entidade.tarefa.RelatorioAnaliticoTarefaSetor.ValorCampoTipoTarefaRelatorio;
import br.gov.stf.estf.entidade.tarefa.TarefaSetor;
import br.gov.stf.estf.entidade.tarefa.TipoAtribuicaoTarefa;
import br.gov.stf.estf.entidade.tarefa.TipoTarefaSetor;
import br.gov.stf.estf.tarefa.model.dataaccess.TarefaSetorDao;
import br.gov.stf.estf.tarefa.model.util.TarefaSetorSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;
import br.gov.stf.framework.util.FWConfig;

@Repository
public class TarefaSetorDaoHibernate extends GenericHibernateDao<TarefaSetor, Long> implements TarefaSetorDao {

	private static final long serialVersionUID = -9105075321220788123L;

	public TarefaSetorDaoHibernate() {
		super(TarefaSetor.class);
	}

	/**
	 * @deprecated Utilizar o método {@link #pesquisarQuantidadeTarefaSetor(TarefaSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	@Override
	public Long pesquisarQuantidadeTarefaSetor(Long codigo, String descricao, String classeProcessual, Long numeroProcesso, Long idSetorOrigem, Long idSetorDestino,
			Long idTipoTarefa, Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal, Date dataInicioPrevistoInicial,
			Date dataInicioPrevistoFinal, Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim, Date dataFimInicial,
			Date dataFimFinal, Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario, Boolean urgente, Boolean sigiloso,
			Boolean iniciado, Boolean finalizado, Boolean semCampoTarefa, List<CampoTarefaValor> listaCampoTarefa, Boolean semTarefaSetor) throws DaoException {

		TarefaSetorSearchData searchData = new TarefaSetorSearchData(codigo, descricao, classeProcessual, numeroProcesso, idSetorOrigem, idSetorDestino, idTipoTarefa, prioridade,
				idTipoSituacaoTarefa, dataCriacaoInicial, dataCriacaoFinal, dataInicioPrevistoInicial, dataInicioPrevistoFinal, dataFimPrevistoInicial, dataFimPrevistoFinal,
				dataInicioInicial, dataInicioFim, dataFimInicial, dataFimFinal, dataTipoCampoTarefaInicial, dataTipoCampoTarefaFinal, idTipoCampoTarefaPeriodo, sigUsuario,
				urgente, sigiloso, iniciado, finalizado, semCampoTarefa, null, listaCampoTarefa, null, semTarefaSetor, Boolean.TRUE);
		
		return pesquisarQuantidadeTarefaSetor(searchData);
	}

	@Override
	public Long pesquisarQuantidadeTarefaSetor(TarefaSetorSearchData searchData) throws DaoException {

		Long result = null;

		try {
			Query query = createTarefaSetor(searchData, true);
			result = (Long) query.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return result;
	}

	/**
	 * @deprecated Utilizar o método {@link #pesquisarTarefaSetor(TarefaSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	@Override
	public List<TarefaSetor> pesquisarTarefaSetor(Long codigo, String descricao, String classeProcessual, Long numeroProcesso, Long idSetorOrigem, Long idSetorDestino,
			Long idTipoTarefa, Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal, Date dataInicioPrevistoInicial,
			Date dataInicioPrevistoFinal, Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim, Date dataFimInicial,
			Date dataFimFinal, Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario, Boolean urgente, Boolean sigiloso,
			Boolean iniciado, Boolean finalizado, Boolean semCampoTarefa, Boolean limitarPesquisa, List<CampoTarefaValor> listaCampoTarefa, Boolean semTarefaSetor,
			Boolean readOnlyQuery) throws DaoException {

		TarefaSetorSearchData searchData = new TarefaSetorSearchData(codigo, descricao, classeProcessual, numeroProcesso, idSetorOrigem, idSetorDestino, idTipoTarefa, prioridade,
				idTipoSituacaoTarefa, dataCriacaoInicial, dataCriacaoFinal, dataInicioPrevistoInicial, dataInicioPrevistoFinal, dataFimPrevistoInicial, dataFimPrevistoFinal,
				dataInicioInicial, dataInicioFim, dataFimInicial, dataFimFinal, dataTipoCampoTarefaInicial, dataTipoCampoTarefaFinal, idTipoCampoTarefaPeriodo, sigUsuario,
				urgente, sigiloso, iniciado, finalizado, semCampoTarefa, limitarPesquisa, listaCampoTarefa, null, semTarefaSetor, readOnlyQuery);

		return pesquisarTarefaSetor(searchData);
	}

	@Override
	public List<TarefaSetor> pesquisarTarefaSetor(TarefaSetorSearchData searchData) throws DaoException {

		List<TarefaSetor> listaAlerta = null;
		/*
		 * if(limitarPesquisa == null || !limitarPesquisa.booleanValue()){ readOnlyQuery = true; }
		 */
		try {
			Query query = createTarefaSetor(searchData, false);
			if (searchData.limitarPesquisa != null && searchData.limitarPesquisa.booleanValue()) {
				if (FWConfig.getInstance().getMaxQueryResult() > 0)
					query.setMaxResults(FWConfig.getInstance().getMaxQueryResult());

			}

			listaAlerta = query.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return listaAlerta;
	}

	@Override
	public TarefaSetor recuperarTarefaSetor(Long id, String classeProcessual, Long numeroProcesso, Long codidoSetor) throws DaoException {
		TarefaSetor tarefaSetor = null;

		try {
			Query query = createTarefaSetor(id, null, classeProcessual, numeroProcesso, null, codidoSetor, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null, null, null, null, null, null, null, false, false);

			tarefaSetor = (TarefaSetor) query.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return tarefaSetor;
	}

	@Override
	public List<TipoAtribuicaoTarefa> pesquisarTipoAtribuicaoTarefa(Long id, String sigla, String descricao) throws DaoException {
		Session session = retrieveSession();

		List<TipoAtribuicaoTarefa> lista = null;

		try {
			Criteria criteria = session.createCriteria(TipoAtribuicaoTarefa.class);

			if (id != null) {
				criteria.add(Restrictions.eq("id", id));
			}

			if (sigla != null && sigla.trim().length() > 0) {
				criteria.add(Restrictions.eq("sigla", sigla));
			}

			if (descricao != null && descricao.trim().length() > 0) {
				criteria.add(Restrictions.eq("descricao", descricao));
			}

			criteria.addOrder(Order.desc("descricao"));

			lista = criteria.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return lista;
	}

	@Override
	public Boolean persistirTarefaSetor(TarefaSetor tarefa) throws DaoException {
		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;
		try {

			// session.persist(tarefa);
			session.saveOrUpdate(tarefa);
			session.flush();

			alterado = Boolean.TRUE;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return alterado;

	}

	@Override
	public Boolean excluirTarefaSetor(TarefaSetor tarefaSetor) throws DaoException {
		Session session = retrieveSession();
		Boolean excluido = Boolean.FALSE;
		try {
			session.delete(tarefaSetor);
			session.flush();

			excluido = Boolean.TRUE;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return excluido;

	}

	@Override
	public Boolean persistirCampoTarefaValor(CampoTarefaValor campoTarefaValor) throws DaoException {
		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;
		try {

			// session.persist(tarefa);
			session.persist(campoTarefaValor);
			session.flush();

			alterado = Boolean.TRUE;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return alterado;

	}

	/**
	 * @deprecated Utilizar o método {@link #pesquisarTarefaSetorRelatorioSqlNativo(TarefaSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	@Override
	public List<RelatorioAnaliticoTarefaSetor> pesquisarTarefaSetorRelatorioSqlNativo(Long codigo, String descricao, String classeProcessual, Long numeroProcesso,
			Long idSetorOrigem, Long idSetorDestino, Long idTipoTarefa, Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal,
			Date dataInicioPrevistoInicial, Date dataInicioPrevistoFinal, Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim,
			Date dataFimInicial, Date dataFimFinal, Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario,
			Boolean urgente, Boolean sigiloso, Boolean iniciado, Boolean finalizado, Boolean semCampoTarefa, List<CampoTarefaValor> listaCampoTarefa,
			List<RelatorioAnaliticoTarefaSetor.ValorCampoTipoTarefaRelatorio> listaValorCampoTipoTarefa, Boolean semTarefaSetor) throws DaoException {

		TarefaSetorSearchData searchData = new TarefaSetorSearchData(codigo, descricao, classeProcessual, numeroProcesso, idSetorOrigem, idSetorDestino, idTipoTarefa, prioridade,
				idTipoSituacaoTarefa, dataCriacaoInicial, dataCriacaoFinal, dataInicioPrevistoInicial, dataInicioPrevistoFinal, dataFimPrevistoInicial, dataFimPrevistoFinal,
				dataInicioInicial, dataInicioFim, dataFimInicial, dataFimFinal, dataTipoCampoTarefaInicial, dataTipoCampoTarefaFinal, idTipoCampoTarefaPeriodo, sigUsuario,
				urgente, sigiloso, iniciado, finalizado, semCampoTarefa, null, listaCampoTarefa, listaValorCampoTipoTarefa, semTarefaSetor, null);

		return pesquisarTarefaSetorRelatorioSqlNativo(searchData);
	}

	@Override
	public List<RelatorioAnaliticoTarefaSetor> pesquisarTarefaSetorRelatorioSqlNativo(TarefaSetorSearchData searchData) throws DaoException {
		List<RelatorioAnaliticoTarefaSetor> listaRelatorio = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Session session = retrieveSession();

			StringBuffer sql = new StringBuffer(" SELECT * FROM ( SELECT proc.processos ," + " usuarios.usuarios, " + " (select tst.dsc_tipo_situacao_tarefa "
					+ " from egab.historico_situacao_tarefa hs " + " join egab.tipo_situacao_tarefa tst on hs.seq_tipo_situacao_tarefa = tst.seq_tipo_situacao_tarefa "
					+ " where hs.seq_historico_situacao_tarefa = ts.seq_historico_situacao_tarefa " + " )as situacao, " + " ts.dat_inicio, " + " ts.dat_fim, "
					+ " ts.dat_prevista_inicio, " + " ts.dat_prevista_fim, " + " ts.num_prioridade, " + " flg_sigiloso, " + " flg_urgente,");

			if (searchData.listaValorCampoTipoTarefa != null && searchData.listaValorCampoTipoTarefa.size() > 0) {

				for (ValorCampoTipoTarefaRelatorio valor : searchData.listaValorCampoTipoTarefa) {
					sql.append("  (select ct.dsc_valor_campo " + "     from egab.campo_tarefa ct " + "     where ct.seq_tarefa_setor = ts.seq_tarefa_setor "
							+ "     and ct.seq_tipo_campo_tarefa = " + valor.getCodigo() + ")as " + removerCaracteresDiferenteLetraNumero(valor.getDescricao()) + ", ");
				}
			}
			sql.append(" ts.dsc_tarefa_setor ");

			sql.append("FROM " + " EGAB.TAREFA_SETOR ts, "
					+ " (SELECT LTRIM(MAX (SYS_CONNECT_BY_PATH (SIG_USUARIO, ' - '))KEEP (DENSE_RANK LAST ORDER BY curr),', ') AS USUARIOS, " + "  des.SEQ_TAREFA_SETOR "
					+ "  FROM ( SELECT SEQ_TAREFA_SETOR, SIG_USUARIO, SEQ_TIPO_ATRIBUICAO, "
					+ "         ROW_NUMBER () OVER (PARTITION BY SEQ_TAREFA_SETOR ORDER BY sig_usuario desc) AS curr, "
					+ "         ROW_NUMBER () OVER (PARTITION BY SEQ_TAREFA_SETOR ORDER BY sig_usuario desc) - 1 AS prev " + "         FROM EGAB.TAREFA_USUARIO A  "
					+ "        ) des " + "  GROUP BY SEQ_TAREFA_SETOR " + "  CONNECT BY prev = PRIOR curr AND SEQ_TAREFA_SETOR = PRIOR SEQ_TAREFA_SETOR "
					+ "  START WITH curr = 1 " + " ) usuarios, "
					+ " ( SELECT LTRIM(MAX (SYS_CONNECT_BY_PATH (prc.processo, ' - '))KEEP (DENSE_RANK LAST ORDER BY curr),', ') AS processos," + "    prc.SEQ_TAREFA_SETOR "
					+ "  FROM ( SELECT seq_tarefa_setor, sig_classe_proces||'/'||num_processo as processo, "
					+ "         ROW_NUMBER () OVER (PARTITION BY seq_tarefa_setor order by sig_classe_proces,num_processo ) AS curr, "
					+ "         ROW_NUMBER () OVER (PARTITION BY seq_tarefa_setor order by sig_classe_proces,num_processo ) - 1 AS prev "
					+ "         FROM EGAB.tarefa_atribuida_processo A  " + "        ) prc " + "  GROUP BY prc.SEQ_TAREFA_SETOR "
					+ "  CONNECT BY prev = PRIOR curr AND SEQ_TAREFA_SETOR = PRIOR SEQ_TAREFA_SETOR " + "  START WITH curr = 1 " + " ) proc "
					+ " WHERE usuarios.SEQ_TAREFA_SETOR(+) = ts.SEQ_TAREFA_SETOR " + " and proc.SEQ_TAREFA_SETOR (+)= ts.SEQ_TAREFA_SETOR ");
			if (searchData.codigo != null)
				sql.append(" AND ts.seq_tarefa_setor = " + searchData.codigo);

			if (searchData.descricao != null && searchData.descricao.trim().length() > 0)
				sql.append(" AND ts.dsc_tarefa_setor LIKE '%" + searchData.descricao + "%'");

			if ((searchData.classeProcessual != null && !searchData.classeProcessual.equals("")) || searchData.numeroProcesso != null) {
				sql.append(" AND ts.seq_tarefa_setor IN(Select tap.seq_tarefa_setor " + " from egab.tarefa_atribuida_processo tap " + " WHERE 1=1");
				if (searchData.classeProcessual != null && searchData.classeProcessual.trim().length() > 0) {
					sql.append(" AND tap.sig_classe_proces = '" + searchData.classeProcessual + "'");
				}
				if (searchData.numeroProcesso != null) {
					sql.append(" AND tap.num_processo = " + searchData.numeroProcesso);
				}
				sql.append(" and tap.seq_tarefa_setor = ts.seq_tarefa_setor)");
			}

			if (searchData.idSetorOrigem != null) {
				sql.append(" AND ts.COD_SETOR_ORIGEM = " + searchData.idSetorOrigem);
			}

			if (searchData.idSetorDestino != null) {
				sql.append(" AND ts.COD_SETOR_DESTINO =" + searchData.idSetorDestino);

			}

			if (searchData.idTipoTarefa != null) {
				sql.append(" AND ts.seq_tipo_tarefa =" + searchData.idTipoTarefa);
			}

			if (searchData.prioridade != null) {
				sql.append(" AND ts.NUM_PRIORIDADE =" + searchData.prioridade);
			}
			if (searchData.idTipoSituacaoTarefa != null) {
				sql.append(" AND ts.seq_tarefa_setor in(SELECT hst.seq_tarefa_setor " + " 							FROM egab.HISTORICO_SITUACAO_TAREFA hst "
						+ " 							WHERE hst.SEQ_TIPO_SITUACAO_TAREFA = " + searchData.idTipoSituacaoTarefa + ""
						+ "					  and hst.seq_historico_situacao_tarefa = ts.seq_historico_situacao_tarefa) ");
			}

			if (searchData.dataCriacaoInicial != null) {
				sql.append(" AND ts.DAT_CRIACAO >= to_date('" + DateTimeHelper.getDataHoraString(searchData.dataCriacaoInicial) + "', 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (searchData.dataCriacaoFinal != null) {
				sql.append(" AND ts.DAT_CRIACAO <= to_date('" + DateTimeHelper.getDataHoraString(searchData.dataCriacaoFinal) + "', 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (searchData.dataInicioPrevistoInicial != null) {
				sql.append(" AND ts.DAT_PREVISTA_INICIO >= to_date('" + DateTimeHelper.getDataHoraString(searchData.dataInicioPrevistoInicial) + "', 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (searchData.dataInicioPrevistoFinal != null) {
				sql.append(" AND ts.DAT_PREVISTA_INICIO <= to_date('" + DateTimeHelper.getDataHoraString(searchData.dataInicioPrevistoFinal) + "', 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (searchData.dataFimPrevistoInicial != null) {
				sql.append(" AND ts.DAT_PREVISTA_FIM >= to_date('" + DateTimeHelper.getDataHoraString(searchData.dataFimPrevistoInicial) + "', 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (searchData.dataFimPrevistoFinal != null) {
				sql.append(" AND ts.DAT_PREVISTA_FIM <= to_date('" + DateTimeHelper.getDataHoraString(searchData.dataFimPrevistoFinal) + "', 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (searchData.dataInicioInicial != null) {
				sql.append(" AND ts.DAT_INICIO >= to_date('" + DateTimeHelper.getDataHoraString(searchData.dataInicioInicial) + "', 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (searchData.dataInicioFim != null) {
				sql.append(" AND ts.DAT_INICIO <= to_date('" + DateTimeHelper.getDataHoraString(searchData.dataInicioFim) + "', 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (searchData.dataFimInicial != null) {
				sql.append(" AND ts.DAT_FIM >= to_date('" + DateTimeHelper.getDataHoraString(searchData.dataFimInicial) + "', 'DD/MM/YYYY HH24:MI:SS')");
				;
			}
			if (searchData.dataFimFinal != null) {
				sql.append(" AND ts.DAT_FIM <= to_date('" + DateTimeHelper.getDataHoraString(searchData.dataFimFinal) + "', 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (searchData.sigUsuario != null && searchData.sigUsuario.trim().length() > 0) {
				sql.append(" 	AND ts.seq_tarefa_setor in (Select ata.seq_tarefa_setor " + " from egab.TAREFA_USUARIO ata " + " where ata.sig_usuario = '" + searchData.sigUsuario
						+ "')");
			}

			if (searchData.urgente != null) {
				if (searchData.urgente) {
					sql.append(" AND ts.FLG_URGENTE = 'S'");
				} else {
					sql.append(" AND ts.FLG_URGENTE = 'N'");
				}
			}

			if (searchData.semTarefaSetor != null) {
				if (searchData.semTarefaSetor) {
					sql.append(" AND ts.seq_tipo_tarefa <> " + TipoTarefaSetor.TipoTarefa.REPERCUSSAO_GERAL.getCodigo());
				} else {
					sql.append(" AND ts.seq_tipo_tarefa = " + TipoTarefaSetor.TipoTarefa.REPERCUSSAO_GERAL.getCodigo());
				}
			}

			if (searchData.sigiloso != null) {
				if (searchData.sigiloso) {
					sql.append(" AND ts.FLG_SIGILOSO = 'S' ");
				} else {
					sql.append(" AND ts.FLG_SIGILOSO = 'N' ");
				}
			}

			if (searchData.iniciado != null) {
				if (searchData.iniciado) {
					sql.append(" AND ts.DAT_INICIO IS NOT NULL ");
				} else {
					sql.append(" AND ts.DAT_INICIO IS NULL ");
				}
			}

			if (searchData.finalizado != null) {
				if (searchData.finalizado) {
					sql.append(" AND ts.DAT_FIM IS NOT NULL ");
				} else {
					sql.append(" AND ts.DAT_FIM IS NULL ");
				}
			}

			if (searchData.listaCampoTarefa != null && searchData.listaCampoTarefa.size() > 0) {
				List<Long> listaIdTarefaSetor = pesquisarTarefaSetorId(searchData.listaCampoTarefa, true);
				if (listaIdTarefaSetor != null && listaIdTarefaSetor.size() > 0) {
					sql.append(" AND ts.seq_tarefa_setor in(");
					boolean inicio = true;
					for (Long id : listaIdTarefaSetor) {
						sql.append(inicio ? id : "," + id);
						inicio = false;
					}
					sql.append(")");
				}

			}
			
			if (searchData.semCampoTarefa != null) {
				if (searchData.semCampoTarefa.booleanValue()) {
					sql.append(" AND ts.seq_tarefa_setor NOT IN ");
				} else {
					sql.append(" AND ts.seq_tarefa_setor IN ");
				}
				sql.append(" (SELECT ct.seq_tarefa_setor FROM egab.Campo_tarefa ct " + " WHERE ct.seq_tarefa_setor = ts.seq_tarefa_setor)  ");
			}
	        
			sql.append(" ) tmp ");			

			if ((searchData.dataTipoCampoTarefaInicial != null && searchData.dataTipoCampoTarefaFinal != null && searchData.idTipoCampoTarefaPeriodo != null)) {
				sql.append(" WHERE  TO_DATE (tmp.data, 'DD/MM/YYYY') >= TO_DATE ('"+ DateTimeHelper.getDataString(searchData.dataTipoCampoTarefaInicial) +" ', 'DD/MM/YYYY') ");
                sql.append(" AND TO_DATE (tmp.data, 'DD/MM/YYYY') <= TO_DATE ('"+ DateTimeHelper.getDataString(searchData.dataTipoCampoTarefaFinal) +" ', 'DD/MM/YYYY') ");
		
			}

		    // System.out.println(sql.toString());
			stmt = session.connection().createStatement();
			rs = stmt.executeQuery(sql.toString());
			listaRelatorio = new LinkedList<RelatorioAnaliticoTarefaSetor>();
			while (rs.next()) {
				RelatorioAnaliticoTarefaSetor rel = new RelatorioAnaliticoTarefaSetor();
				String processos = rs.getString("processos");
				if (processos != null && processos.trim().length() > 2) {
					rel.setIdenticiacoesProcessuais(processos.substring(2));
				}

				String usuarios = rs.getString("usuarios");
				if (usuarios != null && usuarios.trim().length() > 2) {
					rel.setUsuariosResponsaveis(usuarios.substring(2));
				}

				Date dataInicio = rs.getDate("dat_inicio");
				if (dataInicio != null) {
					rel.setDataInicio(DateTimeHelper.getDataString(dataInicio));
				}

				Date dataFim = rs.getDate("dat_fim");
				if (dataFim != null) {
					rel.setDataFim(DateTimeHelper.getDataString(dataFim));
				}

				Date dataPrevistaInicio = rs.getDate("dat_prevista_inicio");
				if (dataPrevistaInicio != null) {
					rel.setDataPrevistaInicio(DateTimeHelper.getDataString(dataPrevistaInicio));
				}

				Date dataPrevistaFim = rs.getDate("dat_prevista_fim");
				if (dataPrevistaFim != null) {
					rel.setDataPrevistaFim(DateTimeHelper.getDataString(dataPrevistaFim));
				}

				rel.setSituacao(rs.getString("situacao"));
				rel.setPrioridade(rs.getString("num_prioridade"));
				rel.setSigiloso(rs.getString("FLG_SIGILOSO").equals("S") ? "Sim" : "Não");
				rel.setUrgente(rs.getString("FLG_URGENTE").equals("S") ? "Sim" : "Não");
				rel.setDescricao(rs.getString("dsc_tarefa_setor"));

				rel.setValoresCampoTipoTarefa(new LinkedList<ValorCampoTipoTarefaRelatorio>());
				if (searchData.listaValorCampoTipoTarefa != null && searchData.listaValorCampoTipoTarefa.size() > 0) {
					for (ValorCampoTipoTarefaRelatorio valor : searchData.listaValorCampoTipoTarefa) {
						ValorCampoTipoTarefaRelatorio valorRel = new ValorCampoTipoTarefaRelatorio();
						valorRel.setCodigo(valor.getCodigo());
						valorRel.setDescricao(valor.getDescricao());
						valorRel.setClassificacao(valor.getClassificacao());
						String coluna = removerCaracteresDiferenteLetraNumero(valor.getDescricao());
						String valorColuna = rs.getString(coluna);
						if (valorColuna != null && valorColuna.trim().length() > 0) {
							valorRel.setValor(valorColuna);
						}
						rel.getValoresCampoTipoTarefa().add(valorRel);
					}
				}
				listaRelatorio.add(rel);
			}
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} finally {

			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DaoException("Erro ao executar o comando SQL", e);
			}

		}

		return listaRelatorio;
	}

	private String removerCaracteresDiferenteLetraNumero(String valor) {
		String retorno = valor;
		if (valor != null && valor.trim().length() > 0) {
			for (int i = 0; i < valor.length(); i++) {
				Character letra = valor.charAt(i);
				int codigo = valor.codePointAt(i);
				boolean substituir = true;
				if (codigo >= 48 && codigo <= 57) {
					substituir = false;
				}

				if (codigo >= 65 && codigo <= 90) {
					substituir = false;
				}

				if (codigo >= 97 && codigo <= 122) {
					substituir = false;
				}

				if (substituir) {
					retorno = retorno.replace(letra.toString(), "");
				}
			}
		}
		return retorno;
	}

	/**
	 * @deprecated Utilizar o método {@link #createTarefaSetor(TarefaSetorSearchData, Boolean)} ao invés deste.
	 */
	@Deprecated
	protected Query createTarefaSetor(Long codigo, String descricao, String classeProcessual, Long numeroProcesso, Long idSetorOrigem, Long idSetorDestino, Long idTipoTarefa,
			Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal, Date dataInicioPrevistoInicial, Date dataInicioPrevistoFinal,
			Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim, Date dataFimInicial, Date dataFimFinal,
			Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario, Boolean urgente, Boolean sigiloso, Boolean iniciado,
			Boolean finalizado, Boolean semCampoTarefa, List<CampoTarefaValor> listaCampoTarefa, Boolean semTarefaSetor, Boolean readOnlyQuery, Boolean count) throws DaoException {

		TarefaSetorSearchData searchData = new TarefaSetorSearchData(codigo, descricao, classeProcessual, numeroProcesso, idSetorOrigem, idSetorDestino, idTipoTarefa, prioridade,
				idTipoSituacaoTarefa, dataCriacaoInicial, dataCriacaoFinal, dataInicioPrevistoInicial, dataInicioPrevistoFinal, dataFimPrevistoInicial, dataFimPrevistoFinal,
				dataInicioInicial, dataInicioFim, dataFimInicial, dataFimFinal, dataTipoCampoTarefaInicial, dataTipoCampoTarefaFinal, idTipoCampoTarefaPeriodo, sigUsuario,
				urgente, sigiloso, iniciado, finalizado, semCampoTarefa, null, listaCampoTarefa, null, semTarefaSetor, readOnlyQuery);

		return createTarefaSetor(searchData, count);
	}

	protected Query createTarefaSetor(TarefaSetorSearchData searchData, Boolean count) throws DaoException {
		StringBuffer hql = new StringBuffer();

		/*
		 * ------------------------------------- SELECT & JOINS ----------------------------------
		 */

		if (count != null && count.booleanValue()) {
			hql.append(" SELECT COUNT(ts.id) FROM TarefaSetor ts ");
		} else {
			hql.append(" SELECT ts FROM TarefaSetor ts ");
		}
		if (searchData.classeProcessual != null && searchData.classeProcessual.trim().length() > 0 || searchData.numeroProcesso != null) {
			hql.append(" JOIN ts.processos p ");
		}
		if (searchData.sigUsuario != null && searchData.sigUsuario.trim().length() > 0) {
			hql.append(" JOIN ts.atribuicoesTarefa at ");
		}

		/*
		 * ---------------------------------------- WHERE ---------------------------------------
		 */

		hql.append(" WHERE 1=1 ");

		if (searchData.codigo != null)
			hql.append(" AND ts.id = :id ");

		if (searchData.descricao != null && searchData.descricao.trim().length() > 0)
			hql.append(" AND ts.descricao LIKE :Descricao ");

		if (searchData.classeProcessual != null && !searchData.classeProcessual.equals(""))
			hql.append(" AND p.classeProcessual.id = :classeProcessual ");

		if (searchData.numeroProcesso != null)
			hql.append(" AND p.numeroProcessual = :numeroProcesso ");

		/*
		 * if( searchData.idSetorOrigem != null ) hql.append(" AND ts.setorOrigem.id = :idSetorOrigem ");
		 */

		if (searchData.idSetorDestino != null)
			hql.append(" AND ts.setorDestino.id = :idSetorDestino ");

		if (searchData.idTipoTarefa != null)
			hql.append(" AND ts.tipoTarefa.id = :idTipoTarefa ");

		if (searchData.prioridade != null)
			hql.append(" AND ts.prioridade = :prioridade ");

		if (searchData.idTipoSituacaoTarefa != null)
			hql.append(" AND ts.situacaoAtual.tipoSituacaoTarefa.id = :idTipoSituacaoTarefa ");

		if (searchData.dataCriacaoInicial != null)
			hql.append(" AND ts.dataCriacao >= to_date(:dataCriacaoInicial, 'DD/MM/YYYY HH24:MI:SS') ");

		if (searchData.dataCriacaoFinal != null) {
			hql.append(" AND ts.dataCriacao <= to_date(:dataCriacaoFinal, 'DD/MM/YYYY HH24:MI:SS') ");
		}

		if (searchData.dataInicioPrevistoInicial != null)
			hql.append(" AND ts.dataPrevistaInicio >= to_date(:dataInicioPrevistoInicial, 'DD/MM/YYYY HH24:MI:SS') ");

		if (searchData.dataInicioPrevistoFinal != null) {
			hql.append(" AND ts.dataPrevistaInicio <= to_date(:dataInicioPrevistoFinal, 'DD/MM/YYYY HH24:MI:SS') ");
		}
		if (searchData.dataFimPrevistoInicial != null)
			hql.append(" AND ts.dataPrevistaFim >= to_date(:dataFimPrevistoInicial, 'DD/MM/YYYY HH24:MI:SS') ");

		if (searchData.dataFimPrevistoFinal != null) {
			hql.append(" AND ts.dataPrevistaFim <= to_date(:dataFimPrevistoFinal, 'DD/MM/YYYY HH24:MI:SS') ");
		}

		if (searchData.dataInicioInicial != null)
			hql.append(" AND ts.dataInicio >= to_date(:dataInicioInicial, 'DD/MM/YYYY HH24:MI:SS') ");

		if (searchData.dataInicioFim != null) {
			hql.append(" AND ts.dataInicio <= to_date(:dataInicioFim, 'DD/MM/YYYY HH24:MI:SS') ");
		}

		if (searchData.dataFimInicial != null)
			hql.append(" AND ts.dataFim >= to_date(:dataFimInicial, 'DD/MM/YYYY HH24:MI:SS') ");

		if (searchData.dataFimFinal != null) {
			hql.append(" AND ts.dataFim <= to_date(:dataFimFinal, 'DD/MM/YYYY HH24:MI:SS') ");
		}

		if (searchData.sigUsuario != null && searchData.sigUsuario.trim().length() > 0) {
			hql.append(" AND at.usuarioAtribuido.id = :sigUsuario ");
		}
		if (searchData.urgente != null) {
			if (searchData.urgente) {
				hql.append(" AND ts.urgente = 'S' ");
			} else {
				hql.append(" AND ts.urgente = 'N' ");
			}
		}

		if (searchData.semTarefaSetor != null) {
			if (searchData.semTarefaSetor) {
				hql.append(" AND ts.tipoTarefa.id <> " + TipoTarefaSetor.TipoTarefa.REPERCUSSAO_GERAL.getCodigo());
			} else {
				hql.append(" AND ts.tipoTarefa.id = " + TipoTarefaSetor.TipoTarefa.REPERCUSSAO_GERAL.getCodigo());
			}
		}

		if (searchData.sigiloso != null) {
			if (searchData.sigiloso) {
				hql.append(" AND ts.sigiloso = 'S' ");
			} else {
				hql.append(" AND ts.sigiloso = 'N' ");
			}
		}

		if (searchData.iniciado != null) {
			if (searchData.iniciado) {
				hql.append(" AND ts.dataInicio IS NOT NULL ");
			} else {
				hql.append(" AND ts.dataInicio IS NULL ");
			}
		}

		if (searchData.finalizado != null) {
			if (searchData.finalizado) {
				hql.append(" AND ts.dataFim IS NOT NULL ");
			} else {
				hql.append(" AND ts.dataFim IS NULL ");
			}
		}

		List<Long> listaIdTarefaSetor = null;
		if (searchData.listaCampoTarefa != null && searchData.listaCampoTarefa.size() > 0) {
			listaIdTarefaSetor = pesquisarTarefaSetorId(searchData.listaCampoTarefa, true);
			if (listaIdTarefaSetor != null && listaIdTarefaSetor.size() > 0) {
				hql.append(" AND ts.id in(");
				boolean inicio = true;
				for (Long id : listaIdTarefaSetor) {
					hql.append(inicio ? id : "," + id);
					inicio = false;
				}
				hql.append(")");
			}

		}

		if (searchData.dataTipoCampoTarefaInicial != null && searchData.dataTipoCampoTarefaFinal != null && searchData.idTipoCampoTarefaPeriodo != null) {
			hql.append(" AND EXISTS ( SELECT ct.id " + "				FROM CampoTarefaValor ct " + "			  WHERE ct.tarefaSetor.id = ts.id " + " AND (ct.tipoCampoTarefa.id = "
					+ searchData.idTipoCampoTarefaPeriodo + " AND TO_DATE(ct.valor,'DD/MM/YYYY') >= TO_DATE('"
					+ DateTimeHelper.getDataString(searchData.dataTipoCampoTarefaInicial) + "','DD/MM/YYYY')" + " AND TO_DATE(ct.valor,'DD/MM/YYYY') <= TO_DATE('"
					+ DateTimeHelper.getDataString(searchData.dataTipoCampoTarefaFinal) + "','DD/MM/YYYY'))");

			hql.append(" ) ");
		}

		if (searchData.semCampoTarefa != null) {
			if (searchData.semCampoTarefa.booleanValue()) {
				hql.append(" AND ts NOT IN ");
			} else {
				hql.append(" AND ts IN ");
			}
			hql.append("(SELECT ct.tarefaSetor FROM CampoTarefaValor ct" + "	  WHERE ct.tarefaSetor = ts)            ");
		}

		/*
		 * ---------------------------------- QUERY & PARAMETERS ---------------------------------
		 */

		Session session = retrieveSession();
		Query query = session.createQuery(hql.toString());

		if (searchData.codigo != null)
			query.setLong("id", searchData.codigo);
		if (searchData.descricao != null && searchData.descricao.trim().length() > 0)
			query.setString("Descricao", "%" + searchData.descricao + "%");
		if (searchData.classeProcessual != null && searchData.classeProcessual.trim().length() > 0)
			query.setString("classeProcessual", searchData.classeProcessual);
		if (searchData.numeroProcesso != null)
			query.setLong("numeroProcesso", searchData.numeroProcesso);
		/*
		 * if( idSetorOrigem != null ) query.setLong("idSetorOrigem", idSetorOrigem);
		 */
		if (searchData.idSetorDestino != null)
			query.setLong("idSetorDestino", searchData.idSetorDestino);
		if (searchData.idTipoTarefa != null)
			query.setLong("idTipoTarefa", searchData.idTipoTarefa);
		if (searchData.prioridade != null)
			query.setLong("prioridade", searchData.prioridade);
		if (searchData.idTipoSituacaoTarefa != null)
			query.setLong("idTipoSituacaoTarefa", searchData.idTipoSituacaoTarefa);
		if (searchData.dataCriacaoInicial != null)
			query.setString("dataCriacaoInicial", DateTimeHelper.getDataString(searchData.dataCriacaoInicial) + " 00:00:00");
		if (searchData.dataCriacaoFinal != null)
			query.setString("dataCriacaoFinal", DateTimeHelper.getDataString(searchData.dataCriacaoFinal) + " 23:59:59");
		if (searchData.dataInicioPrevistoInicial != null)
			query.setString("dataInicioPrevistoInicial", DateTimeHelper.getDataString(searchData.dataInicioPrevistoInicial) + " 00:00:00");
		if (searchData.dataInicioPrevistoFinal != null)
			query.setString("dataInicioPrevistoFinal", DateTimeHelper.getDataString(searchData.dataInicioPrevistoFinal) + " 23:59:59");
		if (searchData.dataFimPrevistoInicial != null)
			query.setString("dataFimPrevistoInicial", DateTimeHelper.getDataString(searchData.dataFimPrevistoInicial) + " 00:00:00");
		if (searchData.dataFimPrevistoFinal != null)
			query.setString("dataFimPrevistoFinal", DateTimeHelper.getDataString(searchData.dataFimPrevistoFinal) + " 23:59:59");
		if (searchData.dataInicioInicial != null)
			query.setString("dataInicioInicial", DateTimeHelper.getDataString(searchData.dataInicioInicial) + " 00:00:00");
		if (searchData.dataInicioFim != null)
			query.setString("dataInicioFim", DateTimeHelper.getDataString(searchData.dataInicioFim) + " 23:59:59");
		if (searchData.dataFimInicial != null)
			query.setString("dataFimInicial", DateTimeHelper.getDataString(searchData.dataFimInicial) + " 00:00:00");
		if (searchData.dataFimFinal != null)
			query.setString("dataFimFinal", DateTimeHelper.getDataString(searchData.dataFimFinal) + " 23:59:59");
		if (searchData.sigUsuario != null && searchData.sigUsuario.trim().length() > 0)
			query.setString("sigUsuario", searchData.sigUsuario);

		if (searchData.readOnlyQuery != null && searchData.readOnlyQuery.booleanValue())
			query.setReadOnly(true);
		return query;
	}

	@Override
	public List<CampoTarefaValor> pesquisarCampoTarefaValor(Long id, Long idTarefaSetor, Long idTipoCampoTarefa, String classeProcessual, Long numeroProcesso,
			Boolean repercussaoFinalizada) throws DaoException {
		Session session = retrieveSession();

		List<CampoTarefaValor> lista = null;

		try {
			StringBuffer hql = new StringBuffer(" SELECT ct " + " FROM CampoTarefaValor ct, " + " ct.tarefaSetor ts ");

			if (classeProcessual != null && classeProcessual.trim().length() > 0 || numeroProcesso != null) {
				hql.append(" ,ts.processos p ");
			}

			hql.append(" WHERE ct.tarefaSetor.id = ts.id ");

			if (id != null) {
				hql.append(" AND ct.id = " + id);
			}

			if (idTarefaSetor != null) {
				hql.append(" AND ts.id = " + idTarefaSetor);
			}

			if (idTipoCampoTarefa != null) {
				hql.append(" AND ct.tipoCampoTarefa.id = " + idTipoCampoTarefa);
			}

			if (classeProcessual != null && classeProcessual.trim().length() > 0) {
				hql.append(" AND p.classeProcessual.id = '" + classeProcessual + "'");
			}

			if (numeroProcesso != null) {
				hql.append(" AND p.numeroProcessual = " + numeroProcesso);
			}

			if (repercussaoFinalizada != null && repercussaoFinalizada) {
				hql.append(" AND (ts.dataFim IS NOT NULL " + " OR ts.dataPrevistaFim < SYSDATE )");
			}
			hql.append(" AND ct.dataInclusao = (SELECT MAX(cvt.dataInclusao)" + "		                 FROM CampoTarefaValor cvt"
					+ "					    WHERE cvt.tarefaSetor.id = ct.tarefaSetor.id) ");

			Query q = session.createQuery(hql.toString());
			lista = q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return lista;
	}

	private List<Long> pesquisarTarefaSetorId(List<CampoTarefaValor> listaContato, boolean like) throws DaoException {
		Session session = retrieveSession();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (listaContato != null && listaContato.size() > 0) {
				StringBuffer hqlWhere = new StringBuffer("");
				StringBuffer hql = new StringBuffer(" Select ts.seq_tarefa_setor " + " from egab.tarefa_setor ts ");

				boolean inicio = true;
				for (CampoTarefaValor campo : listaContato) {
					String campoSemAcento = retirarAcentuacao(campo.getTipoCampoTarefa().getDescricao());
					hql.append(" ,(SELECT ct2.dsc_valor_campo as valor , ct2.seq_tarefa_setor as tarefa " + "    FROM egab.campo_tarefa ct2 "
							+ "   WHERE ct2.seq_tipo_campo_tarefa = " + campo.getTipoCampoTarefa().getId() + ")" + campoSemAcento);

					hqlWhere.append(inicio ? " WHERE " : " AND ");
					if (like && campo.getTipoCampoTarefa().getTipo().equals(ClassificacaoTipoCampoTarefa.TL)) {
						hqlWhere.append(campoSemAcento + ".valor LIKE ('%" + campo.getValor().toUpperCase() + "%') ");
					} else {
						hqlWhere.append(campoSemAcento + ".valor = '" + campo.getValor().toUpperCase() + "' ");
					}
					hqlWhere.append(" AND " + campoSemAcento + ".tarefa = ts.seq_tarefa_setor ");
					inicio = false;
				}
				hql.append(hqlWhere);
				// System.out.println(hql.toString());
				stmt = session.connection().createStatement();
				rs = stmt.executeQuery(hql.toString());
				List<Long> lista = new LinkedList<Long>();
				while (rs.next()) {
					lista.add(rs.getLong("seq_tarefa_setor"));
				}

				/**
				 * Quando a lista for vazia, é acrescido o valor zero para que a pesquisa pai(createTarefaSetor) não retorne nenhum registro.
				 * 
				 * @Guilhermea
				 */
				if (lista.size() == 0) {
					lista.add((long) 0);
				}

				return lista;
			}

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();

				if (rs != null)
					rs.close();

			} catch (SQLException e) {
				throw new DaoException("SQLException", e);
			}
		}
		return null;
	}

	public TarefaSetor recuperarTarefaSetor(List<CampoTarefaValor> listaContato) throws DaoException {

		try {
			List<Long> lista = pesquisarTarefaSetorId(listaContato, false);
			if (lista != null && lista.size() > 0) {
				return recuperarTarefaSetor((Long) lista.get(0), null, null, null);
			}
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return null;
	}

	private static String retirarAcentuacao(String acentuada) {

		char[] acentuados = new char[] { 'ç', 'á', 'à', 'ã', 'â', 'ä', 'é', 'è', 'ê', 'ë', 'í', 'ì', 'î', 'ï', 'ó', 'ò', 'õ', 'ô', 'ö', 'ú', 'ù', 'û', 'ü' };

		char[] naoAcentuados = new char[] { 'c', 'a', 'a', 'a', 'a', 'a', 'e', 'e', 'e', 'e', 'i', 'i', 'i', 'i', 'o', 'o', 'o', 'o', 'o', 'u', 'u', 'u', 'u' };

		for (int i = 0; i < acentuados.length; i++) {
			acentuada = acentuada.replace(acentuados[i], naoAcentuados[i]);
			acentuada = acentuada.replace(Character.toUpperCase(acentuados[i]), Character.toUpperCase(naoAcentuados[i]));
		}
		return acentuada;
	}

	public List<Contato> pesquisarContato(Long id, Long tipoTarefaSetor, Long idTipoCampoTarefa, String valorCampo, Long idSetor) throws DaoException {
		Session session = retrieveSession();

		List<Contato> lista = new LinkedList<Contato>();
		Statement stmt = null;
		ResultSet rs = null;

		try {

			StringBuffer sql = new StringBuffer(" SELECT DISTINCT ( select ct2.DSC_VALOR_CAMPO " + " 					from egab.campo_Tarefa ct2 "
					+ " 				   where ct2.SEQ_TIPO_CAMPO_TAREFA = " + TipoContato.NOME.getCodigo() + " 				   and ct2.seq_tarefa_setor = ct.seq_tarefa_setor)Nome, "
					+ " 				  ( select ct2.DSC_VALOR_CAMPO " + " 					 from egab.campo_Tarefa ct2 " + " 					where ct2.SEQ_TIPO_CAMPO_TAREFA =  "
					+ TipoContato.TELEFONE.getCodigo() + " 					and ct2.seq_tarefa_setor = ct.seq_tarefa_setor)Telefone, " + " 				  ( select ct2.DSC_VALOR_CAMPO "
					+ " 					 from egab.campo_Tarefa ct2 " + " 					where ct2.SEQ_TIPO_CAMPO_TAREFA = " + TipoContato.ORIGEM.getCodigo()
					+ " 					and ct2.seq_tarefa_setor = ct.seq_tarefa_setor)Origem " + " from EGAB.CAMPO_TAREFA ct, EGAB.TAREFA_SETOR ts  "
					+ " where ct.SEQ_TAREFA_SETOR=ts.SEQ_TAREFA_SETOR  ");

			if (tipoTarefaSetor != null) {
				sql.append(" AND ts.SEQ_TIPO_TAREFA=" + tipoTarefaSetor);
			}

			if (idTipoCampoTarefa != null) {
				sql.append(" AND ct.SEQ_TIPO_CAMPO_TAREFA=" + idTipoCampoTarefa);
			}

			if (valorCampo != null && valorCampo.trim().length() > 0) {
				sql.append(" AND UPPER(ct.DSC_VALOR_CAMPO)like'%" + valorCampo.toUpperCase() + "%'");
			}

			if (idSetor != null) {
				sql.append(" AND COD_SETOR_ORIGEM = " + idSetor + " AND COD_SETOR_DESTINO = " + idSetor);
			}

			sql.append(" order by nome");

			// System.out.println(sql.toString());
			stmt = session.connection().createStatement();
			rs = stmt.executeQuery(sql.toString());
			while (rs.next()) {
				Contato contato = new Contato();
				contato.setNome(rs.getString("Nome"));
				contato.setTelefone(rs.getString("Telefone"));
				contato.setOrigem(rs.getString("Origem"));
				lista.add(contato);
			}

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}

				if (rs != null) {
					rs.close();
				}

			} catch (SQLException e) {
				throw new DaoException("SQLExceptionFinally", e);
			}
		}

		return lista;
	}

}
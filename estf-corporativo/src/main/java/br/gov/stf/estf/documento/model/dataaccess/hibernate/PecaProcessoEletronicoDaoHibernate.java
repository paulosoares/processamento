package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.PecaProcessoEletronicoDao;
import br.gov.stf.estf.documento.model.service.enums.TipoDeAcessoDoDocumento;
import br.gov.stf.estf.documento.model.util.PecaProcessoEletronicoDoTextoDynamicQuery;
import br.gov.stf.estf.documento.model.util.PecaProcessoEletronicoDynamicRestriction;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoEletronicoView;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TipoProcessoEletronicoConstante;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.util.query.QueryBuilder;

@Repository
public class PecaProcessoEletronicoDaoHibernate extends
		GenericHibernateDao<PecaProcessoEletronico, Long> implements
		PecaProcessoEletronicoDao {

	private static final long serialVersionUID = -1925126809844604234L;

	public PecaProcessoEletronicoDaoHibernate() {
		super(PecaProcessoEletronico.class);
	}

	@Override
	public PecaProcessoEletronico recuperarPeca(
			DocumentoEletronico documentoEletronico) throws DaoException {
		PecaProcessoEletronico pecaProcessoEletronico = null;

		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder();
			hql.append("SELECT ppe FROM PecaProcessoEletronico ppe, ArquivoProcessoEletronico ape ");
			hql.append("WHERE ppe.id = ape.pecaProcessoEletronico.id ");
			hql.append("AND ape.documentoEletronico.id = ?");

			Query query = session.createQuery(hql.toString());
			query.setLong(0, documentoEletronico.getId());

			pecaProcessoEletronico = (PecaProcessoEletronico) query
					.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return pecaProcessoEletronico;
	}

	/**
	 * Recupera a lista de peças caso existam processos em lote ou uma única
	 * peça
	 */
	public List<PecaProcessoEletronico> recuperarListaPecasComunicacao(
			DocumentoEletronico documentoEletronico) throws DaoException {
		List<PecaProcessoEletronico> listaPecaProcessoEletronico = null;

		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder();
			hql.append("SELECT ppe FROM PecaProcessoEletronico ppe, ArquivoProcessoEletronico ape ");
			hql.append("WHERE ppe.id = ape.pecaProcessoEletronico.id ");
			hql.append("AND ape.documentoEletronico.id = ?");

			Query q = session.createQuery(hql.toString());
			q.setLong(0, documentoEletronico.getId());
			listaPecaProcessoEletronico = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return listaPecaProcessoEletronico;
	}

	@Override
	public PecaProcessoEletronico recuperaPecaComunicacao(Long idDocumento, Long idObjetoIncidente) throws DaoException {

	
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT ppe ");
		hql.append(" FROM DocumentoEletronicoView vw_doc, PecaProcessoEletronico ppe, ArquivoProcessoEletronico ape ");
		hql.append(" WHERE vw_doc.descricaoStatusDocumento = ? ");
		hql.append(" AND ape.documentoEletronicoView.id = vw_doc.id ");
		hql.append(" AND ape.pecaProcessoEletronico.id = ppe.id ");
		hql.append(" AND  vw_doc.id = ?  ");
		hql.append(" AND  ppe.objetoIncidente.id = ?  ");

		Query q = session.createQuery(hql.toString());

		q.setString(0, DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO);
		q.setLong(1, idDocumento);
		q.setLong(2, idObjetoIncidente);
		List<PecaProcessoEletronico> pecas = q.list();
		
		
		
		return pecas.get(0);		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<PecaProcessoEletronico> pesquisarPecaProcessoEletronico(
			String siglaClasseProcessual, Long numeroProcessual,
			Long numeroProtocolo, Short anoProtocolo, Long idDocumentoEletronico)
			throws DaoException {
		List<PecaProcessoEletronico> result = null;

		try {
			Session session = retrieveSession();

			Criteria criteria = session.createCriteria(
					PecaProcessoEletronico.class, "ppe");

			if (numeroProtocolo != null || anoProtocolo != null) {

				criteria = criteria.createAlias("ppe.protocolo", "pt",
						CriteriaSpecification.INNER_JOIN).setFetchMode("pt",
						FetchMode.JOIN);

				if (numeroProtocolo != null)
					criteria.add(Restrictions.eq("pt.id.numero",
							numeroProtocolo));

				if (anoProtocolo != null)
					criteria.add(Restrictions.eq("pt.id.ano", anoProtocolo));
			}

			if (siglaClasseProcessual != null || numeroProcessual != null) {

				if (numeroProtocolo == null && anoProtocolo == null)
					criteria = criteria.createAlias("ppe.protocolo", "pt",
							CriteriaSpecification.INNER_JOIN).setFetchMode(
							"pt", FetchMode.JOIN);

				criteria = criteria.createAlias("pt.processo", "pc",
						CriteriaSpecification.INNER_JOIN).setFetchMode("pc",
						FetchMode.JOIN);

				if (siglaClasseProcessual != null)
					criteria.add(Restrictions.eq("pc.id.siglaClasseProcessual",
							siglaClasseProcessual));

				if (numeroProcessual != null)
					criteria.add(Restrictions.eq("pc.id.numeroProcessual",
							numeroProcessual));
			}

			result = criteria.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<PecaProcessoEletronico> pesquisar(String sigla, Long numero,
			Long codRecurso, String julgamento, Long seqArquivoEletronico)
			throws DaoException {
		List<PecaProcessoEletronico> pecas = null;

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ppe ");
			hql.append(" FROM DocumentoEletronicoView vw_doc, DocumentoTexto dt, Texto tt, Processo p, PecaProcessoEletronico ppe, ArquivoProcessoEletronico ape ");
			hql.append(" WHERE vw_doc.descricaoStatusDocumento = ? ");
			hql.append(" AND dt.id = vw_doc.id ");
			hql.append(" AND tt.id = dt.texto.id ");
			hql.append(" AND p.id.siglaClasseProcessual = tt.classeProcesso.id ");
			hql.append(" AND p.id.numeroProcessual = tt.numeroProcesso ");
			hql.append(" AND ppe.protocolo.id.numero = p.protocolo.id.numero ");
			hql.append(" AND ppe.protocolo.id.ano = p.protocolo.id.ano ");
			hql.append(" AND ape.documentoEletronicoView.id = vw_doc.id ");
			hql.append(" AND ape.pecaProcessoEletronico.id = ppe.id ");
			hql.append(" AND tt.arquivoEletronico.id = ? ");
			hql.append(" AND tt.numeroProcesso = ? ");
			hql.append(" AND tt.classeProcesso.id = ? ");
			hql.append(" AND tt.tipoRecurso.id = ? ");
			hql.append(" AND tt.tipoJulgamento.id = ? ");

			Query q = session.createQuery(hql.toString());

			q.setString(0, DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO);
			q.setLong(1, seqArquivoEletronico);
			q.setLong(2, numero);
			q.setString(3, sigla);
			q.setLong(4, codRecurso);
			q.setString(5, julgamento);

			pecas = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return pecas;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PecaProcessoEletronico> pesquisar(
			PecaProcessoEletronicoDoTextoDynamicQuery consulta)
			throws DaoException {
		try {
			String hql = getHqlPecaProcessoEletronicoTexto();
			QueryBuilder montadorDeQuery = new QueryBuilder(getSession(), hql,
					consulta);
			Query query = montadorDeQuery.getQuery();
			return query.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	private String getHqlPecaProcessoEletronicoTexto() {
		return "SELECT pe  FROM ArquivoProcessoEletronico ae, PecaProcessoEletronico pe, "
				+ " DocumentoEletronicoView doc, DocumentoTexto dt, Texto tx";
	}

	public PecaProcessoEletronico recuperar(ObjetoIncidente<?> objetoIncidente,
			String siglaTipoPecaProcesso) throws DaoException {
		PecaProcessoEletronico peca = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PecaProcessoEletronico.class);
			c.add(Restrictions.eq("objetoIncidente.id", objetoIncidente.getId()));
			c.add(Restrictions.eq("tipoPecaProcesso.sigla",
					siglaTipoPecaProcesso));

			peca = (PecaProcessoEletronico) c.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return peca;
	}

	@Override
	public Long recuperarNumeroDeOrdemMaximo(ObjetoIncidente<?> objetoIncidente)
			throws DaoException {
		Criteria criteria = retrieveSession().createCriteria(
				PecaProcessoEletronico.class);
		ProjectionList projecao = Projections.projectionList();
		projecao.add(Projections.max("numeroOrdemPeca"));
		criteria.setProjection(projecao);
		criteria.add(Restrictions.eq("objetoIncidenteProcesso.id", objetoIncidente.getPrincipal().getId()));
		Long numeroDeOrdem = (Long) criteria.uniqueResult();
		if (numeroDeOrdem == null) {
			numeroDeOrdem = 0L;
		}
		return numeroDeOrdem;
	}

	@Override
	public void persistirPecaProcessoEletronico(
			PecaProcessoEletronico pecaProcessoEletronico) throws DaoException {
		Session session = retrieveSession();
		try {
			session.persist(pecaProcessoEletronico);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	@Override
	public void excluirPecaProcessoEletronico(
			PecaProcessoEletronico pecaProcessoEletronico) throws DaoException {
		excluirPecaProcessoEletronico(pecaProcessoEletronico, "");
	}

	/**
	 * A exlusão é lógica. Verificar o comentário da interface.
	 * 
	 * @see br.gov.stf.estf.documento.model.dataaccess.IDaoPecaProcessoEletronico#excluirPecaProcessoEletronico(br.gov.stf.estf.documento.modelo.PecaProcessoEletronico)
	 */
	public void excluirPecaProcessoEletronico(
			PecaProcessoEletronico pecaProcessoEletronico, String motivo)
			throws DaoException {
		excluirPecaProcessoEletronico(pecaProcessoEletronico, motivo, true);
	}

	@Override
	public void excluirPecaProcessoEletronico(
			PecaProcessoEletronico pecaProcessoEletronico, String motivo,
			boolean cancelarPDF) throws DaoException {
		try {
			if (pecaProcessoEletronico != null) {
				pecaProcessoEletronico
						.setTipoSituacaoPeca(TipoSituacaoPeca.EXCLUIDA);
				salvar(pecaProcessoEletronico);
				// Só vai entrar aqui se for necessário cancelar o PDF. O
				// Decisão não entra.
				if (cancelarPDF) {
					cancelaPdfsDaPeca(pecaProcessoEletronico, motivo);
				}
			}
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (Throwable t) {
			throw new DaoException("HibernateException", t);
		}
	}

	@Override
	public void cancelaPdfsDaPeca(
			PecaProcessoEletronico pecaProcessoEletronico, String motivo)
			throws DaoException {
		Session session = retrieveSession();

		try {
			Set<Long> documentosParaCancelar = carregaPdfsParaCancelar(pecaProcessoEletronico);
			for (Long idDocumentoParaCancelar : documentosParaCancelar) {
				StopWatch stopWatch = new Log4JStopWatch();
				
				SessionImplementor callableSession = (SessionImplementor) session;
				CallableStatement storedProcedureStmt = callableSession
						.getBatcher()
						.prepareCallableStatement(
								"{call DOC.PKG_DOCUMENTO.PRC_CANCELA_DOCUMENTO(?,?)}");
				storedProcedureStmt.setLong(1, idDocumentoParaCancelar);
				storedProcedureStmt.setString(2, motivo);
				storedProcedureStmt.execute();
				callableSession.getBatcher()
						.closeStatement(storedProcedureStmt);
				
				stopWatch.stop("CancelaDocumento");
			}
		} catch (SQLException exception) {
			throw new DaoException(exception);
		} finally {
			session.flush();
		}
	}

	private Set<Long> carregaPdfsParaCancelar(
			PecaProcessoEletronico pecaProcessoEletronico) throws DaoException {
		Set<Long> documentosParaCancelar = new HashSet<Long>();
		pecaProcessoEletronico = recuperarPorId(pecaProcessoEletronico.getId());
		if (pecaProcessoEletronico.getDocumentos() != null) {
			for (ArquivoProcessoEletronico pecaDocumento : pecaProcessoEletronico.getDocumentos()) {
				if (pecaDocumento != null) {
					DocumentoEletronicoView documento = pecaDocumento
							.getDocumentoEletronicoView();
					if (documento != null
							&& !DocumentoEletronico.SIGLA_DESCRICAO_STATUS_CANCELADO
									.equals(documento.getDescricaoStatusDocumento())) {
						documentosParaCancelar.add(documento.getId());
					}
				}
			}
		}
		
		return documentosParaCancelar;
	}

	@SuppressWarnings("unchecked")
	public List<PecaProcessoEletronico> pesquisarPecaProcessoEletronico(
			Long numeroProtocolo, Short anoProtocolo) throws DaoException {

		Session session = retrieveSession();
		List<PecaProcessoEletronico> pecas = null;

		try {
			Criteria criteria = session
					.createCriteria(PecaProcessoEletronico.class);
			criteria.add(Restrictions.eq("numeroProtocolo", numeroProtocolo));
			criteria.add(Restrictions.eq("anoProtocolo", anoProtocolo));
			pecas = (List<PecaProcessoEletronico>) criteria.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return pecas;
	}

	// WARN: Método utilizado no digitalizador de peças do e-STF
	@SuppressWarnings("unchecked")
	public List<PecaProcessoEletronico> pesquisarPecaProcessoEletronico(
			Long numeroProtocolo, Short anoProtocolo, Long codSetor)
			throws DaoException {
		Session session = retrieveSession();
		session.flush();
		List<PecaProcessoEletronico> pecas = null;

		try {
			Criteria criteria = session.createCriteria(
					PecaProcessoEletronico.class).add(
					Restrictions
							.conjunction()
							.add(Restrictions.eq("numeroProtocolo",
									numeroProtocolo))
							.add(Restrictions.eq("anoProtocolo", anoProtocolo))
							.add(Restrictions
									.disjunction()
									.add(Restrictions.eq("tipoSituacaoPeca.id",
											3L))
									.add(Restrictions
											.conjunction()
											.add(Restrictions.eq(
													"tipoSituacaoPeca.id", 2L))
											.add(Restrictions.eq("setor.id",
													codSetor)))));
			pecas = criteria.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		if (pecas != null && pecas.size() > 0) {
			return pecas;
		} else {
			return null;
		}

	}

	public Long recuperarProximaOrdemPecaProcessoEletronico(
			Long numeroProtocolo, Short anoProtocolo) throws DaoException {
		Session session = retrieveSession();
		Long ordemAtual = null;
		try {
			String hql = " SELECT MAX(p.numeroOrdemPeca) FROM PecaProcessoEletronico p"
					+ " WHERE ( 1 = 1 ) AND "
					+ " p.numeroProtocolo = "
					+ numeroProtocolo + " AND p.anoProtocolo = " + anoProtocolo;
			ordemAtual = (Long) session.createQuery(hql).uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		if (ordemAtual != null) {
			return ordemAtual + 1L;
		} else {
			return 1L;
		}
	}

	public PecaProcessoEletronico recuperarPecaProcessoEletronico(Long id)
			throws DaoException {

		Session session = retrieveSession();
		PecaProcessoEletronico peca = null;

		try {
			Criteria criteria = session
					.createCriteria(PecaProcessoEletronico.class);
			criteria.add(Restrictions.idEq(id));
			peca = (PecaProcessoEletronico) criteria.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return peca;
	}

	public PecaProcessoEletronico recuperarPecaProcessoEletronico(
			String classe, Long numeroProcesso, Long numeroProtocolo,
			Short anoProtocolo, Long codSetor,
			TipoProcessoEletronicoConstante tipo) throws DaoException {
		Session session = retrieveSession();
		PecaProcessoEletronico peca = null;

		try {
			StringBuffer hql = new StringBuffer("SELECT pe FROM "
					+ "PecaProcessoEletronico pe ");
			if (classe != null && classe.trim().length() > 0
					&& numeroProcesso != null) {
				hql.append(",Processo p ");
			}
			hql.append("WHERE (1=1)");

			if (classe != null && classe.trim().length() > 0
					&& numeroProcesso != null) {
				hql.append(" AND p.siglaClasseProcessual = '" + classe + "'");
				hql.append(" AND p.numeroProcessual = " + numeroProcesso);
			}

			if (numeroProtocolo != null && anoProtocolo != null) {
				hql.append(" AND pe.numeroProtocolo = " + numeroProtocolo);
				hql.append(" AND pe.anoProtocolo = " + anoProtocolo);

			}

			if (codSetor != null) {
				hql.append(" AND pe.setor.id = " + codSetor);
			}

			if (tipo != null) {
				hql.append(" AND pe.tipoPecaProcesso.id = " + tipo.getCodigo());
			}

			if (classe != null && classe.trim().length() > 0
					&& numeroProcesso != null) {
				hql.append(" AND pe.numeroProtocolo = p.protocolo.numeroProtocolo");
				hql.append(" AND pe.anoProtocolo = p.protocolo.anoProtocolo");
			}
			Query q = session.createQuery(hql.toString());
			peca = (PecaProcessoEletronico) q.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return peca;
	}

	@SuppressWarnings("unchecked")
	public List<PecaProcessoEletronico> pesquisar(String siglaClasseProcesso,
			Long numeroProcesso, Long codigoRecurso, String tipoJulgamento,
			Long tipoPecaProcesso, Boolean pesquisarCancelados)
			throws DaoException {
		List<PecaProcessoEletronico> pecas = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PecaProcessoEletronico.class);
			if (siglaClasseProcesso != null
					&& siglaClasseProcesso.trim().length() > 0) {
				c.add(Restrictions.eq("processo.id.siglaClasseProcessual",
						siglaClasseProcesso));
			}

			if (numeroProcesso != null && numeroProcesso.intValue() > 0) {
				c.add(Restrictions.eq("processo.id.numeroProcessual",
						numeroProcesso));
			}

			if (codigoRecurso != null && codigoRecurso.intValue() > 0) {
				c.add(Restrictions.eq("tipoRecurso.id", codigoRecurso));
			}

			if (tipoJulgamento != null && tipoJulgamento.trim().length() > 0) {
				c.add(Restrictions.eq("tipoJulgamento.id", tipoJulgamento));
			}

			if (tipoPecaProcesso != null && tipoPecaProcesso.intValue() > 0) {
				c.add(Restrictions.eq("tipoPecaProcesso", tipoPecaProcesso));
			}

			if (pesquisarCancelados != null) {
				if (pesquisarCancelados) {
					c.add(Restrictions.eq("tipoSituacaoPeca.id",
							Long.valueOf(1)));
				} else {
					c.add(Restrictions.isNotEmpty("arquivoProcessoEletronico"));
				}
			}

			pecas = c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return pecas;

	}

	@SuppressWarnings("unchecked")
	public List<PecaProcessoEletronico> pesquisar(
			PecaProcessoEletronicoDynamicRestriction consulta)
			throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session
				.createCriteria(
						PecaProcessoEletronico.class,
						PecaProcessoEletronicoDynamicRestriction.ALIAS_PECA_PROCESSO_ELETRONICO);
		consulta.addCriteriaRestrictions(criteria);
		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	public List<PecaProcessoEletronico> pesquisar(
			ObjetoIncidente<?> objetoIncidente,
			TipoSituacaoPeca... tipoSituacaoPecao) throws DaoException {

		List<PecaProcessoEletronico> pecas = null;
		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ppe FROM PecaProcessoEletronico ppe ");
			hql.append(" WHERE ppe.objetoIncidente.id = ? ");
			boolean possuiSituacaoPeca = false;
			if (tipoSituacaoPecao != null && tipoSituacaoPecao.length > 0) {
				hql.append(" AND ppe.tipoSituacaoPeca IN (:situacoes) ");
				possuiSituacaoPeca = true;
			}

			Query q = session.createQuery(hql.toString());
			q.setLong(0, objetoIncidente.getId());

			if (possuiSituacaoPeca) {
				Long codigos[] = new Long[tipoSituacaoPecao.length];
				for (int i = 0; i < tipoSituacaoPecao.length; i++) {
					codigos[i] = tipoSituacaoPecao[i].getCodigo();
				}
				q.setParameterList("situacoes", codigos);
			}

			pecas = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return pecas;

	}

	@Override
	public boolean temPecasPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws DaoException {

		Session session = retrieveSession();

		String sql = "select count(1) from stf.peca_processo_eletronico ppe, stf.arquivo_processo_eletronico ape, doc.vw_documento doc "
				+ "where ppe.seq_peca_proc_eletronico = ape.seq_peca_proc_eletronico "
				+ "and ape.seq_documento = doc.seq_documento "
				+ "and ppe.seq_tipo_situacao_peca = :tipoSituacaoPeca "
				+ "and doc.tip_acesso = :tipoAcesso "
				+ "and ppe.seq_objeto_incidente = :idObjetoIncidente ";

		Query query = session.createSQLQuery(sql);
		query.setLong("tipoSituacaoPeca", TipoSituacaoPeca.JUNTADA.getCodigo());
		query.setString("tipoAcesso", TipoDeAcessoDoDocumento.INTERNO.getChave());
		query.setLong("idObjetoIncidente", objetoIncidente.getId());

		Number number = (Number) query.uniqueResult();

		return number != null && number.intValue() > 0;
	}
	
	@Override
	public List<PecaProcessoEletronico> pecasPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		try {			
			Session session = retrieveSession();

			String sql = "select ppe.seq_peca_proc_eletronico from stf.peca_processo_eletronico ppe, stf.arquivo_processo_eletronico ape, doc.vw_documento doc "
					+ "where ppe.seq_peca_proc_eletronico = ape.seq_peca_proc_eletronico "
					+ "and ape.seq_documento = doc.seq_documento "
					+ "and doc.tip_acesso = :tipoAcesso "
					+ "and ppe.seq_objeto_incidente = :idObjetoIncidente ";

			Query query = session.createSQLQuery(sql);
			query.setString("tipoAcesso", TipoDeAcessoDoDocumento.INTERNO.getChave());
			query.setLong("idObjetoIncidente", objetoIncidente.getId());

			List listaSeqDoc = query.list();
			
			if(listaSeqDoc != null && !listaSeqDoc.isEmpty()){
				
				StringBuilder hql = new StringBuilder();
				hql.append("SELECT ppe FROM PecaProcessoEletronico ppe ");
				hql.append("WHERE ppe.id IN (:listaIds) ");

				List<Long> listaIds = new ArrayList<Long>();
				for(Object x : listaSeqDoc){
					listaIds.add(((BigDecimal) x).longValue());
				}
				
				Query q = session.createQuery(hql.toString());
				q.setParameterList("listaIds", listaIds);
				return q.list();							
			}
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return null;
	}

	@Override
	public List<PecaProcessoEletronico> pecasJuntadasPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		try {			
			Session session = retrieveSession();

			String sql = "select ppe.seq_peca_proc_eletronico from stf.peca_processo_eletronico ppe, stf.arquivo_processo_eletronico ape, doc.vw_documento doc "
					+ "where ppe.seq_peca_proc_eletronico = ape.seq_peca_proc_eletronico "
					+ "and ape.seq_documento = doc.seq_documento "
					+ "and ppe.seq_tipo_situacao_peca = :tipoSituacaoPeca "
					+ "and doc.tip_acesso = :tipoAcesso "
					+ "and ppe.seq_objeto_incidente = :idObjetoIncidente ";

			Query query = session.createSQLQuery(sql);
			query.setLong("tipoSituacaoPeca", TipoSituacaoPeca.JUNTADA.getCodigo());
			query.setString("tipoAcesso", TipoDeAcessoDoDocumento.INTERNO.getChave());
			query.setLong("idObjetoIncidente", objetoIncidente.getId());

			List listaSeqDoc = query.list();
			
			if(listaSeqDoc != null && !listaSeqDoc.isEmpty()){
				
				StringBuilder hql = new StringBuilder();
				hql.append("SELECT ppe FROM PecaProcessoEletronico ppe ");
				hql.append("WHERE ppe.id IN (:listaIds) ");

				List<Long> listaIds = new ArrayList<Long>();
				for(Object x : listaSeqDoc){
					listaIds.add(((BigDecimal) x).longValue());
				}
				
				Query q = session.createQuery(hql.toString());
				q.setParameterList("listaIds", listaIds);
				return q.list();							
			}
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return null;
	}

	@Override
	public void tornarPublicasPecasPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		try {			
			Session session = retrieveSession();

			String sql = " UPDATE doc.vw_documento doc		 									"
					   + " SET doc.tip_acesso = :tipoAcesso  									"
					   + " WHERE EXISTS (					 								 	"
					   + " SELECT 1 														 	"
					   + " FROM stf.peca_processo_eletronico ppe							 	"
					   + "	  , stf.arquivo_processo_eletronico ape							 	"
					   + " WHERE  ppe.seq_peca_proc_eletronico = ape.seq_peca_proc_eletronico 	"
					   + "   and ppe.seq_objeto_incidente = :idObjetoIncidente 					"
					   + "    and ape.seq_documento = doc.seq_documento 						"
					   + " )																	";

			Query query = session.createSQLQuery(sql);
			query.setString("tipoAcesso", TipoDeAcessoDoDocumento.PUBLICO.getChave());
			query.setLong("idObjetoIncidente", objetoIncidente.getId());

			query.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}

	@Override
	public List<PecaProcessoEletronico> pesquisarPorProcesso(Processo processo,
			Boolean incluirCancelados) throws DaoException {
		
		List<PecaProcessoEletronico> pecaProcessoEletronicos = new ArrayList<PecaProcessoEletronico>();
		Session session = retrieveSession();

		try {
			StringBuffer hql = new StringBuffer();

			hql.append(" SELECT ppe FROM PecaProcessoEletronico ppe ");
			hql.append("WHERE ppe.objetoIncidenteProcesso.id = :processoId ");
			
			if(incluirCancelados != null && !incluirCancelados){
				hql.append(" AND ppe.tipoSituacaoPeca != 1 ");
			}
			
			hql.append( "ORDER BY ppe.numeroOrdemPeca ");
			
			Query query = session.createQuery(hql.toString());

			query.setLong("processoId", processo.getId());

			pecaProcessoEletronicos = query.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return pecaProcessoEletronicos;
	}

	@Override
	public List<PecaProcessoEletronico> listarPecas(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		List<PecaProcessoEletronico> listaPecaProcessoEletronico = null;

		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder();
			hql.append("SELECT ppe FROM PecaProcessoEletronico ppe, ArquivoProcessoEletronico ape ");
			hql.append("WHERE ppe.id = ape.pecaProcessoEletronico.id ");
			hql.append("AND ppe.objetoIncidente = :objetoIncidente");

			Query q = session.createQuery(hql.toString());
			q.setParameter("objetoIncidente", objetoIncidente);
			listaPecaProcessoEletronico = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return listaPecaProcessoEletronico;
	}

}

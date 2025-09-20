package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.engine.SessionImplementor;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.TextoDao;
import br.gov.stf.estf.documento.model.util.DadosDoTextoDynamicRestriction;
import br.gov.stf.estf.documento.model.util.TextoDynamicQuery;
import br.gov.stf.estf.documento.model.util.TextoSearchData;
import br.gov.stf.estf.documento.model.util.TextoSearchData.TipoOrdem;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.publicacao.FaseTextoProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.util.query.QueryBuilder;
import br.gov.stf.framework.model.util.query.SQLOrder;
import br.gov.stf.framework.util.SearchData;
import br.gov.stf.framework.util.SearchResult;

@Repository
public class TextoDaoHibernate extends GenericHibernateDao<Texto, Long>
		implements TextoDao {

	private static final long serialVersionUID = -4958664420772533712L;
	private static final Long CODIGO_MINISTRO_PRESIDENTE = 1L;

	public TextoDaoHibernate() {
		super(Texto.class);
	}

	// metodo "helper" utilizado para realizar as pesquisas com criteria
	private Criteria buscar(String sigla, Long numero, Long codRecurso,
			String julgamento, TipoTexto tipoTexto, Long seqObjetoIncidente, Long codigoMinistro,
			Boolean orderAscDataSessao) throws DaoException {

		Session session = retrieveSession();
		Criteria c = session.createCriteria(Texto.class);
		if (sigla != null && sigla.trim().length() > 0) {
			c.add(Restrictions.eq("siglaClasseProcessual", sigla));
		}
		if (numero != null && numero > 0) {
			c.add(Restrictions.eq("numeroProcessual", numero));
		}
		if (codRecurso != null) {
			c.add(Restrictions.eq("codigoRecurso", codRecurso));
		}
		if (julgamento != null && julgamento.trim().length() > 0) {
			c.add(Restrictions.eq("tipoJulgamento.id", julgamento));
		}
		if (tipoTexto != null && tipoTexto.getCodigo() > 0) {
			c.add(Restrictions.eq("tipoTexto", tipoTexto));
		}
		if (seqObjetoIncidente != null && seqObjetoIncidente > 0) {
			c.add(Restrictions.eq("sequenciaObjetoIncidente", seqObjetoIncidente));
		}
		if (orderAscDataSessao != null && orderAscDataSessao) {
			c.addOrder(Order.asc("dataSessao"));
		}
		if (codigoMinistro != null && codigoMinistro != 0) {
			c.add(Restrictions.eq("ministro.id", codigoMinistro));
		}

		return c;
	}

	private Criteria buscar(ObjetoIncidente<?> objetoIncidente,
			TipoTexto tipoTexto, Long codigoMinistro, Boolean orderAscDataSessao)
			throws DaoException {

		Session session = retrieveSession();
		Criteria c = session.createCriteria(Texto.class);
		if (objetoIncidente != null) {
			c.add(Restrictions
					.eq("objetoIncidente.id", objetoIncidente.getId()));
		}
		if (tipoTexto != null) {
			c.add(Restrictions.eq("tipoTexto", tipoTexto));
		}
		if (orderAscDataSessao != null && orderAscDataSessao) {
			c.addOrder(Order.asc("dataSessao"));
		}
		if (codigoMinistro != null && codigoMinistro != 0) {
			c.add(Restrictions.eq("ministro.id", codigoMinistro));
		}

		return c;
	}

	public Texto recuperar(String sigla, Long numero, Long codRecurso,
			String julgamento, TipoTexto tipoTexto) throws DaoException {

		Texto result = null;

		try {

			result = (Texto) buscar(sigla, numero, codRecurso, julgamento,
					tipoTexto, null, null, null).uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisarDecisoesAta(String sigla, Long numero,
			Long codRecurso, String julgamento, Date dataAta)
			throws DaoException {
		List<Texto> textos = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Texto.class);

			c.add(Restrictions.eq("classeProcesso.id", sigla));
			c.add(Restrictions.eq("numeroProcesso", numero));
			c.add(Restrictions.eq("tipoRecurso.id", codRecurso));
			c.add(Restrictions.eq("tipoJulgamento.id", julgamento));
			c.add(Restrictions.eq("tipoTexto", TipoTexto.DECISAO));
			c.add(Restrictions.le("dataSessao", dataAta));
			c.addOrder(Order.asc("dataSessao"));

			textos = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return textos;
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisarDecisoesAta(ObjetoIncidente<?> objetoIncidente,
			Date dataAta) throws DaoException {
		List<Texto> textos = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Texto.class);

			c.add(Restrictions.eq("objetoIncidente", objetoIncidente));
			c.add(Restrictions.eq("tipoTexto", TipoTexto.DECISAO));
			c.add(Restrictions.le("dataSessao", dataAta));
			c.addOrder(Order.asc("dataSessao"));

			textos = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return textos;
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisar(String sigla, Long numero, Long codigoRecurso,
			String tipoJulgamento, Long codigoMinistro) throws DaoException {
		List<Texto> textos = null;
		try {
			Criteria c = buscar(sigla, numero, codigoRecurso, tipoJulgamento,
					null, null, codigoMinistro, false);
			textos = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return textos;
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisar(String sigla, Long numero, Long codRecurso,
			String julgamento, TipoTexto tipoTexto, Long codigoMinistro,
			Boolean orderAscDataSessao) throws DaoException {
		List<Texto> result = null;
		try {

			result = buscar(sigla, numero, codRecurso, julgamento, tipoTexto, null,
					codigoMinistro, orderAscDataSessao).addOrder(
					Order.asc("classeProcesso.id")).addOrder(
					Order.asc("numeroProcesso")).list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return result;
	}

	public SearchResult<Texto> pesquisar(TextoSearchData sd)
			throws DaoException {
		Session session = retrieveSession();
		try {

			Criteria c = session.createCriteria(Texto.class, "t");

			if (sd.objetoIncidente != null) {
				c.add(Restrictions.eq("t.objetoIncidente", sd.objetoIncidente));
			}
			if (sd.tipoTexto != null) {
				c.add(Restrictions.eq("t.tipoTexto", sd.tipoTexto));
			}
			if (sd.listaTipoTexto != null) {
				c.add(Restrictions.in("t.tipoTexto", sd.listaTipoTexto));
			}
			if (sd.codigoMinistro != null && sd.codigoMinistro != 0) {
				if (sd.ministroDiferenteAutenticado != null
						&& sd.ministroDiferenteAutenticado) {
					c.add(Restrictions.ne("t.ministro.id", sd.codigoMinistro));
				} else {
					c.add(Restrictions.eq("t.ministro.id", sd.codigoMinistro));
				}
			}

			if (sd.publico != null) {
				c.add(Restrictions.eq("t.publico", sd.publico));
			}

			if (sd.textosIguais != null) {
				c.add(Restrictions.eq("t.textosIguais", sd.textosIguais));
			}

			if (sd.idArquivoEletronico != null) {
				c.add(Restrictions.eq("t.arquivoEletronico.id",
						sd.idArquivoEletronico));
			}

			if (sd.ordem != null) {
				if (sd.ordem.equals(TipoOrdem.DATA_SESSAO)) {
					c.addOrder(Order.asc("t.dataSessao"));
				} else if (sd.ordem.equals(TipoOrdem.TIPO_TEXTO)) {
					// c.addOrder(Order.asc("t.tipoTexto.descricao"));
				} else if (sd.ordem.equals(TipoOrdem.TIPO_TEXTO_DATA_SESSAO)) {
					// c.addOrder(Order.asc("t.tipoTexto.descricao"));
					c.addOrder(Order.asc("t.dataSessao"));
				}
			}

			return pesquisarComPaginacaoCriteria(sd, c);

		} catch (Exception e) {
			throw new DaoException(e);
		}

	}
	
	public SearchResult<Texto> pesquisarTextoPorObjIncidenteTipoTextoMinistroOuPresidente(TextoSearchData sd)
			throws DaoException {
		Session session = retrieveSession();
		try {

			Criteria c = session.createCriteria(Texto.class, "t");

			if (sd.objetoIncidente != null) {
				c.add(Restrictions.eq("t.objetoIncidente", sd.objetoIncidente));
			}
			if (sd.tipoTexto != null) {
				c.add(Restrictions.eq("t.tipoTexto", sd.tipoTexto));
			}
			if (sd.codigoMinistro != null && sd.codigoMinistro != 0) {
				SimpleExpression ministro = Restrictions.eq("t.ministro.id", sd.codigoMinistro);
				SimpleExpression ministroPresidente = Restrictions.eq("t.ministro.id", CODIGO_MINISTRO_PRESIDENTE);
				c.add(Restrictions.or(ministro, ministroPresidente));
			}

			return pesquisarComPaginacaoCriteria(sd, c);

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public boolean verificarExistenciaTexto(TextoSearchData sd)
			throws DaoException {
			
		Session session = retrieveSession();

		List<Texto> textos = null;

		try {

			Criteria c = session.createCriteria(Texto.class, "t");

			if (sd.objetoIncidente != null) {
				c.add(Restrictions.eq("t.objetoIncidente", sd.objetoIncidente));
			}
			if (sd.tipoTexto != null) {
				c.add(Restrictions.eq("t.tipoTexto", sd.tipoTexto));
			}

			if (sd.codigoMinistro != null && sd.codigoMinistro != 0) {
				if (sd.ministroDiferenteAutenticado != null && sd.ministroDiferenteAutenticado) {
					c.add(Restrictions.ne("t.ministro.id", sd.codigoMinistro));
				} else {
					c.add(Restrictions.eq("t.ministro.id", sd.codigoMinistro));
				}
			}

			if (sd.publico != null) {
				c.add(Restrictions.eq("t.publico", (sd.publico ? "'S'" : "'N'")));

			}

			if (sd.textosIguais != null) {
				c.add(Restrictions.eq("t.textosIguais", (sd.textosIguais ? "'S'" : "'N'")));
			}

			if (sd.idArquivoEletronico != null) {
				c.add(Restrictions.eq("t.arquivoEletronico.id", sd.idArquivoEletronico));
			}

			textos = c.list();

			return textos.size() > 0 ? true : false;

		} catch (Exception e) {
			throw new DaoException(e);
		}	
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisarTextosIguais(Texto texto) throws DaoException {
		return pesquisarTextosIguais(texto, false);
	}

	public List<Texto> pesquisarTextosIguais(Texto texto,
			boolean limitarTextosDoMinistro) throws DaoException {
		List<Texto> textos = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Texto.class, "t");

			c.add(Restrictions.eq("t.arquivoEletronico.id", texto
					.getArquivoEletronico().getId()));
			c.add(Restrictions.eq("t.textosIguais", true));
			c.add(Restrictions.ne("t.id", texto.getId()));
			if (limitarTextosDoMinistro) {
				c.add(Restrictions.eq("t.ministro.id", texto.getMinistro()
						.getId()));
			}
			c.addOrder(Order.asc("t.siglaClasseProcessual"));
			c.addOrder(Order.asc("t.numeroProcessual"));
			c.addOrder(Order.asc("t.objetoIncidente.id"));
			

			textos = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return textos;
	}

	@SuppressWarnings("unchecked")
	public List<FaseTextoProcesso> recuperarVersoesTexto(Long textoId)
			throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(FaseTextoProcesso.class);
		criteria.add(Restrictions.eq("texto.id", textoId));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisarTextoDoProcesso(
			DadosDoTextoDynamicRestriction consultaDinamica)
			throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(Texto.class,
				DadosDoTextoDynamicRestriction.ALIAS_TEXTOS);
		consultaDinamica.addCriteriaRestrictions(criteria);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Texto> recuperarTextosProcessos(List<Processo> listaProcessos,
			List<TipoTexto> listaTipoTexto) throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(Texto.class);
		criteria.add(Restrictions.in("objetoIncidente", listaProcessos));
		if (listaTipoTexto != null) {
			criteria.add(Restrictions.in("tipoTexto", listaTipoTexto));
		}
		return criteria.list();
	}

	public void copiarConteudoArquivoEletronico(
			ArquivoEletronico arquivoEletronicoOrigem,
			ArquivoEletronico arquivoEletronicoDestino) throws DaoException {
		SessionImplementor session = (SessionImplementor) retrieveSession();
		Connection conn = session.connection();
		CallableStatement storedProcedureStmt = null;
		PreparedStatement pstmt = null;
		try {

			String prcRecuperaDocumento = "{call DOC.PKG_RECUPERA_DOCUMENTO.PRC_RECUPERA_DOCUMENTO_BINARIO(?,false)}";

			storedProcedureStmt = session.getBatcher()
					.prepareCallableStatement(prcRecuperaDocumento);

			storedProcedureStmt.setLong(1, arquivoEletronicoDestino.getId());
			// storedProcedureStmt.setBoolean(2, Boolean.FALSE);
			storedProcedureStmt.execute();
			session.getBatcher().closeStatement(storedProcedureStmt);

			storedProcedureStmt = session.getBatcher()
					.prepareCallableStatement(prcRecuperaDocumento);

			storedProcedureStmt.setLong(1, arquivoEletronicoOrigem.getId());
			// storedProcedureStmt.setBoolean(2, Boolean.FALSE);
			storedProcedureStmt.execute();
			session.getBatcher().closeStatement(storedProcedureStmt);

			StringBuffer sql = new StringBuffer();
			sql
					.append("UPDATE DOC.ARQUIVO_TMP SET TXT_CONTEUDO = (SELECT TXT_CONTEUDO FROM ");
			sql
					.append("   DOC.ARQUIVO_TMP WHERE SEQ_ARQUIVO_ELETRONICO = ?) WHERE ");
			sql.append("   SEQ_ARQUIVO_ELETRONICO = ?");

			pstmt = (PreparedStatement) conn.prepareStatement(sql.toString());
			pstmt.setLong(1, arquivoEletronicoOrigem.getId());
			pstmt.setLong(2, arquivoEletronicoDestino.getId());
			pstmt.execute();

			String prcGravaDocumento = "{call DOC.PRC_GRAVA_ARQUIVOCOMPUTADOR(?)}";
			storedProcedureStmt = session.getBatcher()
					.prepareCallableStatement(prcGravaDocumento);
			storedProcedureStmt.setLong(1, arquivoEletronicoDestino.getId());
			storedProcedureStmt.execute();
			session.getBatcher().closeStatement(storedProcedureStmt);

			String prcDeletaDocumento = "DELETE FROM DOC.ARQUIVO_TMP WHERE SEQ_ARQUIVO_ELETRONICO in (?,?)";
			storedProcedureStmt = session.getBatcher()
					.prepareCallableStatement(prcDeletaDocumento);
			storedProcedureStmt.setLong(1, arquivoEletronicoDestino.getId());
			storedProcedureStmt.setLong(2, arquivoEletronicoOrigem.getId());
			storedProcedureStmt.execute();
			session.getBatcher().closeStatement(storedProcedureStmt);

			retrieveSession().refresh(arquivoEletronicoDestino);

		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} finally {
			try {
				if (storedProcedureStmt != null)
					storedProcedureStmt.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw new DaoException("SQLException", e);
			}
		}

	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisarTextosExtratoAta(String siglaClasse,
			Long numProcesso, Long tipoRecurso, Long tipoJulgamento)
			throws DaoException {

		List<Texto> textos = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Texto.class);

			c.add(Restrictions.eq("classeProcesso.id", siglaClasse));
			c.add(Restrictions.eq("numeroProcesso", numProcesso));
			c.add(Restrictions.eq("tipoRecurso.id", tipoRecurso));
			c.add(Restrictions.eq("tipoJulgamento.id", tipoJulgamento));
			c.add(Restrictions.eq("tipoTexto", TipoTexto.DECISAO));
			c.createAlias("documentosTexto", "docTexto");
			c.add(Restrictions.eq("docTexto.tipoDocumentoTexto.id", 50L));
			c.createAlias("docTexto.tipoSituacaoDocumento",
					"tipoSituacaoDocumento");
			c.add(Restrictions.in("tipoSituacaoDocumento",
					new TipoSituacaoDocumento[] {
							TipoSituacaoDocumento.ASSINADO_DIGITALMENTE,
							TipoSituacaoDocumento.ASSINADO_MANUALMENTE }));
			c.createAlias("processo", "proc");
			c.add(Restrictions.eq("proc.codigoDestino", 600000048));

			c.addOrder(Order.asc("dataSessao"));
			c.addOrder(Order.asc("dataInclusao"));

			textos = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return textos;
	}

	public List<Texto> pesquisarTextosExtratoAta(
			TextoDynamicQuery consultaDinamica) throws DaoException {
		consultaDinamica.addOrder(
				TextoDynamicQuery.ALIAS_TEXTO + ".dataSessao",
				SQLOrder.ASCENDENTE);
		consultaDinamica.addOrder(TextoDynamicQuery.ALIAS_TEXTO
				+ ".dataInclusao", SQLOrder.ASCENDENTE);
		return pesquisarTexto(consultaDinamica);
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisarTexto(TextoDynamicQuery consultaDinamica)
			throws DaoException {
		QueryBuilder builder = montaConsultaDeTexto(consultaDinamica);
		Query query = builder.getQuery();
		return query.list();
	}

	private QueryBuilder montaConsultaDeTexto(TextoDynamicQuery consultaDinamica)
			throws DaoException {
		String hql = montaHqlDeTexto();
		QueryBuilder builder = new QueryBuilder(retrieveSession(), hql,
				consultaDinamica);
		return builder;
	}

	private String montaHqlDeTexto() {
		return "SELECT " + TextoDynamicQuery.ALIAS_TEXTO + " FROM "
				+ Texto.class.getSimpleName() + " "
				+ TextoDynamicQuery.ALIAS_TEXTO;
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisarTextosPublicacao(
			ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto,
			Boolean orderAscDataSessao, Date dataSessao) throws DaoException {
		List<Texto> result = null;
		try {

			Criteria c = buscar(objetoIncidente, tipoTexto, null,
					orderAscDataSessao);

			c.add(Restrictions.eq("publico", Boolean.TRUE));

			// TEXTOS DE PSV E DESPACHOS NÃO POSSUEM DATA DA SESSAO PREENCHIDA
			if (!TipoTexto.EDITAL_PROPOSTA_SUMULA_VINCULANTE.equals(tipoTexto)
					&& !TipoTexto.DESPACHO.equals(tipoTexto) && !TipoTexto.DECISAO_MONOCRATICA.equals(tipoTexto)) {
				c.add(Restrictions.isNotNull("dataSessao"));
				if (dataSessao != null) {
					c.add(Restrictions.le("dataSessao", dataSessao));
				}
			}

			result = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return result;
	}

	public Texto recuperarDecisaoAta(ObjetoIncidente<?> objetoIncidente,
			Date dataSessao) throws DaoException {
		Texto texto = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Texto.class);

			c.add(Restrictions
					.eq("objetoIncidente.id", objetoIncidente.getId()));
			c.add(Restrictions.eq("tipoTexto", TipoTexto.DECISAO));
			c.add(Restrictions.eq("dataSessao", dataSessao));

			texto = (Texto) c.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return texto;
	}

	public Texto recuperarTextoPublicacao(ObjetoIncidente<?> objetoIncidente,
			TipoTexto tipoTexto) throws DaoException {
		Texto result = null;

		try {

			Criteria c = buscar(objetoIncidente, tipoTexto, null, null);

			c.add(Restrictions.eq("publico", Boolean.TRUE));
			c.add(Restrictions.isNotNull("dataSessao"));

			result = (Texto) c.uniqueResult();
		} catch (NonUniqueResultException nurw) {
			throw new DaoException(
					"Mais de um texto encontrado: objetoIncidente"
							+ objetoIncidente + ", tipoTexto=" + tipoTexto,
					nurw);
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente,
			TipoTexto tipoTexto, Boolean orderAscDataSessao)
			throws DaoException {
		List<Texto> result = null;
		try {

			result = buscar(objetoIncidente, tipoTexto, null,
					orderAscDataSessao).list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente,
			Long codigoMinistro, Boolean orderAscDataSessao)
			throws DaoException {
		Session session = retrieveSession();
		Criteria c = session.createCriteria(Texto.class);

		if (objetoIncidente != null) {
			c.add(Restrictions.eq("objetoIncidente", objetoIncidente));
		}
		if (codigoMinistro != null) {
			c.add(Restrictions.eq("ministro.id", codigoMinistro));
		}
		if (orderAscDataSessao != null && orderAscDataSessao) {
			c.addOrder(Order.asc("dataSessao"));
		}

		return c.list();
	}

	public Texto recuperar(ObjetoIncidente<?> objetoIncidente,
			TipoTexto tipoTexto) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Texto.class);

			if (objetoIncidente != null) {
				c.add(Restrictions.eq("objetoIncidente", objetoIncidente));
			}
			if (tipoTexto != null) {
				c.add(Restrictions.eq("tipoTexto", tipoTexto));
			}
			return (Texto) c.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	public Texto recuperarTextoEletronico(Integer idArquivoEletronico)
			throws DaoException {
		Session session = retrieveSession();

		Criteria criteria = session.createCriteria(Texto.class);
		criteria.add(Restrictions.eq("codigoBrs", idArquivoEletronico));

		return (Texto) criteria.uniqueResult();

	}

	public Texto recuperar(Long idObjetoIncidente,
			TipoTexto tipoTexto, Long idMinistro) throws DaoException {

		Session session = retrieveSession();
		Criteria c = session.createCriteria(Texto.class);
		
		if (idObjetoIncidente != null)
			c.add(Restrictions.eq("objetoIncidente.id", idObjetoIncidente));
		
		if (tipoTexto != null)
			c.add(Restrictions.eq("tipoTexto", tipoTexto));

		if (idMinistro != null && idMinistro != 0)
			c.add(Restrictions.eq("ministro.id", idMinistro));

		return (Texto) c.uniqueResult();
	}

	@Override
	public Texto recuperarTextoSemControleVoto(ObjetoIncidente objetoIncidente, TipoTexto tipoTexto, Ministro ministro) throws DaoException {

		Session session = retrieveSession();
		Criteria c = session.createCriteria(Texto.class);
		
		if (objetoIncidente != null)
			c.add(Restrictions.eq("objetoIncidente", objetoIncidente));
		
		if (tipoTexto != null)
			c.add(Restrictions.eq("tipoTexto", tipoTexto));
		
		if (ministro != null)
			c.add(Restrictions.eq("ministro", ministro));
		
		c.add(Restrictions.or(Restrictions.isNull("sequenciaVoto"), Restrictions.eq("sequenciaVoto", 0L)));
		c.add(Restrictions.isNull("dataComposicaoParcial"));
		
		return (Texto) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente,
			TipoTexto... tipoTexto) throws DaoException {

		List<Texto> textos = null;
		try {
			Criteria c = retrieveSession().createCriteria(Texto.class);
			c.add(Restrictions.eq("objetoIncidente", objetoIncidente));
			c.add(Restrictions.in("tipoTexto", tipoTexto));
			c.addOrder(Order.asc("dataCriacao"));
			
			textos = c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return textos;
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente,
			TipoTexto tipoTexto, Ministro ministro) throws DaoException {
		List<Texto> textos = null;
		try {
			Criteria c = retrieveSession().createCriteria(Texto.class);
			c.add(Restrictions
					.eq("objetoIncidente.id", objetoIncidente.getId()));
			c.add(Restrictions.eq("tipoTexto", tipoTexto));
			if (ministro != null) {
				c.add(Restrictions.eq("ministro.id", ministro.getId()));
			}
			textos = c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return textos;
	}

	@SuppressWarnings("unchecked")
	public List<Texto> pesquisarTextosIguais(Long seqArquivoEletronico)
			throws DaoException {
		List<Texto> textos = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Texto.class);

			c
					.add(Restrictions.eq("arquivoEletronico.id",
							seqArquivoEletronico));
			c.add(Restrictions.eq("textosIguais", true));

			textos = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return textos;
	}

	public Texto recuperaArquivoEletronico(ControleVoto cv) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Texto.class);

			c.add(Restrictions.eq("objetoIncidente.id", cv.getObjetoIncidente()
					.getId()));
			c.add(Restrictions.eq("tipoTexto", cv.getTipoTexto()));
			c.add(Restrictions.eq("ministro.id", cv.getMinistro().getId()));
			c.add(Restrictions.eq("dataSessao", cv.getDataSessao()));
			c.add(Restrictions.eq("sequenciaVoto", cv.getSequenciaVoto()));

			return (Texto) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	public Texto recarregar(Texto texto) throws DaoException {
		try {
			Session session = retrieveSession();
			session.clear();
			texto = (Texto) session.get(Texto.class, texto.getId());
			return texto;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	public Texto recuperarTextoEmentaSobreRepercussaoGeral(Long seqObjetoIncidente, Long codigoMinistro) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Texto.class, "t");
			
			if(seqObjetoIncidente != null && seqObjetoIncidente > 0)
				c.add(Restrictions.eq("t.sequenciaObjetoIncidente", seqObjetoIncidente));
			
			if(codigoMinistro != null && codigoMinistro > 0)
				c.add(Restrictions.eq("t.codigoMinistro", codigoMinistro));
			
			c.add(Restrictions.eq("t.tipoTexto", TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL));
			
			return (Texto) c.uniqueResult();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	public Texto recuperarTextoDecisaoSobreRepercussaoGeral(Long seqObjetoIncidente, Long codigoMinistro) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Texto.class, "t");
			
			if(seqObjetoIncidente != null && seqObjetoIncidente > 0)
				c.add(Restrictions.eq("t.sequenciaObjetoIncidente", seqObjetoIncidente));
			
			if(codigoMinistro != null && codigoMinistro > 0)
				c.add(Restrictions.eq("t.codigoMinistro", codigoMinistro));
			
			c.add(Restrictions.eq("t.tipoTexto", TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL));
			
			return (Texto) c.uniqueResult();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Texto> pesquisarTextoRepercussaoGeralVotoValido(Long seqObjetoIncidente) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		
		List<Long> listaCodigoTipoTexto = new ArrayList<Long>();
		listaCodigoTipoTexto.add(TipoTexto.MANIFESTACAO_SOBRE_REPERCUSAO_GERAL.getCodigo());
		listaCodigoTipoTexto.add(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL.getCodigo());		
		listaCodigoTipoTexto.add(TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL.getCodigo());	

		try {
			hql.append("SELECT t FROM Texto t JOIN t.objetoIncidente oit JOIN t.ministro mt");
			
			hql.append(", JulgamentoProcesso jp JOIN jp.listaVotoJulgamentoProcesso ljp, Sessao s ");
			hql.append("WHERE ");
			hql.append("s.id = jp.sessao.id ");
			hql.append("AND t.objetoIncidente.id = jp.objetoIncidente.id ");
			hql.append("AND s.tipoAmbiente = 'V' ");
			hql.append("AND t.tipoTexto IN ( :listaTipoTexto ) ");
			hql.append("AND oit.id = :seqObjetoIncidente ");
			
			hql.append("ORDER BY ljp.numeroOrdemVotoSessao");
			
			Query q = session.createQuery(hql.toString());
			q.setLong("seqObjetoIncidente", seqObjetoIncidente);
			q.setParameterList("listaTipoTexto", listaCodigoTipoTexto);
			
			Set<Texto> listaTextosDoProcesso = new HashSet<Texto>((List<Texto>)q.list());
			
			return new ArrayList<Texto>(listaTextosDoProcesso);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Texto> recuperarTextosReferendo(Long seqObjetoIncidente) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		
		List<Long> listaCodigoTipoTexto = new ArrayList<Long>();
		listaCodigoTipoTexto.add(TipoTexto.DECISAO_MONOCRATICA.getCodigo());

		try {
			hql.append(" SELECT t FROM Texto t ");
			hql.append(" WHERE ");
			hql.append(" t.objetoIncidenteReferendo.id = :seqObjetoIncidente ");
			hql.append(" AND t.tipoTexto IN ( :listaTipoTexto ) ");
			hql.append(" AND t.inclusaoAutomatica ='S' ");
			hql.append(" ORDER BY t.id DESC");

			
			Query q = session.createQuery(hql.toString());
			q.setLong("seqObjetoIncidente", seqObjetoIncidente);
			q.setParameterList("listaTipoTexto", listaCodigoTipoTexto);
			
			Set<Texto> listaTextosDoProcesso = new HashSet<Texto>((List<Texto>)q.list());
			
			return new ArrayList<Texto>(listaTextosDoProcesso);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	public Texto recuperar(Long seqObjetoIncidente, Long codigoMinistro, TipoTexto tipoTexto, Date dataFim, Long seqVoto) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		
		try {
			hql.append("SELECT t FROM Texto t ");
			hql.append("WHERE 1 = 1");
			
			if(seqObjetoIncidente != null && seqObjetoIncidente.longValue() > 0)
				hql.append("AND t.objetoIncidente.id = :seqObjetoIncidente ");
			
			if(codigoMinistro != null && codigoMinistro.longValue() > 0)
				hql.append("AND t.ministro.id = :codigoMinistro ");
			
			if(tipoTexto != null)
				hql.append("AND t.tipoTexto = :tipoTexto ");
			
			if(dataFim != null)
				hql.append("AND t.dataSessao = TO_DATE(:dataFim,'dd/mm/yyyy') ");
			
			if(seqVoto != null && seqVoto.longValue() > 0)
				hql.append("AND t.sequenciaVoto = :seqVoto ");
			
			Query q = session.createQuery(hql.toString());
			
			if(seqObjetoIncidente != null && seqObjetoIncidente.longValue() > 0)
				q.setLong("seqObjetoIncidente", seqObjetoIncidente);
			
			if(codigoMinistro != null && codigoMinistro.longValue() > 0)
				q.setLong("codigoMinistro", codigoMinistro);
			
			if(tipoTexto != null)
				q.setParameter("tipoTexto", tipoTexto.getCodigo());
				
			if(dataFim != null) {
				SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
				q.setString("dataFim", formatDate.format(dataFim));
			}
			
			if(seqVoto != null && seqVoto.longValue() > 0)
				q.setLong("seqVoto", seqVoto);
			
			return (Texto) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Texto recuperar(Long seqObjetoIncidente, String tipoJulgamento,
			TipoTexto tipoTexto, Long codigoMinistro) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		
		try {
			hql.append("SELECT t FROM Texto t ");
			hql.append("WHERE 1 = 1");
			
			if(seqObjetoIncidente != null && seqObjetoIncidente.longValue() > 0)
				hql.append("AND t.objetoIncidente.id = :seqObjetoIncidente ");
			
			if(SearchData.stringNotEmpty(tipoJulgamento))
				hql.append("AND t.tipoJulgamento = :tipoJulgamento ");
			
			if(codigoMinistro != null && codigoMinistro.longValue() > 0)
				hql.append("AND t.ministro.id = :codigoMinistro ");
			
			if(tipoTexto != null)
				hql.append("AND t.tipoTexto = :tipoTexto ");
			
			Query q = session.createQuery(hql.toString());
			
			if(seqObjetoIncidente != null && seqObjetoIncidente.longValue() > 0)
				q.setLong("seqObjetoIncidente", seqObjetoIncidente);
			
			if(SearchData.stringNotEmpty(tipoJulgamento))
				q.setString("tipoJulgamento", tipoJulgamento);
			
			if(codigoMinistro != null && codigoMinistro.longValue() > 0)
				q.setLong("codigoMinistro", codigoMinistro);
			
			if(tipoTexto != null)
				q.setParameter("tipoTexto", tipoTexto.getCodigo());
			
			return (Texto) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Texto recuperar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto, Ministro ministro,
			Boolean textoPublico, Boolean dataSessaoPreenchida) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Texto.class);
			
			c.add(Restrictions.eq("objetoIncidente", objetoIncidente));
			c.add(Restrictions.eq("tipoTexto", tipoTexto));
			c.add(Restrictions.eq("ministro", ministro));
			if (textoPublico != null) {
				c.add(Restrictions.eq("publico", textoPublico));
			}
			if (dataSessaoPreenchida != null) {
				if (dataSessaoPreenchida) {
					c.add(Restrictions.isNotNull("dataSessao"));
				} else {
					c.add(Restrictions.isNull("dataSessao"));
				}
			}
			
			return (Texto) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Texto> recuperarListaTextoEletronico(Integer idArquivoEletronico) throws DaoException {
		Session session = retrieveSession();

		Criteria criteria = session.createCriteria(Texto.class);
		criteria.add(Restrictions.eq("codigoBrs", idArquivoEletronico));
		
		return criteria.list();

	}
	
	@Override
	public void refresh(Texto entity) throws DaoException {
		try {
			retrieveSession().refresh(entity);
		} catch (HibernateException e) {
			throw new DaoException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Texto> recuperarTextosPorFaseTipoEspecificos(Long seqObjetoIncidente, List<Long> tipoTextos, FaseTexto fase) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();

		try {
			hql.append(" SELECT t FROM Texto t ");
			hql.append(" WHERE ");
			hql.append(" t.objetoIncidente.id = :seqObjetoIncidente ");
			hql.append(" AND t.tipoTexto NOT IN ( :tipoTextos ) ");
			hql.append(" ORDER BY t.id DESC");

			
			Query q = session.createQuery(hql.toString());
			q.setLong("seqObjetoIncidente", seqObjetoIncidente);
			q.setParameterList("tipoTextos", tipoTextos);
			
			List<Texto> listaTextosDoProcesso = (List<Texto>)q.list();
			
			return new ArrayList<Texto>(listaTextosDoProcesso);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}

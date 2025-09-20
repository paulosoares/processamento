package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoTextoDao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import oracle.jdbc.OracleTypes;

@SuppressWarnings("unchecked")
@Repository
public class DocumentoTextoDaoHibernate extends GenericHibernateDao<DocumentoTexto, Long> implements DocumentoTextoDao {

	private static final long serialVersionUID = 349259474026678356L;

	public DocumentoTextoDaoHibernate() {
		super(DocumentoTexto.class);
	}

	public DocumentoTexto recuperarNaoCancelado(Texto texto, Long codigoTipoDocumentoTexto) throws DaoException {
		DocumentoTexto documentoTexto = null;
		try {

			Session session = retrieveSession();

			String hql = 
				      "SELECT dt FROM DocumentoTexto dt, DocumentoEletronicoView de "
					+ "WHERE dt.documentoEletronico.id = de.id"  
					+ " AND dt.texto.id = ? "
					+ " AND de.descricaoStatusDocumento <> ? ";
			if (codigoTipoDocumentoTexto != null && codigoTipoDocumentoTexto != 0) {
				hql += " AND dt.tipoDocumentoTexto.id = ?";
			}

			/* 
			 * Ordenar pela última alteração de forma decrescente
			 * @author tiago.peixoto
			 * @since 16/9/2010 
			 */
			hql += " ORDER BY dt.dataAlteracao DESC ";
			
			Query q = session.createQuery(hql);

			q.setLong(0, texto.getId());
			q.setString(1, DocumentoEletronico.SIGLA_DESCRICAO_STATUS_CANCELADO);

			if (codigoTipoDocumentoTexto != null && codigoTipoDocumentoTexto != 0) {
				q.setLong(2, codigoTipoDocumentoTexto);
			}

			documentoTexto = (DocumentoTexto) q.uniqueResult();

		} catch (Exception e) {

			throw new DaoException(e);
		}
		return documentoTexto;
	}

	public List<DocumentoTexto> pesquisarDocumentosSetor(Setor setor, TipoSituacaoDocumento tipoSituacaoDocumento,
			Date dataInicio, Date dataFim, String classeProcesso, Long numeroProcesso) throws DaoException {
		try {
			if (dataInicio != null) {
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(new SimpleDateFormat("dd/MM/yyyy")
						.format(dataInicio)
						+ " 00:00:00");
			}
			if (dataFim != null) {
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(new SimpleDateFormat("dd/MM/yyyy").format(dataFim)
						+ " 23:59:59");
			}
		} catch (ParseException e) {
			throw new DaoException(e);
		}

		List<DocumentoTexto> listaDocumentoTexto = null;

		Session session = retrieveSession();

		if (classeProcesso != null && classeProcesso.trim().length() == 0) {
			classeProcesso = null;
		}
		if (numeroProcesso != null && numeroProcesso.intValue() == 0) {
			numeroProcesso = null;
		}

		StringBuffer hql = new StringBuffer();

		/**
		 * QUERY ATUALIZADA CONFORME CONSTA NO TEXTUAL
		 * SOLICITAÇÃO FEITA PELO TIAGO POIS A QUERY
		 * ANTIGA ESTAVA TRAZENDO REGISTROS QUE NAO DEVERIA
		 * @author ViniciusK
		 */
		hql.append(" SELECT dt FROM Processo  p, DocumentoTexto dt " + "JOIN FETCH dt.documentoEletronicoView "
				+ "JOIN FETCH dt.texto " + "JOIN FETCH dt.texto.tipoTexto " + "JOIN FETCH dt.texto.objetoIncidente "
				+ "JOIN FETCH dt.texto.objetoIncidente.principal " + "LEFT JOIN FETCH dt.assinaturaDigitalView "
				+ "LEFT JOIN FETCH dt.tipoDocumentoTexto ");
		hql.append(" WHERE dt.id = (SELECT MAX(dt2.id) FROM DocumentoTexto dt2 " + "WHERE   dt2.texto.id = dt.texto.id) ");
		hql.append(" AND dt.tipoSituacaoDocumento = :tipoSituacaoDocumento ");
		hql.append(" AND dt.dataRevisao IS NOT NULL ");
		hql.append(" AND dt.texto.objetoIncidente.principal = p ");
		if (classeProcesso != null) {
			hql.append(" AND p.siglaClasseProcessual = :classeProcesso ");
		}
		if (numeroProcesso != null) {
			hql.append(" AND p.numeroProcessual = :numeroProcesso ");
		}
		hql.append(" AND dt.texto.ministro.setor = :setor ");
		hql.append(" AND dt.texto.tipoTexto  NOT IN (:codTipoTexto) ");
		hql.append(" AND dt.documentoEletronicoView.descricaoStatusDocumento IN (:descricao) ");

		/**
		 * QUERY ANTIGA GERADA PARA A VERSÃO 2.0.0
		 * @author Leonardo.almeida
		 */

		boolean maxResult = false;
		if (tipoSituacaoDocumento == TipoSituacaoDocumento.REVISADO) {
			if (dataInicio != null && dataFim != null) {
				hql.append(" AND dt.dataRevisao BETWEEN :dataInicio AND :dataFim ");
			}
		} else if (tipoSituacaoDocumento.equals(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE)) {
			if (dataInicio != null && dataFim != null) {
				hql.append(" AND dt.assinaturaDigitalView.dataCarimboTempo BETWEEN :dataInicio AND :dataFim ");
			} else {
				maxResult = true;
			}
		}

		hql.append(" ORDER BY dt.texto.arquivoEletronico.id ASC, p.numeroProcessual ASC ");

		Query q = session.createQuery(hql.toString());
		if (maxResult) {
			q.setMaxResults(300);
		}

		q.setLong("setor", setor.getId());

		q.setParameter("tipoSituacaoDocumento", tipoSituacaoDocumento.getCodigo());
		q.setParameterList("codTipoTexto", new Long[] { TipoTexto.DECISAO.getCodigo(),
				TipoTexto.EDITAL_PROPOSTA_SUMULA_VINCULANTE.getCodigo() });

		if (tipoSituacaoDocumento.getCodigo() == 2) {
			List<String> listaDescricaoDoc = new LinkedList<String>();
			listaDescricaoDoc.add("RAS");
			listaDescricaoDoc.add("DIG");
			q.setParameterList("descricao", listaDescricaoDoc);
		} else if (tipoSituacaoDocumento.getCodigo() == 3) {
			q.setParameter("descricao", "ASS");
		}

		if (dataInicio != null) {
			q.setDate("dataInicio", dataInicio);
		}
		if (dataFim != null) {
			q.setDate("dataFim", dataFim);
		}
		if (classeProcesso != null) {
			q.setString("classeProcesso", classeProcesso);
		}
		if (numeroProcesso != null) {
			q.setLong("numeroProcesso", numeroProcesso);
		}

		listaDocumentoTexto = q.list();

		return listaDocumentoTexto;
	}

	public List<DocumentoTexto> pesquisarDocumentosSetor(Setor setor, List<TipoSituacaoDocumento> tipoSituacaoDocumentos)
			throws DaoException {
		List<DocumentoTexto> listaDocumentoTexto = null;

		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(DocumentoTexto.class);
		criteria.add(Restrictions.in("tipoSituacaoDocumento", tipoSituacaoDocumentos));
		Criteria criteriaTexto = criteria.createCriteria("texto");
		Criteria criteriaMinistro = criteriaTexto.createCriteria("ministro");
		criteriaMinistro.add(Restrictions.eq("setor", setor));

		listaDocumentoTexto = criteria.list();

		return listaDocumentoTexto;
	}

	public Boolean cancelarDocumento(DocumentoEletronico documentoEletronico, String descricaoCancelamento) throws DaoException {
		try {
			Session session = retrieveSession();

			CallableStatement storedProcedureStmt = session.connection().prepareCall(
					"{call doc.pkg_documento.prc_cancela_documento(?,?)}");

			storedProcedureStmt.setLong(1, documentoEletronico.getId());
			storedProcedureStmt.setString(2, descricaoCancelamento);
			storedProcedureStmt.execute();
			storedProcedureStmt.close();

		} catch (SQLException e) {
			throw new DaoException(e);
		}
		return true;
	}

	public DocumentoTexto recuperar(DocumentoEletronico documentoEletronico) throws DaoException {
		DocumentoTexto documentoTexto = null;

		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(DocumentoTexto.class, "dt");
		criteria = criteria.createAlias("dt.documentoEletronico", "de", CriteriaSpecification.INNER_JOIN).setFetchMode("de",
				FetchMode.JOIN);
		criteria.add(Restrictions.eq("de.id", documentoEletronico.getId()));

		return documentoTexto = (DocumentoTexto) criteria.uniqueResult();
	}

	public List<Long> pesquisaTextualIdDocumentoEletronico(String classeProcessual, Long numeroProcesso, Long codigoMinistro,
			TipoTexto tipoTexto, String descricao) throws DaoException {

		Session session = retrieveSession();
		Connection connection = null;
		CallableStatement stmt = null;
		PreparedStatement stmtConverte = null;
		ResultSet rs = null;
		ResultSet rsConvert = null;

		try {
			connection = session.connection();
			List<Long> lista = new LinkedList<Long>();

			if (descricao != null && descricao.trim().length() > 0) {

				if (descricao.contains("%")) {
					descricao = descricao.replace("%", "\\%");
				}

				StringBuffer sqlConverte = new StringBuffer("Select BRS.FNC_CONVERTE_PESQUISA('" + descricao
						+ "')as valor from dual");
				stmtConverte = session.connection().prepareStatement(sqlConverte.toString());
				rsConvert = stmtConverte.executeQuery();

				if (rsConvert.next()) {

					StringBuffer sql = new StringBuffer("SELECT /*+ INDEX (a ictx_arquivocomp) use_hash (t a) */ "
							+ " DISTINCT TO_NUMBER (t.seq_arquivo_eletronico) seq_arquivo_eletronico "
							+ "    FROM (SELECT /*+ NO_MERGE */ seq_arquivo_eletronico "
							+ " 			FROM stf.textos t,stf.processos p " + " 		   WHERE t.num_processo = p.num_processo "
							+ " 			AND t.sig_classe_proces = p.sig_classe_proces "
							+ "		    AND t.cod_ministro = p.cod_relator_atual ");
					if (codigoMinistro != null)
						sql.append(" AND t.cod_ministro = " + codigoMinistro);

					if (classeProcessual != null && classeProcessual.trim().length() > 0)
						sql.append(" AND p.sig_classe_proces = '" + classeProcessual + "' ");

					if (numeroProcesso != null)
						sql.append(" AND p.num_processo = " + numeroProcesso);

					if (tipoTexto != null)
						sql.append(" AND t.cod_tipo_texto = " + tipoTexto.getCodigo());

					sql.append(" ) t, ");
					sql.append("         doc.arquivo_eletronico a "
							+ "   WHERE t.seq_arquivo_eletronico = a.seq_arquivo_eletronico "
							+ "     AND contains (a.txt_conteudo, '" + rsConvert.getString("valor") + "') >0 "
							+ " ORDER BY 1 DESC ");

					stmt = connection.prepareCall("{call doc.pkg_pesquisa_textual.prc_pesquisa_textual (?,?)}");

					stmt.setString(1, sql.toString());
					stmt.registerOutParameter(2, OracleTypes.CURSOR);
					stmt.execute();
					rs = (ResultSet) stmt.getObject(2);
					while (rs.next()) {
						lista.add(rs.getLong("seq_arquivo_eletronico"));
					}

				}
			}
			return lista;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} catch (Exception e) {
			throw new DaoException("Exception", e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmtConverte != null)
					stmtConverte.close();
				if (rs != null)
					rs.close();
				if (rsConvert != null)
					rsConvert.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				throw new DaoException("SQLException Finally", e);
			}
		}

	}

	public DocumentoTexto recuperarDocumentoTextoMaisRecenteNaoCancelado(Texto texto,
			boolean verificarDocumentoEletronicoCancelado) throws DaoException {
		Criteria criteria = montaCriteriaDocumentosTextoDoTexto(texto);
		criteria.add(Restrictions.ne("dt.tipoSituacaoDocumento", TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO));
		criteria.add(Restrictions.ne("dt.tipoSituacaoDocumento", TipoSituacaoDocumento.CANCELADO_AUTOMATICAMENTE));
		if (verificarDocumentoEletronicoCancelado) {
			criteria = criteria.createCriteria("dt.documentoEletronicoView", "dev");
			criteria.add(Restrictions.ne("dev.descricaoStatusDocumento", DocumentoEletronico.SIGLA_DESCRICAO_STATUS_CANCELADO));
		}
		criteria.setMaxResults(1);
		return (DocumentoTexto) criteria.uniqueResult();
	}
	
	public DocumentoTexto recuperarDocumentoTextoMaisRecenteNaoCanceladoExtratoDeAta(Texto texto,
			boolean verificarDocumentoEletronicoCancelado) throws DaoException {
		Criteria criteria = montaCriteriaDocumentosTextoDoTextoExtratoDeAta(texto);
		criteria.add(Restrictions.ne("dtx.tipoDocumentoTexto.id", TipoDocumentoTexto.COD_TIPO_DOCUMENTO_TEXTO_CERTIDAO_JULGAMENTO));
		criteria.add(Restrictions.ne("dtx.tipoSituacaoDocumento", TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO));
		criteria.add(Restrictions.ne("dtx.tipoSituacaoDocumento", TipoSituacaoDocumento.CANCELADO_AUTOMATICAMENTE));
		if (verificarDocumentoEletronicoCancelado) {
			criteria = criteria.createCriteria("dtx.documentoEletronicoView", "devw");
			criteria.add(Restrictions.ne("devw.descricaoStatusDocumento", DocumentoEletronico.SIGLA_DESCRICAO_STATUS_CANCELADO));
		}
		criteria.setMaxResults(1);
		return (DocumentoTexto) criteria.uniqueResult();
	}

	public DocumentoTexto recuperarDocumentoTextoMaisRecente(Texto texto) throws DaoException {
		Criteria criteria = montaCriteriaDocumentosTextoDoTexto(texto);
		criteria.setMaxResults(1);
		return (DocumentoTexto) criteria.uniqueResult();
	}

	private Criteria montaCriteriaDocumentosTextoDoTexto(Texto texto) throws DaoException {
		Criteria criteria = retrieveSession().createCriteria(DocumentoTexto.class, "dt");
		criteria.add(Restrictions.eq("dt.texto.id", texto.getId()));
		criteria.addOrder(Order.desc("dt.id"));
		return criteria;
	}
	
	private Criteria montaCriteriaDocumentosTextoDoTextoExtratoDeAta(Texto texto) throws DaoException {
		Criteria criteria = retrieveSession().createCriteria(DocumentoTexto.class, "dtx");
		criteria.add(Restrictions.eq("dtx.texto.id", texto.getId()));
		criteria.addOrder(Order.desc("dtx.id"));
		return criteria;
	}

	public List<DocumentoTexto> pesquisarDocumentosTextoDoTexto(Texto texto) throws DaoException {
		Criteria criteria = montaCriteriaDocumentosTextoDoTexto(texto);
		return criteria.list();
	}
	
	@Override
	public DocumentoTexto recuperarDocumentoTextoAssinadoPorUltimo(Texto texto) throws DaoException {
		
		Session session = retrieveSession();
		String hql = "SELECT *"
				+ " FROM stf.documento_texto "
				+ " WHERE seq_documento_texto ="
				+ " (SELECT MAX (d.seq_documento_texto)"
				+ " FROM stf.textos t, stf.documento_texto d"
				+ " WHERE (t.seq_objeto_incidente,"
				+ " t.seq_arquivo_eletronico) ="
				+ " (SELECT t.seq_objeto_incidente,"
				+ " t.seq_arquivo_eletronico"
				+ " FROM stf.textos t"
				+ " WHERE t.seq_textos = :p_seqTextos)"
				+ " AND t.seq_textos = d.seq_textos"
				+ " AND d.seq_tipo_situacao_documento IN (3, 7)"
				+ " AND d.cod_tipo_documento_texto <> 51)";

		SQLQuery sqlQuery = session.createSQLQuery(hql).addEntity(DocumentoTexto.class);
		
		sqlQuery.setLong("p_seqTextos", texto.getId());
		
		DocumentoTexto documentoAssinadoPorUltimo = (DocumentoTexto) sqlQuery.uniqueResult();
		
		return documentoAssinadoPorUltimo;
	}
	
	@Override
	public DocumentoTexto recuperarDocumentoComDocumentoEletronico(Long idDocumentoTexto) throws DaoException {
		DocumentoTexto documentoTexto = null;
		try {

			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append("SELECT dt FROM DocumentoTexto dt LEFT JOIN FETCH dt.documentoEletronicoView  ");
			hql.append("WHERE dt.id = ? ");

			Query q = session.createQuery(hql.toString());

			q.setLong(0, idDocumentoTexto);

			documentoTexto = (DocumentoTexto) q.uniqueResult();
			documentoTexto.getDocumentoEletronicoView();
		} catch (Exception e) {

			throw new DaoException(e);
		}
		return documentoTexto;
	}
	
	@Override
	public DocumentoTexto recuperarUltimoExtratoAtaAssinadoReferenteUltimaSessao(ObjetoIncidente<?> oi) throws DaoException {

		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append("SELECT dt FROM DocumentoTexto dt ");
			hql.append(
					"WHERE dt.texto.id = (SELECT max(t.id) FROM Texto t WHERE t.objetoIncidente = :idObjetoIncidente AND t.tipoTexto = :tipoTexto) ");
			hql.append(" AND dt.tipoSituacaoDocumento in (:tipoSituacaoDocumento)");
			hql.append(" AND dt.tipoDocumentoTexto = :tipoDocumentoTexto");

			Query q = session.createQuery(hql.toString());

			q.setParameter("idObjetoIncidente", oi);
			q.setParameter("tipoTexto", TipoTexto.DECISAO.getCodigo());
			q.setParameterList("tipoSituacaoDocumento",
					Arrays.asList(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getCodigo(),
							TipoSituacaoDocumento.ASSINADO_MANUALMENTE.getCodigo(), TipoSituacaoDocumento.JUNTADO.getCodigo()));
			q.setParameter("tipoDocumentoTexto", TipoDocumentoTexto.COD_TIPO_DOCUMENTO_TEXTO_EXTRADO_ATA);

			DocumentoTexto dt = (DocumentoTexto) q.uniqueResult();

			return dt;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}

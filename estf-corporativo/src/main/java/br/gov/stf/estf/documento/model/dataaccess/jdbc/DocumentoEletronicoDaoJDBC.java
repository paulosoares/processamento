package br.gov.stf.estf.documento.model.dataaccess.jdbc;

import static br.gov.stf.estf.entidade.documento.DocumentoEletronico.SIGLA_DESCRICAO_STATUS_CANCELADO;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.SessionImplementor;
import org.perf4j.StopWatch;
import org.perf4j.aop.Profiled;
import org.perf4j.log4j.Log4JStopWatch;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoEletronicoDao;
import br.gov.stf.estf.documento.model.service.enums.TipoDeAcessoDoDocumento;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.FlagTipoAssinatura;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository("documentoEletronicoDao")
public class DocumentoEletronicoDaoJDBC extends GenericHibernateDao<DocumentoEletronico, Long> implements DocumentoEletronicoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8932567820152873515L;

	public DocumentoEletronicoDaoJDBC() {
		super(DocumentoEletronico.class);
	}

	public DocumentoEletronicoDaoJDBC(Class<DocumentoEletronico> clazz) {
		super(clazz);
	}

	@Profiled(tag = "CancelaDocumento")
	public Boolean cancelarDocumento(DocumentoEletronico documentoEletronico, String descricaoCancelamento) throws DaoException {
		Boolean sucesso = false;
		try {
			SessionImplementor session = (SessionImplementor) retrieveSession();

			CallableStatement storedProcedureStmt = session.getBatcher().prepareCallableStatement(
					"{call doc.PKG_DOCUMENTO.prc_cancela_documento(?,?)}");

			storedProcedureStmt.setLong(1, documentoEletronico.getId());
			storedProcedureStmt.setString(2, descricaoCancelamento);
			storedProcedureStmt.execute();
			session.getBatcher().closeStatement(storedProcedureStmt);
			sucesso = true;
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		return sucesso;
	}

	@Profiled(tag = "MudaTipoAcessoDocumento")
	public void alterarTipoDeAcessoDoDocumento(DocumentoEletronico documento, TipoDeAcessoDoDocumento tipoDeAcesso)
			throws DaoException {

		try {
			SessionImplementor session = (SessionImplementor) retrieveSession();
			CallableStatement csSelect = session.getBatcher().prepareCallableStatement(
					"{call DOC.PKG_DOCUMENTO.prc_muda_tipo_acesso(?,?)}");
			csSelect.setLong(1, documento.getId());
			csSelect.setString(2, tipoDeAcesso.getChave());
			csSelect.execute();
			session.getBatcher().closeStatement(csSelect);
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public DocumentoEletronico salvarDocumentoEletronicoAssinado(DocumentoEletronico de, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo, FlagTipoAssinatura tipoAssinatura) throws DaoException {
		try {
			Connection con = retrieveSession().connection();

			// ATUALIZAR O DOCUMENTO
			loadDocumentoEletronico(con, de.getId());
			StringBuffer sbDoc = new StringBuffer();
			sbDoc
					.append(" UPDATE doc.documento_temp dt SET dt.BIN_ARQUIVO = ?, dt.DSC_STATUS_DOCUMENTO = 'ASS' WHERE dt.SEQ_DOCUMENTO = ? ");
			PreparedStatement ps = con.prepareStatement(sbDoc.toString());
			ps.setBytes(1, pdfAssinado);
			ps.setLong(2, de.getId());
			ps.execute();
			ps.close();
			saveDocumentoEletronico(con, de);

			// SALVAR A ASSINATURA
			StringBuffer sbAss = new StringBuffer();
			sbAss.append(" INSERT INTO doc.ASSINATURA_DIGITAL_TEMP adt ");
			sbAss.append(" ( seq_assinatura_digital, seq_documento, bin_assinatura, bin_timestamp, dat_timestamp, tip_assinatura ) ");
			sbAss.append(" values ( ?, ?, ?, ?, ?, ?) ");
			PreparedStatement psInsertAss = con.prepareStatement(sbAss.toString());
			Long seqAss = getNextSeq(con, "DOC.SEQ_ASSINATURA_DIGITAL");
			psInsertAss.setLong(1, seqAss);
			psInsertAss.setLong(2, de.getId());
			psInsertAss.setBytes(3, assinatura);
			psInsertAss.setBytes(4, carimboTempo);
			psInsertAss.setDate(5, new java.sql.Date(dataCarimboTempo.getTime()));
			if (tipoAssinatura == null) {
				psInsertAss.setString(6, FlagTipoAssinatura.PADRAO.getCodigo());
			} else {
				psInsertAss.setString(6, tipoAssinatura.getCodigo());
			}
			psInsertAss.execute();
			psInsertAss.close();
			saveAssinaturaDigital(con, seqAss);
			
			atualizaCampoOS(de);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return de;
	}
	
	@Override
	public DocumentoEletronico salvarDocumentoEletronicoAssinado(DocumentoEletronico de, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo) throws DaoException {
		return salvarDocumentoEletronicoAssinado(de, pdfAssinado, assinatura, carimboTempo, dataCarimboTempo,
				FlagTipoAssinatura.PADRAO);
	}
	
	@Override
	public DocumentoEletronico salvarDocumentoEletronicoAssinadoContingencialmente(DocumentoEletronico de, byte[] pdfAssinado) throws DaoException {
		try {
			Connection con = retrieveSession().connection();

			// ATUALIZAR O DOCUMENTO
			loadDocumentoEletronico(con, de.getId());
			StringBuffer sbDoc = new StringBuffer();
			sbDoc
					.append(" UPDATE doc.documento_temp dt SET dt.BIN_ARQUIVO = ?, dt.DSC_STATUS_DOCUMENTO = 'ASS', dt.TXT_HASH_VALIDACAO = ? WHERE dt.SEQ_DOCUMENTO = ? ");
			PreparedStatement ps = con.prepareStatement(sbDoc.toString());
			ps.setBytes(1, pdfAssinado);
			ps.setLong(2, de.getId());
			ps.setString(3, de.getHashValidacao());
			ps.execute();
			ps.close();
			saveDocumentoEletronico(con, de);
			atualizaCampoOS(de);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return de;
	}
	
	public DocumentoEletronico salvarDocumentoEletronicoAssinadoEVerificaSeNecessitaDeOutraAssinatura(DocumentoEletronico de, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo, Boolean precisaAssinaturaDecisao ) throws DaoException {
		try {
			Connection con = retrieveSession().connection();

			// ATUALIZAR O DOCUMENTO
			loadDocumentoEletronico(con, de.getId());
			StringBuffer sbDoc = new StringBuffer();
			sbDoc
					.append(" UPDATE doc.documento_temp dt SET dt.BIN_ARQUIVO = ?, dt.DSC_STATUS_DOCUMENTO = 'ASS' WHERE dt.SEQ_DOCUMENTO = ? ");
			PreparedStatement ps = con.prepareStatement(sbDoc.toString());
			ps.setBytes(1, pdfAssinado);
			ps.setLong(2, de.getId());
			ps.execute();
			ps.close();
			saveDocumentoEletronico(con, de);

			// SALVAR A ASSINATURA
			StringBuffer sbAss = new StringBuffer();
			sbAss.append(" INSERT INTO doc.ASSINATURA_DIGITAL_TEMP adt ");
			sbAss.append(" ( seq_assinatura_digital, seq_documento, bin_assinatura, bin_timestamp, dat_timestamp ) ");
			sbAss.append(" values ( ?, ?, ?, ?, ?) ");
			PreparedStatement psInsertAss = con.prepareStatement(sbAss.toString());
			Long seqAss = getNextSeq(con, "DOC.SEQ_ASSINATURA_DIGITAL");
			psInsertAss.setLong(1, seqAss);
			psInsertAss.setLong(2, de.getId());
			psInsertAss.setBytes(3, assinatura);
			psInsertAss.setBytes(4, carimboTempo);
			psInsertAss.setDate(5, new java.sql.Date(dataCarimboTempo.getTime()));
			psInsertAss.execute();
			psInsertAss.close();
			if (precisaAssinaturaDecisao){
				saveAssinaturaDigitalEAlterarFaseDocumentoParaMaisUmaAssinatura(con, seqAss, "'S'");
			}else{
				saveAssinaturaDigital(con, seqAss);
			}

			atualizaCampoOS(de);
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return de;
	}	
	
	public DocumentoEletronico salvarDocumentoEletronicoCoAssinado(DocumentoEletronico de, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo) throws DaoException {
		try {
			Connection con = retrieveSession().connection();
			
			// ATUALIZAR O DOCUMENTO
			loadDocumentoEletronico(con, de.getId());
			StringBuffer sbDoc = new StringBuffer();
			sbDoc
					.append(" UPDATE doc.documento_temp dt SET dt.BIN_ARQUIVO = ?, dt.DSC_STATUS_DOCUMENTO = 'ASS' WHERE dt.SEQ_DOCUMENTO = ? ");
			PreparedStatement ps = con.prepareStatement(sbDoc.toString());
			ps.setBytes(1, pdfAssinado);
			ps.setLong(2, de.getId());
			ps.execute();
			ps.close();
			saveDocumentoEletronico(con, de);

			// SALVAR A ASSINATURA
			StringBuffer sbAss = new StringBuffer();
			sbAss.append(" INSERT INTO doc.ASSINATURA_DIGITAL_TEMP adt ");
			sbAss.append(" ( seq_assinatura_digital, seq_documento, bin_assinatura, bin_timestamp, dat_timestamp ) ");
			sbAss.append(" values ( ?, ?, ?, ?, ?) ");
			PreparedStatement psInsertAss = con.prepareStatement(sbAss.toString());
			Long seqAss = getNextSeq(con, "DOC.SEQ_ASSINATURA_DIGITAL");
			psInsertAss.setLong(1, seqAss);
			psInsertAss.setLong(2, de.getId());
			psInsertAss.setBytes(3, assinatura);
			psInsertAss.setBytes(4, carimboTempo);
			psInsertAss.setDate(5, new java.sql.Date(dataCarimboTempo.getTime()));
			psInsertAss.execute();
			psInsertAss.close();
			saveAssinaturaDigital(con, seqAss);

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return de;

	}

	public DocumentoEletronico salvarDocumentoEletronicoCoAssinado(DocumentoEletronico de, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo, Long seqDocumentoVinculado) throws DaoException {
		try {
			Connection con = retrieveSession().connection();
			
			incluirDocumentoEletronico(de);

			// SALVAR A ASSINATURA
			StringBuffer sbAss = new StringBuffer();
			sbAss.append(" INSERT INTO doc.ASSINATURA_DIGITAL_TEMP adt ");
			sbAss.append(" ( seq_assinatura_digital, seq_documento, bin_assinatura, bin_timestamp, dat_timestamp ) ");
			sbAss.append(" values ( ?, ?, ?, ?, ?) ");
			PreparedStatement psInsertAss = con.prepareStatement(sbAss.toString());
			Long seqAss = getNextSeq(con, "DOC.SEQ_ASSINATURA_DIGITAL");
			psInsertAss.setLong(1, seqAss);
			psInsertAss.setLong(2, de.getId());
			psInsertAss.setBytes(3, assinatura);
			psInsertAss.setBytes(4, carimboTempo);
			psInsertAss.setDate(5, new java.sql.Date(dataCarimboTempo.getTime()));
			psInsertAss.execute();
			psInsertAss.close();
			saveCoAssinaturaDigital(con, seqAss, seqDocumentoVinculado);

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return de;

	}
	
	public void incluirDocumentoEletronico(DocumentoEletronico documento) throws DaoException {
		try {
			StringBuffer sbDoc = new StringBuffer();
			sbDoc.append("INSERT INTO doc.documento_temp "
					+ " (SEQ_DOCUMENTO,BIN_ARQUIVO, DSC_STATUS_DOCUMENTO , TIP_ACESSO, SIG_SISTEMA, SEQ_TIPO_ARQUIVO, TXT_HASH_VALIDACAO) "
					+ " VALUES (?,?,?,?,?,?,?)");
			SessionImplementor session = (SessionImplementor) retrieveSession();
			PreparedStatement ps = session.getBatcher().prepareStatement(sbDoc.toString());
			ps.setLong(1, documento.getId());
			ps.setBytes(2, documento.getArquivo());
			ps.setString(3, documento.getDescricaoStatusDocumento());
			ps.setString(4, documento.getTipoAcesso());
			ps.setString(5, documento.getSiglaSistema());
			ps.setLong(6, documento.getTipoArquivo().getCodigo());
			ps.setString(7, documento.getHashValidacao());
			ps.execute();
			ps.close();
			saveDocumentoEletronico(session.connection(), documento);
			atualizaCampoOS(documento);
			
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	public Long recuperarProximaSequenceDocumentoEletronico() throws DaoException {
		try {
			SessionImplementor session = (SessionImplementor) retrieveSession();
			return getNextSeq(session.connection(), "DOC.SEQ_DOCUMENTO");
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	private void saveAssinaturaContingencial(Connection con, Long seqDoc) throws SQLException {
		StopWatch stopWatch = new Log4JStopWatch();
		
		CallableStatement storedProcedureStmt = con.prepareCall("{call DOC.PKG_DOCUMENTO.PRC_ASSINA_DOCUMENTO_CONT (?)}");
		storedProcedureStmt.setLong(1, seqDoc);
		storedProcedureStmt.execute();
		storedProcedureStmt.close();
		
		stopWatch.stop("AssinaContigencialDocumento");

	}

	private void saveAssinaturaDigital(Connection con, Long seqAss) throws SQLException {
		StopWatch stopWatch = new Log4JStopWatch();
		
		CallableStatement storedProcedureStmt = con.prepareCall("{call DOC.PKG_DOCUMENTO.PRC_ASSINA_DOCUMENTO (?)}");
		storedProcedureStmt.setLong(1, seqAss);
		storedProcedureStmt.execute();
		storedProcedureStmt.close();

		stopWatch.stop("AssinaDocumento");
	}
	
	private void saveAssinaturaDigitalEAlterarFaseDocumentoParaMaisUmaAssinatura(Connection con, Long seqAss, String alteraFaseNaPackage) throws SQLException {
		StopWatch stopWatch = new Log4JStopWatch();
		
		CallableStatement storedProcedureStmt = con.prepareCall("{call DOC.PKG_DOCUMENTO.PRC_ASSINA_DOCUMENTO (?,?)}");
		storedProcedureStmt.setLong(1, seqAss);
		storedProcedureStmt.setString(2, alteraFaseNaPackage);
		storedProcedureStmt.execute();
		storedProcedureStmt.close();

		stopWatch.stop("AssinaEAlteraFaseDocumento");
	}
	
	
	private void saveCoAssinaturaDigital(Connection con, Long seqAss, Long seqDocumentoVinculado) throws SQLException {
		StopWatch stopWatch = new Log4JStopWatch();

		CallableStatement storedProcedureStmt = con.prepareCall("{call DOC.PKG_DOCUMENTO.PRC_CO_ASSINA_DOCUMENTO (?, ?)}");
		storedProcedureStmt.setLong(1, seqAss);
		storedProcedureStmt.setLong(2, seqDocumentoVinculado);
		storedProcedureStmt.execute();
		storedProcedureStmt.close();
		
		stopWatch.stop("CoAssinaDocumento");
	}

	private Long getNextSeq(Connection con, String nomeSeq) throws SQLException {
		StringBuffer sql = new StringBuffer(" select " + nomeSeq + ".NEXTVAL from dual ");
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery(sql.toString());
		Long nextVal = 0L;
		if (rs.next()) {
			nextVal = rs.getLong("NEXTVAL");
		}
		stm.close();
		rs.close();
		return nextVal;
	}

	private void loadDocumentoEletronico(Connection con, Long de) throws SQLException {
		StopWatch stopWatch = new Log4JStopWatch();
		
		CallableStatement storedProcedureStmt = con.prepareCall("{call DOC.PKG_DOCUMENTO.PRC_RECUPERA_DOCUMENTO(?)}");
		storedProcedureStmt.setLong(1, de);
		storedProcedureStmt.execute();
		storedProcedureStmt.close();
		
		stopWatch.stop("RecuperaDocumento");
	}

	private void saveDocumentoEletronico(Connection con, DocumentoEletronico de) throws SQLException {
		StopWatch stopWatch = new Log4JStopWatch();
		
		CallableStatement storedProcedureStmt = con.prepareCall("{call DOC.PKG_DOCUMENTO.PRC_GRAVA_DOCUMENTO (?)}");
		storedProcedureStmt.setLong(1, de.getId());
		storedProcedureStmt.execute();
		storedProcedureStmt.close();
		
		stopWatch.stop("GravaDocumento");
	}

	public byte[] recuperarArquivo(Long documentoEletronico) throws DaoException {
		byte[] documento = null;
		try {
			Connection con = retrieveSession().connection();
			loadDocumentoEletronico(con, documentoEletronico);

			StringBuffer sbSelDoc = new StringBuffer(
					" select dt.bin_arquivo from doc.documento_temp dt where dt.seq_documento = ? ");
			PreparedStatement ps = con.prepareStatement(sbSelDoc.toString());

			ps.setLong(1, documentoEletronico);

			ResultSet rs = ps.executeQuery();

			Blob blob = null;
			while (rs.next()) {
				blob = rs.getBlob("bin_arquivo");
			}

			if (blob != null) {
				InputStream is = blob.getBinaryStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				for (int i = is.read(buffer); i != -1; i = is.read(buffer)) {
					baos.write(buffer, 0, i);
				}
				documento = baos.toByteArray();
				baos.close();
				is.close();
			}

			rs.close();
			ps.close();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return documento;
	}
	
	public Blob recuperarArquivoDocumento(Long seqArquivoDocumento) 
	throws DaoException {
		
		Blob result = null;
		
		Session session = retrieveSession();
		
		Connection conn = session.connection();

		
		try {
			loadDocumentoEletronico(conn, seqArquivoDocumento);
			
			StringBuffer sb = new StringBuffer(" select dt.bin_arquivo from doc.documento_temp dt where dt.seq_documento = ? ");			
			PreparedStatement pstmt = conn.prepareStatement(sb.toString());
			pstmt.setLong(1, seqArquivoDocumento);
			
			try {
				ResultSet rs = pstmt.executeQuery();
				
				try { 
					if( rs.next() ) {
						Blob b = rs.getBlob(1);
						
						result = b;
					}
				}
				finally {
					if( rs != null )
						rs.close();
				}
			}
			finally {
				if( pstmt != null )
					pstmt.close();
			}
				
		}
		catch( SQLException e ) {
			throw new DaoException("Erro ao recuperar documento.", e);
		}
		
		return result;
	}	

	public DocumentoEletronico recuperarUltimoDocumentoEletronicoAtivo(Texto texto) throws DaoException {
		try {

			String hql = "SELECT dt FROM DocumentoTexto dt "
					+ " WHERE dt.documentoEletronicoView.descricaoStatusDocumento <> ? AND dt.texto.id = ? AND dt.tipoSituacaoDocumento <> ? AND dt.tipoSituacaoDocumento <> ?"
					+ " order by dt.id desc";

			Query query = retrieveSession().createQuery(hql);

			query.setString(0, SIGLA_DESCRICAO_STATUS_CANCELADO);
			query.setLong(1, texto.getId());
			query.setLong(2, TipoSituacaoDocumento.CANCELADO_AUTOMATICAMENTE.getCodigo());
			query.setLong(3, TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO.getCodigo());
			List result = query.list();

			if (!result.isEmpty()) {
				DocumentoTexto documentoTexto = (DocumentoTexto) result.get(0);
				if (documentoTexto != null) {
					return documentoTexto.getDocumentoEletronico();
				}
			}

			return null;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Long recuperarSequencialDoUltimoDocumentoEletronico()
			throws DaoException {
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer(" SELECT max(documentoEletronico.id) FROM DocumentoEletronicoView documentoEletronico ");
			Query q = session.createQuery(hql.toString());
			return (Long) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void salvarDocumentoEletronicoAssinadoContingencialmente(
			DocumentoEletronico documentoEletronico) throws DaoException {
		try {
			Connection con = retrieveSession().connection();
			saveAssinaturaContingencial(con, documentoEletronico.getId());
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		
	}

	public void atualizaCampoOS(DocumentoEletronico de) throws DaoException {
		try {
			Connection con = retrieveSession().connection();
			// ATUALIZAR O DOCUMENTO COD_OBJECT_STORAGE
			CallableStatement storedProcedureStmt = con.prepareCall("{call  DOC.PKG_DOCUMENTO.PRC_ALTERA_OBJECT_STORAGE(?,NULL) }");
			storedProcedureStmt.setLong(1,  de.getId());
			storedProcedureStmt.execute();
			storedProcedureStmt.close();
		} catch (SQLException e) {
			throw new DaoException(e);
		}
		
	}

	
}

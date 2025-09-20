package br.gov.stf.estf.documento.model.dataaccess.hibernate;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.PdfDao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.HibernateDao;

@Repository
public class PdfDaoJDBC extends HibernateDao implements PdfDao {
		
	
	@Autowired
	public PdfDaoJDBC(SessionFactory sessionFactory) {
		super();
		setSessionFactory(sessionFactory);
	}

	private Long inserirDocumentoEletronico(Connection con, Long seqTextos, byte[] pdf) throws HibernateException, DaoException, SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into doc.documento_temp (seq_documento, sig_sistema, dsc_status_documento, tip_acesso, seq_tipo_arquivo, bin_arquivo) ");
		sql.append(" values ( ?, ?, ?, ?, ?, ?) ");
		
		PreparedStatement stm = con.prepareStatement(sql.toString());
		Long seqDocumento = getNextSeq(con, "doc.seq_documento"); 
		stm.setLong(1, seqDocumento);
		stm.setString(2, "ESTFSESSOES");
		stm.setString(3, "RAS");
		stm.setString(4, "INT");
		stm.setLong(5, 5L);
		stm.setBytes(6, pdf);
		
		stm.executeUpdate();
		
		stm.close();
		
		StopWatch stopWatch = new Log4JStopWatch();
		
		CallableStatement storedProcedureStmt = con.prepareCall("{call DOC.PKG_DOCUMENTO.PRC_GRAVA_DOCUMENTO (?)}");

		storedProcedureStmt.setLong(1, seqDocumento);

		storedProcedureStmt.execute();

		storedProcedureStmt.close();
		
		stopWatch.stop("GravaDocumento");
		
		return seqDocumento;
		
	}
	
	private Long getNextSeq (Connection con, String nomeSeq) throws SQLException  {
		StringBuffer sql = new StringBuffer(" select "+nomeSeq+".NEXTVAL from dual ");
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery(sql.toString());
		Long nextVal = null;
		if ( rs.next() ) {
			nextVal = rs.getLong("NEXTVAL");
		}
		stm.close();
		rs.close();
		return nextVal;
	}

	public Long getSeqTextos(Long objetoIncidente, Date dataAta) throws HibernateException, DaoException {
		
		Connection con = retrieveSession().connection();
		StringBuffer sql = new StringBuffer(" select seq_textos from stf.textos t ");
		sql.append(" where t.seq_objeto_incidente = ? ");
		sql.append(" and t.cod_tipo_texto = ? ");
		sql.append(" and t.dat_sessao = ? ");
		
		PreparedStatement psRecuperarDecisaoAta;
		try {
			psRecuperarDecisaoAta = con.prepareStatement(sql.toString());
		psRecuperarDecisaoAta.setLong(1, objetoIncidente);
		psRecuperarDecisaoAta.setLong(2, TipoTexto.DECISAO.getCodigo() );
		psRecuperarDecisaoAta.setDate(3, new java.sql.Date(dataAta.getTime()));
		
		ResultSet rsTexto = psRecuperarDecisaoAta.executeQuery();
		Long seqTextos = null;
		if ( rsTexto.next() ) {
			seqTextos = rsTexto.getLong("seq_textos");				
		}
		
		psRecuperarDecisaoAta.close();
		rsTexto.close();
		
		return seqTextos;
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	public void salvarPdf(Long objetoIncidente, Date dataAta, byte[] pdf,
			Long codigoTipoDocumentoTexto) throws DaoException {
		
		try {
			Connection con = retrieveSession().connection();
			Long seqTextos = getSeqTextos(objetoIncidente, dataAta);
			Long seqDocumento = inserirDocumentoEletronico(con, seqTextos, pdf);
			inserirDocumentoTexto(con, seqTextos, seqDocumento, codigoTipoDocumentoTexto);
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
	}
	
	private void inserirDocumentoTexto(Connection con, Long seqTextos,
			Long seqDocumento, Long codigoTipoDocumentoTexto) throws SQLException, HibernateException, DaoException {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer(" insert into stf.documento_texto (seq_documento_texto, seq_documento, seq_textos, cod_tipo_documento_texto, seq_tipo_situacao_documento) ");
		sql.append(" values (?,?,?,?,? )");
		
		PreparedStatement stm = con.prepareStatement(sql.toString());
		Long seqDocumentoTexto = getNextSeq(con, "stf.seq_documento_texto");
		stm.setLong(1, seqDocumentoTexto);
		stm.setLong(2, seqDocumento);
		stm.setLong(3, seqTextos);
		stm.setLong(4, codigoTipoDocumentoTexto);
		stm.setLong(5, 4L);
		
		stm.executeUpdate();
		
		stm.close();
	}

	public void salvarDocumentoTextoAssinado(Long documentoTexto,
			Long documentoEletronico, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo) throws DaoException {
		/* Salvando o novo array de bytes do PDF
		 
		log.info("salvando documento eletronico assinado");
		documentoEletronicoService.salvarDocumentoEletronicoAssinado(
				documentoTexto.getDocumentoEletronico(), pdfAssinado,
				assinatura, carimboTempo, dataCarimboTempo);
		
		log.info("documento eletronico assinado salvo com sucesso");
		
		log.info("recuperando tipo situacao documento assinado");

		
		TipoSituacaoDocumento tipoSituacaoDocumento = tipoSituacaoDocumentoService
				.recuperar(TipoSituacaoDocumento.SIGLA_SITUACAO_ASSINADO_DIGITALMENTE);
		documentoTexto.setTipoSituacaoDocumento(tipoSituacaoDocumento);
		
		log.info("alterando tipo situacao documento");
		alterar(documentoTexto);*/
		
		try {
		
			Connection con = retrieveSession().connection();			
			
			// ATUALIZAR O DOCUMENTO
			loadDocumentoEletronico(con, documentoEletronico);			
			StringBuffer sbDoc = new StringBuffer();
			sbDoc.append(" UPDATE doc.documento_temp dt SET dt.BIN_ARQUIVO = ?, dt.DSC_STATUS_DOCUMENTO = 'ASS', dt.TIP_ACESSO='PUB' WHERE dt.SEQ_DOCUMENTO = ? ");			
			PreparedStatement ps = con.prepareStatement( sbDoc.toString() );			
			ps.setBytes(1, pdfAssinado);
			ps.setLong(2, documentoEletronico);
			ps.execute();
			ps.close();			
			saveDocumentoEletronico(con, documentoEletronico);
			
			// SALVAR A ASSINATURA
			StringBuffer sbAss = new StringBuffer();
			sbAss.append(" INSERT INTO doc.ASSINATURA_DIGITAL_TEMP adt ");
			sbAss.append(" ( seq_assinatura_digital, seq_documento, bin_assinatura, bin_timestamp, dat_timestamp ) ");
			sbAss.append(" values ( ?, ?, ?, ?, ?) ");
			PreparedStatement psInsertAss = con.prepareStatement( sbAss.toString() );
			long seqAss = getNextSeq(con, "DOC.SEQ_ASSINATURA_DIGITAL");
			psInsertAss.setLong(1, seqAss);
			psInsertAss.setLong(2, documentoEletronico);
			psInsertAss.setBytes(3, assinatura);
			psInsertAss.setBytes(4, carimboTempo);
			psInsertAss.setDate(5, new java.sql.Date(dataCarimboTempo.getTime()));
			psInsertAss.execute();
			psInsertAss.close();
			saveAssinaturaDigital(con, seqAss);
			
			StringBuffer sbUpDoc = new StringBuffer();
			sbUpDoc.append(" update stf.documento_texto dt set dt.SEQ_TIPO_SITUACAO_DOCUMENTO = ? where dt.seq_documento_texto = ? ");
			PreparedStatement psUpDoc = con.prepareStatement( sbUpDoc.toString() );
			psUpDoc.setInt(1, 3); //assinado digitalmente
			psUpDoc.setLong(2, documentoTexto);
			psUpDoc.execute();
			psUpDoc.close();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
	}
	
	private void saveAssinaturaDigital(Connection con, long seqAss) throws SQLException {
		StopWatch stopWatch = new Log4JStopWatch();
		
		CallableStatement storedProcedureStmt = con.prepareCall("{call DOC.PKG_DOCUMENTO.PRC_ASSINA_DOCUMENTO (?)}");		
		storedProcedureStmt.setLong(1, seqAss);		
		storedProcedureStmt.execute();		
		storedProcedureStmt.close();
		
		stopWatch.stop("AssinaDocumento");
	}

	private void loadDocumentoEletronico (Connection con, Long de) throws SQLException {
		StopWatch stopWatch = new Log4JStopWatch();
		
		CallableStatement storedProcedureStmt = con.prepareCall("{call DOC.PKG_DOCUMENTO.PRC_RECUPERA_DOCUMENTO(?)}");	
		storedProcedureStmt.setLong(1, de);		
		storedProcedureStmt.execute();		
		storedProcedureStmt.close();
		
		stopWatch.stop("RecuperaDocumento");
	}
	
	private void saveDocumentoEletronico (Connection con, Long de) throws SQLException {
		StopWatch stopWatch = new Log4JStopWatch();
		
		CallableStatement storedProcedureStmt = con.prepareCall("{call DOC.PKG_DOCUMENTO.PRC_GRAVA_DOCUMENTO (?)}");		
		storedProcedureStmt.setLong(1, de);		
		storedProcedureStmt.execute();		
		storedProcedureStmt.close();
		
		stopWatch.stop("GravaDocumento");
	}

	@Override
	public void salvarPDFPadrao(DocumentoEletronico documentoEletronico, String nomeSistema, byte[] pdf)
			throws DaoException {
		try {
			Connection con = retrieveSession().connection();
			StringBuffer sql = new StringBuffer();
			sql.append(" insert into doc.documento_temp (seq_documento, sig_sistema, dsc_status_documento, tip_acesso, seq_tipo_arquivo, bin_arquivo) ");
			sql.append(" values ( ?, ?, ?, ?, ?, ?) ");
			
			PreparedStatement stm = con.prepareStatement(sql.toString());
			Long seqDocumento = getNextSeq(con, "doc.seq_documento"); 
			stm.setLong(1, seqDocumento);
			stm.setString(2, nomeSistema);
			stm.setString(3, "RAS");
			stm.setString(4, "INT");
			stm.setLong(5, 5L);
			stm.setBytes(6, pdf);
			
			stm.executeUpdate();
			
			stm.close();
			
			StopWatch stopWatch = new Log4JStopWatch();
			
			CallableStatement storedProcedureStmt = con.prepareCall("{call DOC.PKG_DOCUMENTO.PRC_GRAVA_DOCUMENTO (?)}");
			
			storedProcedureStmt.setLong(1, seqDocumento);
			
			storedProcedureStmt.execute();
			
			storedProcedureStmt.close();
			
			stopWatch.stop("GravaDocumento");
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		
	}
}

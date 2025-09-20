package br.gov.stf.estf.documento.model.dataaccess;

import java.sql.Blob;
import java.util.Date;

import br.gov.stf.estf.documento.model.service.enums.TipoDeAcessoDoDocumento;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.FlagTipoAssinatura;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DocumentoEletronicoDao extends GenericDao<DocumentoEletronico, Long> {

	public DocumentoEletronico recuperarUltimoDocumentoEletronicoAtivo(Texto texto) throws DaoException;

	public Boolean cancelarDocumento(DocumentoEletronico documentoEletronico, String descricaoCancelamento) throws DaoException;

	public void alterarTipoDeAcessoDoDocumento(DocumentoEletronico documento, TipoDeAcessoDoDocumento tipoDeAcesso)
			throws DaoException;

	public DocumentoEletronico salvarDocumentoEletronicoAssinado(DocumentoEletronico documentoEletronico, byte[] pdfAssinado,
			byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo) throws DaoException;

	public DocumentoEletronico salvarDocumentoEletronicoAssinadoEVerificaSeNecessitaDeOutraAssinatura(DocumentoEletronico documentoEletronico, 
			byte[] pdfAssinado,byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo, Boolean precisaAssinaturaDecisao) throws DaoException;
	
	public DocumentoEletronico salvarDocumentoEletronicoCoAssinado(
			DocumentoEletronico documentoEletronico, byte[] pdfAssinado,
			byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo, Long seqDocumentoVinculado) throws DaoException;

	public DocumentoEletronico salvarDocumentoEletronicoCoAssinado(
			DocumentoEletronico documentoEletronico, byte[] pdfAssinado,
			byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo) throws DaoException;
	
	public byte[] recuperarArquivo(Long documentoEletronico) throws DaoException;
	
	public Blob recuperarArquivoDocumento(Long seqArquivoDocumento) throws DaoException;

	Long recuperarProximaSequenceDocumentoEletronico() throws DaoException;

	void incluirDocumentoEletronico(DocumentoEletronico documento) throws DaoException;

	public Long recuperarSequencialDoUltimoDocumentoEletronico() throws DaoException;

	public void salvarDocumentoEletronicoAssinadoContingencialmente(
			DocumentoEletronico documentoEletronico) throws DaoException;

	DocumentoEletronico salvarDocumentoEletronicoAssinadoContingencialmente(DocumentoEletronico de, byte[] pdfAssinado) throws DaoException;

	DocumentoEletronico salvarDocumentoEletronicoAssinado(DocumentoEletronico de, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo, FlagTipoAssinatura tipoAssinatura) throws DaoException;

}

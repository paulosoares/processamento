package br.gov.stf.estf.documento.model.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.lowagie.text.DocumentException;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoEletronicoDao;
import br.gov.stf.estf.documento.model.service.enums.TipoDeAcessoDoDocumento;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.FlagTipoAssinatura;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface DocumentoEletronicoService extends GenericService<DocumentoEletronico, Long, DocumentoEletronicoDao> {

	/**
	 * @param texto
	 * @return
	 * @throws ServiceException
	 * 
	 */
	public DocumentoEletronico recuperarUltimoDocumentoEletronicoAtivo(Texto texto) throws ServiceException;

	/**
	 * Cancela o documento informado
	 * 
	 * @param documentoEletronico
	 * @param descricaoCancelamento
	 * @return
	 * @throws DaoException
	 */
	public Boolean cancelarDocumento(DocumentoEletronico documentoEletronico, String descricaoCancelamento) throws ServiceException;

	public DocumentoEletronico salvarDocumentoEletronicoAssinado(DocumentoEletronico documentoEletronico, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo) throws ServiceException;

	/**
	 * Salva o texto pela package porém verifica se o PDF necessitará de uma nova assinatura do ministro no eSTF-Decisão caso o 
	 * valor booleano precisaAssinaturaDecisao for true. Package alterada pelo Carlos (SDB) para ter a possibilidade de duas assinaturas
	 * Uma assinatura será feita pelo Expediente Automatizado e a outra pelo eSTF-Decisão
	 */
	public DocumentoEletronico salvarDocumentoEletronicoAssinadoPeloMinistroDecisao(DocumentoEletronico documentoEletronico, byte[] pdfAssinado,
			byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo, Boolean precisaAssinaturaDecisao) throws ServiceException;

	public DocumentoEletronico salvarDocumentoEletronicoCoAssinado(DocumentoEletronico documentoEletronico, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo, Long seqDocumentoVinculado) throws ServiceException;

	public DocumentoEletronico salvarDocumentoEletronicoAssinadoPublico(DocumentoEletronico documentoEletronico, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo) throws ServiceException;

	public void alterarTipoDeAcessoDoDocumento(DocumentoEletronico documento, TipoDeAcessoDoDocumento tipoDeAcesso) throws ServiceException;

	public byte[] recuperarArquivo(Long documentoEletronico) throws ServiceException;

	Long recuperarProximaSequenceDocumentoEletronico() throws ServiceException;

	void incluirDocumentoEletronicoSQL(DocumentoEletronico documentoEletronico) throws ServiceException;

	public DocumentoEletronico criaESalvaDocumentoEletronicoAssinador(byte[] pdf, String nomeSistema, ModeloComunicacao modeloComunicacao)
			throws ServiceException;

	public Long recuperarSequencialDoUltimoDocumentoEletronico() throws ServiceException;

	public void salvarDocumentoEletronicoAssinadoContingencialmente(DocumentoEletronico documentoEletronico) throws ServiceException;

	public Long recuperarQuantidadePaginasDocumentoEletronico(DocumentoEletronico documentoEletronico) throws ServiceException;

	public Long recuperarQuantidadePaginasPdf(byte[] arquivo) throws ServiceException;

	void salvarDocumentoEletronicoAssinadoContingencialmente(DocumentoEletronico documentoEletronico, byte[] pdfAssinado) throws ServiceException;

	DocumentoEletronico salvarDocumentoEletronicoAssinado(DocumentoEletronico documentoEletronico, byte[] pdfAssinado,
			byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo, FlagTipoAssinatura tipoAssinatura)
					throws ServiceException;
	
	public byte[] recuperarDocumentosPDF(List<Long> idsDocumentos) throws ServiceException;
	
	public byte[] recuperarDocumentosPDF(List<Long> idsDocumentos, List<Long> idsSegredoJustiça) throws ServiceException;
	
	public String gerarHashValidacao(Long sequencialDoDocumento);
	
	public String gerarHashValidacao(DocumentoComunicacao documentoComunicacao);
	
	public byte[] unirDocumentos(List<byte[]> documentos) throws IOException, DocumentException;
}

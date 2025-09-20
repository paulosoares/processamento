package br.gov.stf.estf.documento.model.service;

import java.util.Date;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoComunicacaoDao;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.FlagTipoAssinatura;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface DocumentoComunicacaoService extends GenericService<DocumentoComunicacao, Long, DocumentoComunicacaoDao> {

	public DocumentoComunicacao recuperarNaoCancelado(Comunicacao comunicacao) throws ServiceException;

	public Boolean cancelarDocumentos(DocumentoComunicacao documentoComunicacao, String motivoCancelamento, String userName) throws ServiceException;

	/**
	 * Salva o DocumentoComunicacao com o novo PDF assinado, assinatura e carimbo de tempo
	 * 
	 * @param documentoTexto
	 *            documentoTexto
	 * @param pdfAssinado
	 *            bytes do PDF assinado
	 * @param assinatura
	 *            bytes da assinatura
	 * @param carimboTempo
	 *            bytes do carimbo de tempo
	 * @param dataCarimboTempo
	 *            data extraida do token
	 * @return
	 * @throws ServiceException
	 */
	public DocumentoComunicacao salvarDocumentoComunicacaoAssinado(DocumentoComunicacao documentoComunicacao, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo, String usuario) throws ServiceException;

	public void salvarDocumentoComunicacaoAssinadoeSTFDecisao(DocumentoComunicacao documentoComunicacao, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo, String usuario, String siglaSistema) throws ServiceException;

	public void devolverDocumentoeSTFDecisao(DocumentoComunicacao documentoComunicacao, String observacao, String usuario) throws ServiceException;

	Long recuperarSequencialDoDocumentoEletronico(Long idDocumentoComunicacao) throws ServiceException;

	public void encaminharParaDJe(Comunicacao comunicacao, String usuario) throws ServiceException;

	void salvarDocumentoComunicacaoAssinadoContingencialmenteeSTFDecisao(DocumentoComunicacao documentoComunicacao, byte[] pdfAssinado, Date dataAssinatura, String usuario,
			String siglaSistema) throws ServiceException;

	void salvarDocumentoComunicacaoAssinadoeSTFDecisao(DocumentoComunicacao documentoComunicacao, byte[] pdfAssinado,
			byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo, String usuario, String siglaSistema,
			FlagTipoAssinatura tipoAssinatura) throws ServiceException;
	
	public void recarregarDocumentoComunicacao(DocumentoComunicacao documentoComunicacao) throws DaoException;
}
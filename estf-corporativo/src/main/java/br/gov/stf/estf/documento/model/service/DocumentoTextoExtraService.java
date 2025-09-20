package br.gov.stf.estf.documento.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.framework.model.service.ServiceException;

public interface DocumentoTextoExtraService {
	/**
	 * Salva o DocumentoTexto com o novo PDF assinado, assinatura e carimbo de
	 * tempo
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
	public DocumentoTexto salvarDocumentoTextoAssinado(
			DocumentoTexto documentoTexto, byte[] pdfAssinado,
			byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo)
			throws ServiceException;
	
	/**
	 * Cancela os documentos informados
	 * 
	 * @param listaDocumentoTexto
	 *            lista com os documentos que se deseja cancelar
	 * @return
	 * @throws ServiceException
	 */
	public Boolean cancelarDocumentos(List<DocumentoTexto> listaDocumentoTexto,
			String motivoCancelamento) throws ServiceException;
	
	/**
	 * Cria um entrada na tabela Documento e liga ao texto especificado.
	 * 
	 * @param texto
	 * @param pdf
	 * @return entrada DocumentoTexto
	 * @throws ServiceException
	 */
	DocumentoTexto criaDocumentoPdf(Texto texto,byte[] pdf) throws ServiceException;
}

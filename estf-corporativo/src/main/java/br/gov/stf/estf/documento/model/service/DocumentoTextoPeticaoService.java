package br.gov.stf.estf.documento.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoTextoPeticaoDao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTextoPeticao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface DocumentoTextoPeticaoService extends GenericService<DocumentoTextoPeticao, Long, DocumentoTextoPeticaoDao> {

	/**
	 * Recupera os documentos gerados para o setor e situação informados
	 * 
	 * @param setor Setor que gerou os documentos
	 * @param tipoSituacaoDocumento Situação do documento: Gerado, Revisado, Assinado, etc...
	 * @return
	 */
	public List<DocumentoTextoPeticao> pesquisarDocumentosSetor(Setor setor, TipoSituacaoDocumento tipoSituacaoDocumento, Date dataInicio, Date dataFim, Short anoProtocolo, Long numeroProtocolo) throws ServiceException;	
	
	/**
	 * Cancela os documentos informados
	 * 
	 * @param listaDocumentoTextoPeticao lista com os documentos que se deseja cancelar
	 * @return
	 * @throws ServiceException
	 */
	public Boolean cancelarDocumentos(List<DocumentoTextoPeticao> listaDocumentoTextoPeticao, String motivoCancelamento) throws ServiceException;
	
	/**
	 * Salva o DocumentoTextoPeticao com o novo PDF assinado, assinatura e carimbo de tempo 
	 * @param documentoTextoPeticao documentoTextoPeticao
	 * @param pdfAssinado bytes do PDF assinado
	 * @param assinatura bytes da assinatura
	 * @param carimboTempo bytes do carimbo de tempo
	 * @param  dataCarimboTempo data extraida do token
	 * @return
	 * @throws ServiceException
	 */
	public DocumentoTextoPeticao salvarDocumentoTextoPeticaoAssinado (DocumentoTextoPeticao documentoTextoPeticao, byte[] pdfAssinado, byte[] assinatura, byte[] carimboTempo,  Date dataCarimboTempo) throws ServiceException;
	
	public DocumentoTextoPeticao recuperar(DocumentoEletronico documentoEletronico) throws ServiceException;
}

package br.gov.stf.estf.documento.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoTextoDao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface DocumentoTextoService extends GenericService<DocumentoTexto, Long, DocumentoTextoDao> {

	/**
	 * Recupera documentos não cancelados a partir do texto
	 * 
	 * @param texto
	 *            texto o qual se deseja recuperar o PDF
	 * @return
	 * @throws ServiceException
	 */
	public DocumentoTexto recuperarNaoCancelado(Texto texto, Long codigoTipoDocumentoTexto) throws ServiceException;

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
	public DocumentoTexto salvarDocumentoTextoAssinado(DocumentoTexto documentoTexto, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo) throws ServiceException;

	/**
	 * Salva o DocumentoTexto com o novo PDF assinado, assinatura e carimbo de
	 * tempo, e torna o documento publico
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
	// public DocumentoTexto salvarDocumentoTextoAssinadoPublico (DocumentoTexto
	// documentoTexto, byte[] pdfAssinado, byte[] assinatura, byte[]
	// carimboTempo, Date dataCarimboTempo) throws ServiceException;
	/**
	 * Recupera os documentos gerados para o setor e situação informados
	 * 
	 * @param setor
	 *            Setor que gerou os documentos
	 * @param tipoSituacaoDocumento
	 *            Situação do documento: Gerado, Revisado, Assinado, etc...
	 * @return
	 */
	public List<DocumentoTexto> pesquisarDocumentosSetor(Setor setor, TipoSituacaoDocumento tipoSituacaoDocumento,
			Date dataInicio, Date dataFim, String classeProcesso, Long numeroProcesso) throws ServiceException;

	/**
	 * Recupera os documentos gerados para o setor e da lista de situações
	 * informadas
	 * 
	 * @param setor
	 *            Setor que gerou os documentos
	 * @param tipoSituacaoDocumento
	 *            Situação do documento: Gerado, Revisado, Assinado, etc...
	 * @return
	 */
	public List<DocumentoTexto> pesquisarDocumentosSetor(Setor setor, List<TipoSituacaoDocumento> tipoSituacaoDocumento)
			throws ServiceException;

	/**
	 * Cancela os documentos informados
	 * 
	 * @param listaDocumentoTexto
	 *            lista com os documentos que se deseja cancelar
	 * @return
	 * @throws ServiceException
	 */
	public Boolean cancelarDocumentos(List<DocumentoTexto> listaDocumentoTexto, String motivoCancelamento)
			throws ServiceException;

	public DocumentoTexto recuperar(DocumentoEletronico documentoEletronico) throws ServiceException;

	public List<Long> pesquisaTextualIdDocumentoEletronico(String classeProcessual, Long numeroProcesso, Long codigoMinistro,
			TipoTexto tipoTexto, String descricao) throws ServiceException;

	public DocumentoTexto recuperarDocumentoTextoMaisRecente(Texto texto) throws ServiceException;

	void excluirDocumentosTextoDoTexto(Texto texto) throws ServiceException;

	public DocumentoTexto recuperarDocumentoTextoMaisRecenteNaoCancelado(Texto texto) throws ServiceException;
	public DocumentoTexto recuperarDocumentoTextoMaisRecenteNaoCancelado(Texto texto, Boolean verificarDocumentoEletronicoCancelado) throws ServiceException;
	public DocumentoTexto recuperarDocumentoTextoMaisRecenteNaoCanceladoExtratoDeAta(Texto texto, Boolean verificarDocumentoEletronicoCancelado) throws ServiceException;

	Boolean cancelarDocumentos(List<DocumentoTexto> listaDocumentoTexto, String motivoCancelamento,
			TipoSituacaoDocumento situacaoDocumento) throws ServiceException;

	DocumentoEletronico recuperarDocumentoEletronicoNaoCancelado(Texto texto) throws ServiceException;
	
	public DocumentoTexto recuperarDocumentoTextoAssinadoPorUltimo(Texto texto) throws ServiceException;

	DocumentoTexto recuperarDocumentoComDocumentoEletronico(Long idDocumentoTexto) throws ServiceException;
	
	public DocumentoTexto recuperarUltimoExtratoAtaAssinadoReferenteUltimaSessao(ObjetoIncidente<?> oi) throws ServiceException;

}

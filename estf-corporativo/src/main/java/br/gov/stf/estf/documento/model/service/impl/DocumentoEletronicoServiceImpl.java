package br.gov.stf.estf.documento.model.service.impl;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.sun.star.awt.FontFamily;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoEletronicoDao;
import br.gov.stf.estf.documento.model.service.AssinaturaDigitalService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.enums.TipoDeAcessoDoDocumento;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.FlagTipoAssinatura;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaAcessoDocumento;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoArquivo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("documentoEletronicoService")
public class DocumentoEletronicoServiceImpl extends GenericServiceImpl<DocumentoEletronico, Long, DocumentoEletronicoDao> implements DocumentoEletronicoService {

	public DocumentoEletronicoServiceImpl(DocumentoEletronicoDao dao, AssinaturaDigitalService assinaturaDigitalService) {
		super(dao);
	}

	public Boolean cancelarDocumento(DocumentoEletronico documentoEletronico, String descricaoCancelamento) throws ServiceException {
		Boolean result = false;
		try {
			result = dao.cancelarDocumento(documentoEletronico, descricaoCancelamento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return result;
	}

	@Override
	public DocumentoEletronico salvarDocumentoEletronicoAssinado(DocumentoEletronico documentoEletronico, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo, FlagTipoAssinatura tipoAssinatura) throws ServiceException {

		// se o documento estiver assinado cancela volta para rascunho para permitir a nova
		// persistência
		if (documentoEletronico.getDescricaoStatusDocumento().equalsIgnoreCase(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO)) {
			documentoEletronico.setDescricaoStatusDocumento(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_RASCUNHO);
			alterar(documentoEletronico);
		}

		try {
			dao.salvarDocumentoEletronicoAssinado(documentoEletronico, pdfAssinado, assinatura, carimboTempo, dataCarimboTempo, tipoAssinatura);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return documentoEletronico;
	}

	@Override
	public DocumentoEletronico salvarDocumentoEletronicoAssinado(DocumentoEletronico documentoEletronico, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo) throws ServiceException {
		return salvarDocumentoEletronicoAssinado(documentoEletronico, pdfAssinado, assinatura,
				carimboTempo, dataCarimboTempo, FlagTipoAssinatura.PADRAO);
	}
	
	public DocumentoEletronico salvarDocumentoEletronicoAssinadoPeloMinistroDecisao(DocumentoEletronico documentoEletronico, byte[] pdfAssinado,
			byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo, Boolean precisaAssinaturaDecisao) throws ServiceException {

		// se o documento estiver assinado cancela volta para rascunho para permitir a nova
		// persistência
		if (documentoEletronico.getDescricaoStatusDocumento().equalsIgnoreCase(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO)) {
			documentoEletronico.setDescricaoStatusDocumento(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_RASCUNHO);
			alterar(documentoEletronico);
		}

		try {
			dao.salvarDocumentoEletronicoAssinadoEVerificaSeNecessitaDeOutraAssinatura(documentoEletronico, pdfAssinado, assinatura, carimboTempo,
					dataCarimboTempo, precisaAssinaturaDecisao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return documentoEletronico;
	}

	public DocumentoEletronico salvarDocumentoEletronicoCoAssinado(DocumentoEletronico documentoEletronico, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo, Long seqDocumentoVinculado) throws ServiceException {

		try {
			dao.salvarDocumentoEletronicoCoAssinado(documentoEletronico, pdfAssinado, assinatura, carimboTempo, dataCarimboTempo, seqDocumentoVinculado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return documentoEletronico;
	}

	public DocumentoEletronico salvarDocumentoEletronicoAssinadoPublico(DocumentoEletronico documentoEletronico, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo) throws ServiceException {

		salvarDocumentoEletronicoAssinado(documentoEletronico, pdfAssinado, assinatura, carimboTempo, dataCarimboTempo);

		alterarTipoDeAcessoDoDocumento(documentoEletronico, TipoDeAcessoDoDocumento.PUBLICO);

		return documentoEletronico;
	}

	public void alterarTipoDeAcessoDoDocumento(DocumentoEletronico documento, TipoDeAcessoDoDocumento tipoDeAcesso) throws ServiceException {
		try {
			dao.alterarTipoDeAcessoDoDocumento(documento, tipoDeAcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public byte[] recuperarArquivo(Long documentoEletronico) throws ServiceException {
		byte[] arquivo = null;
		try {
			arquivo = dao.recuperarArquivo(documentoEletronico);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return arquivo;
	}

	public DocumentoEletronico recuperarUltimoDocumentoEletronicoAtivo(Texto texto) throws ServiceException {
		try {
			return dao.recuperarUltimoDocumentoEletronicoAtivo(texto);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * Método que insere um documento eletrônico utilizado SQL Nativo. Utilizado para
	 * fazer a persistência de documento eletrônico quando o ID for recuperado previamente.
	 * @param documentoEletronico
	 * @throws ServiceException 
	 */
	public void incluirDocumentoEletronicoSQL(DocumentoEletronico documentoEletronico) throws ServiceException {
		try {
			dao.incluirDocumentoEletronico(documentoEletronico);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Recupera a próxima sequence do documento eletrônico, para ser utilizada antes da inclusão.
	 * @return
	 * @throws ServiceException
	 */
	public Long recuperarProximaSequenceDocumentoEletronico() throws ServiceException {
		try {
			return dao.recuperarProximaSequenceDocumentoEletronico();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public DocumentoEletronico criaESalvaDocumentoEletronicoAssinador(byte[] pdf, String nomeSistema, ModeloComunicacao modeloComunicacao)
			throws ServiceException {
		DocumentoEletronico documentoEletronico = new DocumentoEletronico();
		documentoEletronico.setSiglaSistema(nomeSistema);
		if (modeloComunicacao.getFlagTipoAcessoDocumentoPeca().equals(FlagGenericaAcessoDocumento.I)) {
			documentoEletronico.setTipoAcesso(DocumentoEletronico.TIPO_ACESSO_INTERNO);
		} else {
			documentoEletronico.setTipoAcesso(DocumentoEletronico.TIPO_ACESSO_PUBLICO);
		}
		documentoEletronico.setDescricaoStatusDocumento(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_RASCUNHO);
		documentoEletronico.setTipoArquivo(TipoArquivo.PDF);
		documentoEletronico.setArquivo(pdf);
		documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());

		try {
			dao.salvar(documentoEletronico);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return documentoEletronico;
	}

	@Override
	public Long recuperarSequencialDoUltimoDocumentoEletronico() throws ServiceException {
		try {
			return dao.recuperarSequencialDoUltimoDocumentoEletronico();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void salvarDocumentoEletronicoAssinadoContingencialmente(DocumentoEletronico documentoEletronico) throws ServiceException {
		try {
			dao.salvarDocumentoEletronicoAssinadoContingencialmente(documentoEletronico);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void salvarDocumentoEletronicoAssinadoContingencialmente(DocumentoEletronico documentoEletronico, byte[] pdfAssinado) throws ServiceException {
		try {
			dao.salvarDocumentoEletronicoAssinadoContingencialmente(documentoEletronico, pdfAssinado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Long recuperarQuantidadePaginasDocumentoEletronico(DocumentoEletronico documentoEletronico) throws ServiceException {
		return recuperarQuantidadePaginasPdf(documentoEletronico.getArquivo());
	}

	@Override
	public Long recuperarQuantidadePaginasPdf(byte[] arquivo) throws ServiceException {
		try {
			PdfReader pdfReader = new PdfReader(arquivo);
			Long quantidadePaginas = new Long(pdfReader.getNumberOfPages());
			return quantidadePaginas;
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public byte[] recuperarDocumentosPDF(List<Long> idsDocumentos)
			throws ServiceException {
		return recuperarDocumentosPDF(idsDocumentos, null);
	}
	
	@Override
	public byte[] recuperarDocumentosPDF(List<Long> idsDocumentos, List<Long> idsSegredoJustiça)
		throws ServiceException{
		if (idsDocumentos == null || idsDocumentos.isEmpty())
			return null;
		List<byte[]> documentos = new ArrayList<byte[]>();
		try{
			for (Long id : idsDocumentos){
				byte[] arquivo = recuperarPorId(id).getArquivo();
				if (idsSegredoJustiça != null && !idsSegredoJustiça.isEmpty() && idsSegredoJustiça.contains(id))
					arquivo = incluirMarcaDagua(arquivo);
				documentos.add(arquivo);			
			}
		
			return unirDocumentos(documentos);
		}catch(DocumentException e){
			throw new ServiceException(e);
		}catch(IOException e){
			throw new ServiceException(e);
		}
	}
	
	private byte[] incluirMarcaDagua(byte[] pdfContent) throws IOException, DocumentException{
		ByteArrayOutputStream retorno = new ByteArrayOutputStream(); 
		PdfReader reader = new PdfReader(pdfContent);
		PdfStamper stamper = new PdfStamper(reader, retorno);		
        Font f = new Font(FontFamily.MODERN, 15, Font.BOLD, Color.WHITE);		
        Chunk textAsChunk = new Chunk("SEGREDO DE JUSTIÇA", f);        
        textAsChunk.setBackground(Color.RED);
        textAsChunk.setTextRise(2);
        
        for( int i = 1; i <= reader.getNumberOfPages(); i++){        
	        Rectangle pageSize = reader.getPageSize(i);
	        PdfContentByte over = stamper.getOverContent(i);       
	        Phrase p = new Phrase(textAsChunk); 
	        over.saveState();       
	        //ColumnText.showTextAligned(over, Element.ALIGN_TOP, p, pageSize.getWidth() - 162, pageSize.getHeight() - 103, 0);
	        ColumnText.showTextAligned(over, Element.ALIGN_TOP, p, pageSize.getWidth() - 200, pageSize.getHeight() - 40, 0);
	        over.restoreState();
        }
        stamper.close();
        reader.close();
	    return retorno.toByteArray();    
	}
	
	public byte[] unirDocumentos(List<byte[]> documentos) throws IOException, DocumentException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PdfCopyFields copy = new PdfCopyFields(bos);

		for (Iterator iterator = documentos.iterator(); iterator.hasNext();) {
			byte[] documento = (byte[]) iterator.next();
			ByteArrayInputStream bis = new ByteArrayInputStream(documento);
			PdfReader reader = new PdfReader(bis);
			
			copy.addDocument(reader);
		}

		copy.close();
		return bos.toByteArray();
	}

	@Override
	public String gerarHashValidacao(Long sequencialDoDocumento) {
		String hashValidacao = AssinaturaDigitalServiceImpl.gerarHashValidacao();
		
		if (sequencialDoDocumento != null) {
			DocumentoEletronico documentoEletronico;
			try {
				documentoEletronico = recuperarPorId(sequencialDoDocumento);
			} catch (ServiceException e) {
				documentoEletronico = null;
			}
			
			if (documentoEletronico != null && documentoEletronico.getHashValidacao() != null && !documentoEletronico.getHashValidacao().isEmpty())
				hashValidacao = documentoEletronico.getHashValidacao();
		}
		
		return hashValidacao;
	}
	
	@Override
	public String gerarHashValidacao(DocumentoComunicacao documentoComunicacao) {
		Long sequencialDoDocumento = null;
		
		if (documentoComunicacao != null && documentoComunicacao.getDocumentoEletronico() != null)
			sequencialDoDocumento = documentoComunicacao.getDocumentoEletronico().getId();
		
		return gerarHashValidacao(sequencialDoDocumento);
	}
}
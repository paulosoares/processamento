package br.gov.stf.estf.documento.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.PdfDao;
import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.documento.model.service.PdfService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("pdfService")
public class PdfServiceImpl implements PdfService {
	private final PdfDao pdfDao;
	private final DocumentoTextoService documentoTextoService;
	private final TextoService textoService;
	
	public PdfServiceImpl(PdfDao pdfDao, DocumentoTextoService documentoTextoService, TextoService textoService) {
		super();
		this.pdfDao = pdfDao;
		this.documentoTextoService = documentoTextoService;
		this.textoService = textoService;
	}

	public void salvarExtratoAta(Long objetoIncidente, Date dataAta, byte[] pdf)
				throws ServiceException {
		salvarPdf(objetoIncidente, dataAta, pdf, TipoDocumentoTexto.COD_TIPO_DOCUMENTO_TEXTO_EXTRADO_ATA);
	}
	
	public void salvarCertidaoJulgamento(Long objetoIncidente,
			Date dataAta, byte[] pdf) throws ServiceException {
		salvarPdf(objetoIncidente, dataAta, pdf, TipoDocumentoTexto.COD_TIPO_DOCUMENTO_TEXTO_CERTIDAO_JULGAMENTO);
		
	}
	
	private void salvarPdf (Long objetoIncidente,
			Date dataAta, byte[] pdf, Long codigoTipoDocumentoTexto) throws ServiceException {
		try {
			DocumentoTexto documentoNaoCancelado = documentoTextoService.recuperarNaoCancelado(textoService.recuperarPorId(pdfDao.getSeqTextos(objetoIncidente, dataAta)), codigoTipoDocumentoTexto);
			if (documentoNaoCancelado != null) {
				List<DocumentoTexto> lista = new ArrayList<DocumentoTexto>();
				lista.add(documentoNaoCancelado);
				documentoTextoService.cancelarDocumentos(lista, null);
			}
			pdfDao.salvarPdf(objetoIncidente, dataAta, pdf, codigoTipoDocumentoTexto);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		
	}
	
	public void salvarDocumentoTextoAssinado(Long documentoTexto,
			Long documentoEletronico, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo) throws ServiceException {
		
		try {
			
			if (documentoTexto == null || documentoEletronico==null || pdfAssinado == null
					|| pdfAssinado.length == 0 || assinatura == null
					|| assinatura.length == 0 || carimboTempo == null
					|| carimboTempo.length == 0) {

				throw new IllegalArgumentException(
						"Faltando parâmetros para salvar o documento.");
			}
			
			pdfDao.salvarDocumentoTextoAssinado(documentoTexto, documentoEletronico, pdfAssinado, assinatura, carimboTempo, dataCarimboTempo);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		
	}

	@Override
	public void salvarPDFPadrao(DocumentoEletronico documentoEletronico, String nomeSistema, byte[] pdf)
			throws ServiceException {
		
		try {
			pdfDao.salvarPDFPadrao(documentoEletronico, nomeSistema, pdf);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
	}

}

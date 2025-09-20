package br.gov.stf.estf.documento.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoTextoDao;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoExtraService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoArquivo;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("documentoTextoExtraService")
public class DocumentoTextoExtraServiceImpl implements DocumentoTextoExtraService {
	private final DocumentoTextoService documentoTextoService;
	private final DocumentoEletronicoService documentoEletronicoService;
	private final PecaProcessoEletronicoService pecaProcessoEletronicoService;
	private final DocumentoTextoDao documentoTextoDao;

	public DocumentoTextoExtraServiceImpl(DocumentoTextoService documentoTextoService,
			DocumentoEletronicoService documentoEletronicoService, PecaProcessoEletronicoService pecaProcessoEletronicoService, 
			DocumentoTextoDao documentoTextoDao) {
		super();
		this.documentoTextoService = documentoTextoService;
		this.documentoEletronicoService = documentoEletronicoService;
		this.pecaProcessoEletronicoService = pecaProcessoEletronicoService;
		this.documentoTextoDao = documentoTextoDao;
	}

	public DocumentoTexto salvarDocumentoTextoAssinado(DocumentoTexto documentoTexto, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo) throws ServiceException {

		if (documentoTexto == null || pdfAssinado == null || pdfAssinado.length == 0 || assinatura == null
				|| assinatura.length == 0 || carimboTempo == null || carimboTempo.length == 0) {

			throw new IllegalArgumentException("Faltando parâmetros para salvar o documento.");
		}

		if (documentoTexto.getTipoSituacaoDocumento().equals(
				TipoSituacaoDocumento.ASSINADO_DIGITALMENTE)) {
			throw new ServiceException("Documento assinado anteriormente.");
		}

		if (documentoTexto.getTipoSituacaoDocumento().equals(TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO)) {
			throw new ServiceException("Documento cancelado.");
		}

		/**
		 * Salvando o novo array de bytes do PDF
		 */
		documentoEletronicoService.salvarDocumentoEletronicoAssinado(documentoTexto.getDocumentoEletronico(), pdfAssinado,
				assinatura, carimboTempo, dataCarimboTempo);
		
		documentoTexto.setTipoSituacaoDocumento(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE);

		documentoTextoService.alterar(documentoTexto);

		return documentoTexto;
	}

	public Boolean cancelarDocumentos(List<DocumentoTexto> listaDocumentoTexto, String motivoCancelamento)
			throws ServiceException {
		try {
			for (DocumentoTexto documentoTexto : listaDocumentoTexto) {
				// MUDA A SITUACAO DA PECA PARA O CASO DE CANCELAMENTO DE UM
				// DOCUMENTO JUNTADO
				if (TipoSituacaoDocumento.JUNTADO.equals(documentoTexto.getTipoSituacaoDocumento())) {

					PecaProcessoEletronico pecaProcessoEletronico = pecaProcessoEletronicoService.recuperarPeca(documentoTexto);					
					pecaProcessoEletronico.setTipoSituacaoPeca(TipoSituacaoPeca.EXCLUIDA);
					pecaProcessoEletronicoService.salvar(pecaProcessoEletronico);
				}

				documentoTexto.setTipoSituacaoDocumento(TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO);
				documentoTextoDao.salvar(documentoTexto);
				documentoEletronicoService.cancelarDocumento(documentoTexto.getDocumentoEletronico(), motivoCancelamento);
			}
		} catch (DaoException e) {
			throw new ServiceException("Excecao ao cancelar o documento", e);
		}

		return true;
	}

	public DocumentoTexto criaDocumentoPdf(Texto texto, byte[] pdf) throws ServiceException {
		DocumentoEletronico doc = new DocumentoEletronico();
		doc.setDescricaoStatusDocumento("RAS");
		doc.setSiglaSistema("ESTFDECISAO");
		doc.setTipoArquivo(TipoArquivo.PDF);
		doc.setArquivo(pdf);
		doc.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
		
		try {
			doc = documentoEletronicoService.salvar(doc);
		} catch (ServiceException e) {
			throw new ServiceException("Erro ao salvar documento eletronico", e);
		}

		DocumentoTexto documentoTexto = new DocumentoTexto();
		documentoTexto.setDocumentoEletronico(doc);
		documentoTexto.setTexto(texto);

		documentoTexto.setTipoSituacaoDocumento(TipoSituacaoDocumento.GERADO);
		
		try {
			documentoTextoService.salvar(documentoTexto);
		} catch (ServiceException e) {
			throw new ServiceException("Erro ao salvar o documento", e);
		}
		return documentoTexto;
	}

}

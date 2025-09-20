package br.gov.stf.estf.assinatura.stficp.impl;

import br.gov.stf.estf.documento.model.service.DocumentoTextoPeticaoService;
import br.gov.stf.estf.entidade.documento.DocumentoTextoPeticao;
import br.gov.stf.framework.util.ApplicationFactory;
import br.jus.stf.assinadorweb.api.negocio.ResultadoAssinatura;
import br.jus.stf.assinadorweb.api.resposta.exception.RespostaException;
import br.jus.stf.assinadorweb.handler.IDocumentoPdfHandler;

public class AssinarDocumentoTextoPeticaoHandler implements IDocumentoPdfHandler<DocumentoTextoPeticao> {

	private static final long serialVersionUID = -2746983348804060223L;

	public byte[] recuperarPDF(DocumentoTextoPeticao dtp) throws RespostaException {
		try {
			return dtp.getDocumentoEletronico().getArquivo();
		} catch (Exception e) {
			throw new RespostaException("Ocorreu um erro ao recuperar o documento texto petição!", e);
		}
	}

	private DocumentoTextoPeticaoService getDocumentoTextoPeticaoService() {
		return ((DocumentoTextoPeticaoService) ApplicationFactory.getInstance().getServiceLocator()
				.getService("documentoTextoPeticaoService"));
	}

	@Override
	public void salvarDocumentoPdf(DocumentoTextoPeticao entidade, ResultadoAssinatura resultadoAssinatura)
			throws RespostaException {
		try {
			getDocumentoTextoPeticaoService().salvarDocumentoTextoPeticaoAssinado(entidade,
					resultadoAssinatura.getPdfAssinado(), resultadoAssinatura.getAssinatura(),
					resultadoAssinatura.getPdfCarimbado(), resultadoAssinatura.getDataCarimboDeTempo());
		} catch (Exception e) {
			throw new RespostaException("Ocorreu um erro ao salvar o documento texto petição assinado!", e);
		}

	}

}

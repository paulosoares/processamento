package br.gov.stf.estf.assinatura.stficp.impl;

import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.framework.util.ApplicationFactory;
import br.jus.stf.assinadorweb.api.negocio.ResultadoAssinatura;
import br.jus.stf.assinadorweb.api.resposta.exception.RespostaException;
import br.jus.stf.assinadorweb.handler.IDocumentoPdfHandler;

public class AssinarDocumentoTextoHandler implements IDocumentoPdfHandler<DocumentoTexto> {

	private static final long serialVersionUID = 8074821456135818875L;

	public byte[] recuperarPDF(DocumentoTexto dt) throws RespostaException {
		try {
			return dt.getDocumentoEletronico().getArquivo();
		} catch (Exception e) {
			throw new RespostaException("Ocorreu um erro ao recuperar o documento texto!", e);
		}
	}

	private DocumentoTextoService getDocumentoTextoService() {
		return ((DocumentoTextoService) ApplicationFactory.getInstance().getServiceLocator()
				.getService("documentoTextoService"));
	}

	@Override
	public void salvarDocumentoPdf(DocumentoTexto entidade, ResultadoAssinatura resultadoAssinatura)
			throws RespostaException {
		try {
			getDocumentoTextoService().salvarDocumentoTextoAssinado(entidade, resultadoAssinatura.getPdfAssinado(),
					resultadoAssinatura.getAssinatura(), resultadoAssinatura.getPdfCarimbado(),
					resultadoAssinatura.getDataCarimboDeTempo());
		} catch (Exception e) {
			throw new RespostaException("Ocorreu um erro ao salvar do documento texto assinado!", e);
		}

	}

}

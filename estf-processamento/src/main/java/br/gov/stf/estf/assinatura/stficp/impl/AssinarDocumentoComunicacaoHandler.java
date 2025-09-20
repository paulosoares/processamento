package br.gov.stf.estf.assinatura.stficp.impl;

import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.framework.security.AcegiSecurityUtils;
import br.gov.stf.framework.util.ApplicationFactory;
import br.jus.stf.assinadorweb.api.negocio.ResultadoAssinatura;
import br.jus.stf.assinadorweb.api.resposta.exception.RespostaException;
import br.jus.stf.assinadorweb.handler.IDocumentoPdfHandler;

public class AssinarDocumentoComunicacaoHandler implements IDocumentoPdfHandler<DocumentoComunicacao> {

	private static final long serialVersionUID = 8074821456135818875L;

	public byte[] recuperarPDF(DocumentoComunicacao dc) throws RespostaException {
		try {
			return dc.getDocumentoEletronico().getArquivo();
		} catch (Exception e) {
			throw new RespostaException("Ocorreu um erro ao recuperar a comunicação para assinatura!", e);
		}

	}

	private DocumentoComunicacaoService getDocumentoComunicacaoService() {
		return ((DocumentoComunicacaoService) ApplicationFactory.getInstance().getServiceLocator()
				.getService("documentoComunicacaoService"));
	}

	@Override
	public void salvarDocumentoPdf(DocumentoComunicacao entidade, ResultadoAssinatura resultadoAssinatura)
			throws RespostaException {
		try {
			getDocumentoComunicacaoService().salvarDocumentoComunicacaoAssinado(entidade,
					resultadoAssinatura.getPdfAssinado(), resultadoAssinatura.getAssinatura(),
					resultadoAssinatura.getPdfCarimbado(), resultadoAssinatura.getDataCarimboDeTempo(),
					AcegiSecurityUtils.getUsername());
		} catch (Exception e) {
			throw new RespostaException("Ocorreu um erro ao salvar a comunicação!", e);
		}

	}

}

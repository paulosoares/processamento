package br.gov.stf.estf.assinatura.stficp.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.framework.util.ApplicationFactory;
import br.jus.stf.assinadorweb.api.negocio.ResultadoAssinatura;
import br.jus.stf.assinadorweb.api.resposta.exception.RespostaException;
import br.jus.stf.assinadorweb.handler.IDocumentoPdfHandler;

public class AssinarArquivoProcessoEletronicoHandler implements IDocumentoPdfHandler<ArquivoProcessoEletronico> {
	private static final Log logger = LogFactory.getLog(AssinarArquivoProcessoEletronicoHandler.class);

	public byte[] recuperarPDF(ArquivoProcessoEletronico ape) throws RespostaException {
		try {
			return ape.getDocumentoEletronico().getArquivo();
		} catch (Exception e) {
			throw new RespostaException("Ocorreu um erro ao recuperar o documento para assinatura!", e);
		}
	}

	private ArquivoProcessoEletronicoService getArquivoEletronicoService() {
		return ((ArquivoProcessoEletronicoService) ApplicationFactory.getInstance().getServiceLocator()
				.getService("arquivoProcessoEletronicoService"));
	}

	@Override
	public void salvarDocumentoPdf(ArquivoProcessoEletronico entidade, ResultadoAssinatura resultadoAssinatura)
			throws RespostaException {
		try {
			getArquivoEletronicoService().salvarArquivoProcessoEletronicoAssinado(entidade,
					resultadoAssinatura.getPdfAssinado(), resultadoAssinatura.getAssinatura(),
					resultadoAssinatura.getPdfCarimbado(), resultadoAssinatura.getDataCarimboDeTempo());
		} catch (Exception e) {
			logger.error("Ocorreu um erro ao salvar o documento assinado!", e);
			throw new RespostaException("Ocorreu um erro ao salvar o documento assinado!", e);
		}
	}

}

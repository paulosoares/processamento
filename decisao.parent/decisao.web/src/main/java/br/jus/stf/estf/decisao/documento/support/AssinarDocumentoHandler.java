package br.jus.stf.estf.decisao.documento.support;

import br.jus.stf.assinadorweb.api.negocio.ResultadoAssinatura;
import br.jus.stf.assinadorweb.api.resposta.exception.RespostaException;
import br.jus.stf.estf.decisao.comunicacao.support.AssinarComunicacaoHandler;
import br.jus.stf.estf.decisao.comunicacao.support.ComunicacaoWrapper;
import br.jus.stf.estf.decisao.support.assinatura.AssinaturaHandler;
import br.jus.stf.estf.decisao.texto.support.AssinarTextoHandler;
import br.jus.stf.estf.decisao.texto.support.TextoWrapper;

public class AssinarDocumentoHandler extends AssinaturaHandler<DocumentoWrapper> {

	private AssinarTextoHandler assinarTextoHandler = new AssinarTextoHandler();
	private AssinarComunicacaoHandler assinarComunicacaoHandler = new AssinarComunicacaoHandler();
	
	@Override
	public byte[] recuperarPDF(DocumentoWrapper entidade) throws RespostaException {
		if (entidade instanceof TextoWrapper) {
			return assinarTextoHandler.recuperarPDF((TextoWrapper)entidade);
		} else if (entidade instanceof ComunicacaoWrapper) {
			return assinarComunicacaoHandler.recuperarPDF((ComunicacaoWrapper)entidade);
		} else {
			throw new RespostaException("Handler não encontrado");
		}
	}

	@Override
	public void salvarDocumentoPdf(DocumentoWrapper entidade, ResultadoAssinatura resultadoAssinatura) throws RespostaException {
		if (entidade instanceof TextoWrapper) {
			assinarTextoHandler.salvarDocumentoPdf((TextoWrapper)entidade, resultadoAssinatura);
		} else if (entidade instanceof ComunicacaoWrapper) {
			assinarComunicacaoHandler.salvarDocumentoPdf((ComunicacaoWrapper)entidade, resultadoAssinatura);
		} else {
			throw new RespostaException("Handler não encontrado");
		}
	}

	

}

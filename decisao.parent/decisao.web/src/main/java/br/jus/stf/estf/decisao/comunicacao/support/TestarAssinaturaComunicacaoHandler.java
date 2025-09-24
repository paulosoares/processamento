package br.jus.stf.estf.decisao.comunicacao.support;

import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.assinadorweb.api.negocio.ResultadoAssinatura;
import br.jus.stf.assinadorweb.api.resposta.exception.RespostaException;
import br.jus.stf.estf.decisao.comunicacao.service.ComunicacaoService;

/**
 * @author Paulo.Estevao
 *
 */
public class TestarAssinaturaComunicacaoHandler extends AssinarComunicacaoHandler {


	private static final String SIGLA_SISTEMA = "ESTFDECISAO";


	private ComunicacaoService comunicacaoService;

	public TestarAssinaturaComunicacaoHandler() {
	}


	public void salvarDocumentoPdf(ComunicacaoWrapper comunicacaoWrapper, ResultadoAssinatura resultadoAssinatura)
			throws RespostaException {
		comunicacaoService = (ComunicacaoService) comunicacaoWrapper.getApplicationContext().getBean(
				"comunicacaoServiceLocal");
		super.salvarDocumentoPdf(comunicacaoWrapper, resultadoAssinatura);
	}

	@Override
	protected void salvarComunicacaoAssinada(ComunicacaoWrapper comunicacaoWrapper,
			ResultadoAssinatura resultadoAssinatura, DocumentoComunicacao documentoComunicacao) throws ServiceException {
		comunicacaoService.testarAssinaturaComunicacao(documentoComunicacao, resultadoAssinatura.getPdfAssinado(),
				resultadoAssinatura.getAssinatura(), resultadoAssinatura.getPdfCarimbado(),
				resultadoAssinatura.getDataCarimboDeTempo(), comunicacaoWrapper.getIdUsuario(), SIGLA_SISTEMA);
	}

}

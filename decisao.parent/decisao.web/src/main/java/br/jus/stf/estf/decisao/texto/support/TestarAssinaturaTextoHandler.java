/**
 * 
 */
package br.jus.stf.estf.decisao.texto.support;

import br.gov.stf.estf.documento.model.service.exception.TransicaoDeFaseInvalidaException;
import br.gov.stf.estf.documento.model.util.AssinaturaDto;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.assinadorweb.api.negocio.ResultadoAssinatura;
import br.jus.stf.assinadorweb.api.resposta.exception.RespostaException;

/**
 * @author Paulo.Estevao
 * @since 24.08.2011
 */
public class TestarAssinaturaTextoHandler extends AssinarTextoHandler {

	private br.jus.stf.estf.decisao.texto.service.TextoService textoServiceLocal;

	public TestarAssinaturaTextoHandler() {
	}

	public void salvarDocumentoPdf(TextoWrapper textoWrapper, ResultadoAssinatura resultadoAssinatura)
			throws RespostaException {
		textoServiceLocal = (br.jus.stf.estf.decisao.texto.service.TextoService) textoWrapper.getApplicationContext()
				.getBean("textoServiceLocal");
		super.salvarDocumentoPdf(textoWrapper, resultadoAssinatura);

	}

	@Override
	protected void assinarTexto(AssinaturaDto assinaturaDto) throws ServiceException, TransicaoDeFaseInvalidaException {
		textoServiceLocal.testarAssinaturaTexto(assinaturaDto);
	}

}

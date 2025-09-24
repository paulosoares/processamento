package br.jus.stf.estf.decisao.comunicacao.support;

import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.assinadorweb.api.negocio.ResultadoAssinatura;
import br.jus.stf.assinadorweb.api.resposta.exception.RespostaException;
import br.jus.stf.estf.decisao.support.assinatura.AssinaturaHandler;

/**
 * @author Paulo.Estevao
 *
 */
public class AssinarComunicacaoHandler extends AssinaturaHandler<ComunicacaoWrapper> {

	private DocumentoComunicacaoService documentoComunicacaoService;

	public AssinarComunicacaoHandler() {
	}

	public byte[] recuperarPDF(ComunicacaoWrapper comunicacaoWrapper) throws RespostaException {
		long start = System.currentTimeMillis();
		try {
			documentoComunicacaoService = (DocumentoComunicacaoService) comunicacaoWrapper.getApplicationContext().getBean("documentoComunicacaoService");
			Long idDocumentoComunicacao = comunicacaoWrapper.getComunicacao().getIdDocumentoComunicacao();
			DocumentoComunicacao documentoComunicacao = documentoComunicacaoService.recuperarPorId(idDocumentoComunicacao);
			return documentoComunicacao.getDocumentoEletronico().getArquivo();
		} catch (ServiceException e) {
			logger.error("Erro ao recuperar o PDF para assinatura", e);
			throw new RespostaException(e);
		} finally {
			long end = System.currentTimeMillis();
			logger.info("Tempo total para recuperação do PDF: [" + (end - start) + "] milisegundos.");
		}
	}

	public void salvarDocumentoPdf(ComunicacaoWrapper comunicacaoWrapper, ResultadoAssinatura resultadoAssinatura) throws RespostaException {
		long start = System.currentTimeMillis();
		documentoComunicacaoService = (DocumentoComunicacaoService) comunicacaoWrapper.getApplicationContext().getBean("documentoComunicacaoService");
		String identificacaoDaComunicacao = montaIdentificacaoDaComunicacao(comunicacaoWrapper);
		try {
			logger.warn("PDF Assinado. Enviando comunicacao [" + identificacaoDaComunicacao+ "] para aplicacao das regras de assinatura...");
			Long idDocumentoComunicacao = comunicacaoWrapper.getComunicacao().getIdDocumentoComunicacao();
			DocumentoComunicacao documentoComunicacao = documentoComunicacaoService.recuperarPorId(idDocumentoComunicacao);
			salvarComunicacaoAssinada(comunicacaoWrapper, resultadoAssinatura, documentoComunicacao);
			logger.warn("Regras de assinatura aplicadas com sucesso.");
		} catch (Exception e) {
			logger.error("Problemas ao aplicar as regras de assinatura para a comunicacao "+ identificacaoDaComunicacao + ". " + e.getMessage(), e);
			throw new RespostaException(e);
		} finally {
			long end = System.currentTimeMillis();
			logger.warn("Tempo para persistencia: [" + (end - start) + "] milisegundos.");
		}
	}

	protected void salvarComunicacaoAssinada(ComunicacaoWrapper comunicacaoWrapper,ResultadoAssinatura resultadoAssinatura, DocumentoComunicacao documentoComunicacao) throws ServiceException {
		documentoComunicacaoService.salvarDocumentoComunicacaoAssinadoeSTFDecisao(documentoComunicacao
																				 ,resultadoAssinatura.getPdfAssinado()
																				 ,resultadoAssinatura.getAssinatura()
																				 ,resultadoAssinatura.getPdfCarimbado()
																				 ,resultadoAssinatura.getDataCarimboDeTempo()
																				 ,comunicacaoWrapper.getIdUsuario()
																				 ,SIGLA_SISTEMA, comunicacaoWrapper.getTipoAssinatura());
	}

	private String montaIdentificacaoDaComunicacao(ComunicacaoWrapper comunicacaoWrapper) {
		return comunicacaoWrapper.getComunicacao().toString() + ":" + comunicacaoWrapper.getComunicacao().getId();
	}

}

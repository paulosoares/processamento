package br.gov.stf.estf.assinatura.service;

import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.stfoffice.handler.HandlerException;

public interface DocumentoHandlerServiceLocal {

	/**
	 * Método que efetua a geração da comunicação, a mudança da fase e a criação do documento eletrônico 
	 * @param pdf
	 * @param comunicacao
	 * @param user
	 * @throws HandlerException
	 */
	@Transactional(rollbackFor = { HandlerException.class })
	public abstract void gerarPdf(byte[] pdf, Comunicacao comunicacao, String user) throws HandlerException;

}
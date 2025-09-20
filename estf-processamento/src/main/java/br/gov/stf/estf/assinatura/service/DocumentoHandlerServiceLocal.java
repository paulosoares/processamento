package br.gov.stf.estf.assinatura.service;

import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.stfoffice.handler.HandlerException;

public interface DocumentoHandlerServiceLocal {

	/**
	 * M�todo que efetua a gera��o da comunica��o, a mudan�a da fase e a cria��o do documento eletr�nico 
	 * @param pdf
	 * @param comunicacao
	 * @param user
	 * @throws HandlerException
	 */
	@Transactional(rollbackFor = { HandlerException.class })
	public abstract void gerarPdf(byte[] pdf, Comunicacao comunicacao, String user) throws HandlerException;

}
package br.gov.stf.estf.assinatura.service;

import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.assinatura.stfoffice.editor.RequisicaoAbrirTexto;
import br.gov.stf.stfoffice.handler.HandlerException;

public interface TextoHandlerServiceLocal {

	/**
	 * Gera o PDF do texto repassado
	 * @param pdf
	 * @param requisicao
	 * @throws HandlerException
	 */
	@Transactional(rollbackFor = HandlerException.class)
	public abstract void gerarPDF(byte[] pdf, RequisicaoAbrirTexto requisicao) throws HandlerException;

	/**
	 * Salva um texto novo na base
	 * @param odt
	 * @param requisicaoNovoTexto
	 * @throws HandlerException
	 */
	@Transactional(rollbackFor = HandlerException.class)
	public abstract void salvarNovoTexto(byte[] odt, RequisicaoAbrirTexto requisicaoNovoTexto) throws HandlerException;

}
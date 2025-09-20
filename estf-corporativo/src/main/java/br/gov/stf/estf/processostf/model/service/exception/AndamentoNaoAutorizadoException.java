package br.gov.stf.estf.processostf.model.service.exception;

import br.gov.stf.framework.model.service.ServiceException;

public class AndamentoNaoAutorizadoException extends ServiceException {

	private static final long serialVersionUID = -939919030395200252L;

	public AndamentoNaoAutorizadoException(String message, Throwable cause) {
		super(message, cause);
	}

	public AndamentoNaoAutorizadoException(String message) {
		super(message);
	}

	public AndamentoNaoAutorizadoException(Throwable cause) {
		super(cause);
	}
}

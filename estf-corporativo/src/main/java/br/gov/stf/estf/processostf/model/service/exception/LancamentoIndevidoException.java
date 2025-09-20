package br.gov.stf.estf.processostf.model.service.exception;

import br.gov.stf.framework.model.service.ServiceException;

public class LancamentoIndevidoException extends ServiceException {

	private static final long serialVersionUID = -939919030395200252L;

	public LancamentoIndevidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public LancamentoIndevidoException(String message) {
		super(message);
	}

	public LancamentoIndevidoException(Throwable cause) {
		super(cause);
	}
}

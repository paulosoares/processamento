package br.gov.stf.estf.processostf.model.service.exception;

import br.gov.stf.framework.model.service.ServiceException;

public class ProcessoFindoException extends ServiceException {

	private static final long serialVersionUID = -939919030395200252L;

	public ProcessoFindoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessoFindoException(String message) {
		super(message);
	}

	public ProcessoFindoException(Throwable cause) {
		super(cause);
	}
}

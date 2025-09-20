package br.gov.stf.estf.processostf.model.service.exception;

import br.gov.stf.framework.model.service.ServiceException;

public class MinistroRelatorAposentadoException extends ServiceException {

	private static final long serialVersionUID = -939919030395200252L;

	public MinistroRelatorAposentadoException(String message, Throwable cause) {
		super(message, cause);
	}

	public MinistroRelatorAposentadoException(String message) {
		super(message);
	}

	public MinistroRelatorAposentadoException(Throwable cause) {
		super(cause);
	}
}

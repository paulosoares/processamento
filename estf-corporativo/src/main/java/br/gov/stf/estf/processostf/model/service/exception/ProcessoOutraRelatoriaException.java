package br.gov.stf.estf.processostf.model.service.exception;

import br.gov.stf.framework.model.service.ServiceException;

public class ProcessoOutraRelatoriaException extends ServiceException {

	private static final long serialVersionUID = -939919030395200252L;

	public ProcessoOutraRelatoriaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessoOutraRelatoriaException(String message) {
		super(message);
	}

	public ProcessoOutraRelatoriaException(Throwable cause) {
		super(cause);
	}
}

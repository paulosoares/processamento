package br.gov.stf.estf.processostf.model.service.exception;

import br.gov.stf.framework.model.service.ServiceException;

public class ProcessoApensadoAOutroException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public ProcessoApensadoAOutroException(String msg) {
		super(msg);
	}
	
	public ProcessoApensadoAOutroException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ProcessoApensadoAOutroException(Throwable cause) {
		super(cause);
	}
}

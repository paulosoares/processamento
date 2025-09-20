package br.gov.stf.estf.processostf.model.service.exception;

import br.gov.stf.framework.model.service.ServiceException;

public class DesapensadoException extends ServiceException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DesapensadoException(String msg) {
		super(msg);
	}
	
	public DesapensadoException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public DesapensadoException(Throwable cause) {
		super(cause);
	}
}

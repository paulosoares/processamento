package br.gov.stf.estf.processostf.model.service.exception;

import br.gov.stf.framework.model.service.ServiceException;

public class AIApensadoNaoREException extends ServiceException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AIApensadoNaoREException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	
	public AIApensadoNaoREException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public AIApensadoNaoREException(Throwable cause) {
		super(cause);
	}

}

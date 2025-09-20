package br.gov.stf.estf.processostf.model.service.exception;

import br.gov.stf.framework.model.service.ServiceException;

public class ProcessoNaoAutuadoException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProcessoNaoAutuadoException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	
	public ProcessoNaoAutuadoException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ProcessoNaoAutuadoException(Throwable cause) {
		super(cause);
	}

}

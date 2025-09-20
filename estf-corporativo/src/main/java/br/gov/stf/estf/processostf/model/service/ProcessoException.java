package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.framework.model.service.ServiceException;

public class ProcessoException extends ServiceException{
	
	
	private static final long serialVersionUID = 1L;

	public ProcessoException(Throwable t) {
		super(t);
	}

	public ProcessoException(String msg) {
		super(msg);
	}

	public ProcessoException(String msg, Throwable t) {
		super(msg, t);
	}
	


}

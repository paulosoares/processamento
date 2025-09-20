package br.gov.stf.estf.julgamento.model.exception;

import br.gov.stf.framework.model.service.ServiceException;

public class TemaException extends ServiceException{
	
	
	private static final long serialVersionUID = 1L;

	public TemaException(Throwable t) {
		super(t);
	}

	public TemaException(String msg) {
		super(msg);
	}

	public TemaException(String msg, Throwable t) {
		super(msg, t);
	}
	


}

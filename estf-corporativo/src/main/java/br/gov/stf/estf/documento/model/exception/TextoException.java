package br.gov.stf.estf.documento.model.exception;

import br.gov.stf.framework.model.service.ServiceException;

public class TextoException extends ServiceException{
	
	
	private static final long serialVersionUID = 1L;

	public TextoException(Throwable t) {
		super(t);
	}

	public TextoException(String msg) {
		super(msg);
	}

	public TextoException(String msg, Throwable t) {
		super(msg, t);
	}
	


}
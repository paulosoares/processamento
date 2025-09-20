package br.gov.stf.estf.processostf.model.service.exception;

import br.gov.stf.framework.model.service.ServiceException;

public class ProcessoImagemException extends ServiceException{
	
	
	private static final long serialVersionUID = 1L;

	public ProcessoImagemException(Throwable t) {
		super(t);
	}

	public ProcessoImagemException(String msg) {
		super(msg);
	}

	public ProcessoImagemException(String msg, Throwable t) {
		super(msg, t);
	}
	


}

package br.gov.stf.estf.repercussaogeral.model.service;

import br.gov.stf.framework.model.service.ServiceException;

public class ControleVistaException extends ServiceException{

	private static final long serialVersionUID = 1L;

	public ControleVistaException(Throwable t) {
		super(t);
	}
	public ControleVistaException(String msg) {
		super(msg);
	}
	public ControleVistaException (String msg, Throwable t) {
		super(msg, t);
	}
}


package br.gov.stf.estf.repercussaogeral.model.service;

import br.gov.stf.framework.model.service.ServiceException;



public class RepercussaoGeralException extends ServiceException{
	
	
	private static final long serialVersionUID = 1L;

	public RepercussaoGeralException(Throwable t) {
		super(t);
	}

	public RepercussaoGeralException(String msg) {
		super(msg);
	}

	public RepercussaoGeralException(String msg, Throwable t) {
		super(msg, t);
	}
	


}

package br.gov.stf.estf.processostf.model.service.exception;

public class NaoExistemDeslocamentosException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8477560210538032487L;

	public NaoExistemDeslocamentosException() {
	}

	public NaoExistemDeslocamentosException(String message) {
		super(message);
	}

	public NaoExistemDeslocamentosException(Throwable cause) {
		super(cause);
	}

	public NaoExistemDeslocamentosException(String message, Throwable cause) {
		super(message, cause);
	}

}

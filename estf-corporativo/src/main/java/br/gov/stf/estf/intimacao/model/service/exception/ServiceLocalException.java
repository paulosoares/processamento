package br.gov.stf.estf.intimacao.model.service.exception;

public class ServiceLocalException extends Exception {

	private static final long serialVersionUID = 8220539946114066798L;

	public ServiceLocalException() {
		super();
	}

	public ServiceLocalException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceLocalException(String message) {
		super(message);
	}

	public ServiceLocalException(Throwable cause) {
		super(cause);
	}
}

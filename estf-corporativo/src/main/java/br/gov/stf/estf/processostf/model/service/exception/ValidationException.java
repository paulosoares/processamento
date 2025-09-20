package br.gov.stf.estf.processostf.model.service.exception;

public class ValidationException extends Exception {
	
	private static final long serialVersionUID = 8877888172492969213L;

	public ValidationException(Throwable t) {
        super(t);
    }

    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(String msg, Throwable t) {
        super(msg, t);
    }
}

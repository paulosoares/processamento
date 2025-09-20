package br.gov.stf.estf.assinatura.visao.jsf.beans;

public class ValidacaoException extends Exception {

	private static final long serialVersionUID = -5478312142763931908L;

	public ValidacaoException() {
		super();
	}

	public ValidacaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidacaoException(String message) {
		super(message);
	}

	public ValidacaoException(Throwable cause) {
		super(cause);
	}
}

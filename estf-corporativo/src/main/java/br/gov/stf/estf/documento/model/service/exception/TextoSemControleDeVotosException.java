package br.gov.stf.estf.documento.model.service.exception;

public class TextoSemControleDeVotosException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2531184837098400394L;

	public TextoSemControleDeVotosException() {
	}

	public TextoSemControleDeVotosException(String message) {
		super(message);
	}

	public TextoSemControleDeVotosException(Throwable cause) {
		super(cause);
	}

	public TextoSemControleDeVotosException(String message, Throwable cause) {
		super(message, cause);
	}

}

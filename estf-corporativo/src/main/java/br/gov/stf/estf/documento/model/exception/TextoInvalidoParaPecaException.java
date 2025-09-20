package br.gov.stf.estf.documento.model.exception;

public class TextoInvalidoParaPecaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3183203869322771735L;

	public TextoInvalidoParaPecaException() {
	}

	public TextoInvalidoParaPecaException(String message) {
		super(message);
	}

	public TextoInvalidoParaPecaException(Throwable cause) {
		super(cause);
	}

	public TextoInvalidoParaPecaException(String message, Throwable cause) {
		super(message, cause);
	}

}

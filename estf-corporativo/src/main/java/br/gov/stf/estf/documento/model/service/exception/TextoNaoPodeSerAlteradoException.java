package br.gov.stf.estf.documento.model.service.exception;


public class TextoNaoPodeSerAlteradoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3806181610354386589L;

	public TextoNaoPodeSerAlteradoException() {
	}

	public TextoNaoPodeSerAlteradoException(String message) {
		super(message);
	}

	public TextoNaoPodeSerAlteradoException(Throwable cause) {
		super(cause);
	}

	public TextoNaoPodeSerAlteradoException(String message, Throwable cause) {
		super(message, cause);
	}

}

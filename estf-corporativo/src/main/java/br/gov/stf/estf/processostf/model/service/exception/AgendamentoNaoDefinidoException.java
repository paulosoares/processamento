package br.gov.stf.estf.processostf.model.service.exception;

public class AgendamentoNaoDefinidoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5012721019326557543L;

	public AgendamentoNaoDefinidoException() {
	}

	public AgendamentoNaoDefinidoException(String message) {
		super(message);
	}

	public AgendamentoNaoDefinidoException(Throwable cause) {
		super(cause);
	}

	public AgendamentoNaoDefinidoException(String message, Throwable cause) {
		super(message, cause);
	}

}

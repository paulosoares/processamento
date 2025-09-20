package br.gov.stf.estf.localizacao.model.service.exception;

public class NaoExisteSetorParaDeslocamentoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4557119740804848802L;

	public NaoExisteSetorParaDeslocamentoException() {
	}

	public NaoExisteSetorParaDeslocamentoException(String message) {
		super(message);
	}

	public NaoExisteSetorParaDeslocamentoException(Throwable cause) {
		super(cause);
	}

	public NaoExisteSetorParaDeslocamentoException(String message,
			Throwable cause) {
		super(message, cause);
	}

}

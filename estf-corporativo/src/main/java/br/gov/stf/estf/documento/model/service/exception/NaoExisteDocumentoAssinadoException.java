package br.gov.stf.estf.documento.model.service.exception;

public class NaoExisteDocumentoAssinadoException extends Exception {

	private static final long serialVersionUID = -7152678054021240356L;

	public NaoExisteDocumentoAssinadoException() {
		super("O texto não está assinado!");
	}

	public NaoExisteDocumentoAssinadoException(String message) {
		super(message);
	}

	public NaoExisteDocumentoAssinadoException(Throwable cause) {
		super(cause);
	}

	public NaoExisteDocumentoAssinadoException(String message, Throwable cause) {
		super(message, cause);
	}

}

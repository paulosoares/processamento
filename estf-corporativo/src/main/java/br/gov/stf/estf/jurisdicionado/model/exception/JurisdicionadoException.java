package br.gov.stf.estf.jurisdicionado.model.exception;


public class JurisdicionadoException extends Exception{

	private static final long serialVersionUID = 8673201207514663749L;
	
	public JurisdicionadoException(String msg) {
		super(msg);
	}
	
	public JurisdicionadoException(Throwable cause) {
		super(cause);
	}

	public JurisdicionadoException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
	

}

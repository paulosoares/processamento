/**
 * 
 */
package br.jus.stf.estf.decisao.texto.support;

/**
 * @author Paulo.Estevao
 * @since 27.03.2012
 */
public class SetorInativoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8072862943206049250L;

	public SetorInativoException() {
	}

	public SetorInativoException(String message) {
		super(message);
	}

	public SetorInativoException(Throwable cause) {
		super(cause);
	}

	public SetorInativoException(String message,
			Throwable cause) {
		super(message, cause);
	}
}

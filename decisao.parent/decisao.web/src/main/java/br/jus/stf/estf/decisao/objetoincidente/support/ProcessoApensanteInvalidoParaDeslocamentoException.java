/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.support;

/**
 * @author Paulo.Estevao
 * @since 26.08.2013
 */
public class ProcessoApensanteInvalidoParaDeslocamentoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7234316128421953449L;

	public ProcessoApensanteInvalidoParaDeslocamentoException() {
	}

	public ProcessoApensanteInvalidoParaDeslocamentoException(String message) {
		super(message);
	}

	public ProcessoApensanteInvalidoParaDeslocamentoException(Throwable cause) {
		super(cause);
	}

	public ProcessoApensanteInvalidoParaDeslocamentoException(String message,
			Throwable cause) {
		super(message, cause);
	}
}

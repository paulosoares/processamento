package br.jus.stf.estf.decisao.documento.support.assinador.exception;

public class ValidacaoCertificadoException extends Exception {

	private static final long serialVersionUID = -2083479979250069680L;

	public ValidacaoCertificadoException(Throwable t) {
		super(t);
	}

	public ValidacaoCertificadoException(String msg) {
		super(msg);
	}

	public ValidacaoCertificadoException(String msg, Throwable t) {
		super(msg, t);
	}

}

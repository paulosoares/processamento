package br.gov.stf.estf.publicacao.model.dataaccess.hibernate;

public class ConteudoPublicacaoNaoEncontradoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5290559152639049747L;

	public ConteudoPublicacaoNaoEncontradoException(String msg, Throwable t) {
		super(msg, t);
	}

	public ConteudoPublicacaoNaoEncontradoException(String msg) {
		super(msg);
	}

	public ConteudoPublicacaoNaoEncontradoException(Throwable t) {
		super(t);
	}

	public ConteudoPublicacaoNaoEncontradoException() {
	}
}

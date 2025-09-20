package br.gov.stf.estf.documento.model.service.exception;

/**
 * Indica que um determinado texto não pertence a nenhuma lista de textos iguais.
 * 
 * @author Rodrigo Barreiros
 * @since 04.06.2009
 */
public class TextoNaoPertenceAListaDeTextosIguaisException extends Exception {

	private static final String DEFAULT_MESSAGE = "Essa ação só se aplica a textos em lista de textos iguais.";
	
	private static final long serialVersionUID = 1L;

	public TextoNaoPertenceAListaDeTextosIguaisException() {
		super(DEFAULT_MESSAGE);
	}

}

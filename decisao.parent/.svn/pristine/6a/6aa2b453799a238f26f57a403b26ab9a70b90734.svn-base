package br.jus.stf.estf.decisao.texto.service;

/**
 * Indica que um texto já foi juntado.
 * 
 * @author Rodrigo Barreiros
 * @see 22.07.2010
 */
public class TextoJaJuntadoException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constroi a exceção montando a mensagem em função do flag de disponibilização
	 * das peças juntadas.
	 * 
	 * @param disponibilizarNaInternet flag de disponibilização
	 */
	public TextoJaJuntadoException(boolean disponibilizarNaInternet) {
		super(getMessage(disponibilizarNaInternet));
	}

	/**
	 * Montagem a mensagem de erro indicando qual a situação atual.
	 * 
	 * @param disponibilizarNaInternet flag de disponibilização
	 * @return a mensagem montada
	 */
	private static String getMessage(boolean disponibilizarNaInternet) {
		String mensagem = "O texto já estava juntado ao processo.";
		if (disponibilizarNaInternet) {
			mensagem += " Apenas a disponibilização na internet foi realizada!";
		} else {
			mensagem += " Nenhuma alteração foi realizada.";
		}
		return mensagem;
	}

}

package br.gov.stf.estf.entidade.util;

/**
 * Classe para guardar as informa��o usadas para montar o texto de decis�o pelo edecis�o
 */
public class DadosTextoDecisao {
	public String textoDecisao;
	public String nomeMinistroRelator;
	public String relator;
	public String getTextoDecisao() {
		return textoDecisao;
	}
	public void setTextoDecisao(String textoDecisao) {
		this.textoDecisao = textoDecisao;
	}
	public String getNomeMinistroRelator() {
		return nomeMinistroRelator;
	}
	public void setNomeMinistroRelator(String nomeMinistroRelator) {
		this.nomeMinistroRelator = nomeMinistroRelator;
	}
	public String getRelator() {
		return relator;
	}
	public void setRelator(String relator) {
		this.relator = relator;
	}
	
}
/**
 * 
 */
package br.gov.stf.estf.entidade.tesauro;

/**
 * @author Paulo.Estevao
 * @since 12.07.2012
 */
public enum TipoSituacaoTermo {

	APROVADO("A", "Aprovado"), 
	CANDIDATO("C", "Candidato"), 
	PROVISORIO("P", "Provisório"), 
	NAO_APROVADO("N", "Não aprovado");
	
	private String sigla;
	private String descricao;
	
	private TipoSituacaoTermo(String sigla, String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}
	
	public String getSigla() {
		return sigla;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static TipoSituacaoTermo valueOfSigla(String sigla) {
		for (TipoSituacaoTermo tst : TipoSituacaoTermo.values()) {
			if (tst.getSigla().equals(sigla)) {
				return tst;
			}
		}
		return null;
	}
}

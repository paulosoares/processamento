/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

/**
 * @author Paulo.Estevao
 * @since 11.07.2012
 */
public enum TipoClassificacao {
	
	ACORDAO_PRINCIPAL("AP", "Acórdão Principal"), ACORDAO_SUCESSIVO("AS", "Acórdão Sucessivo");
	
	private String sigla;
	private String descricao;
	
	private TipoClassificacao(String sigla, String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}
	
	public String getSigla() {
		return sigla;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static TipoClassificacao valueOfSigla(String sigla) {
		for (TipoClassificacao tipoClassificacao : TipoClassificacao.values()) {
			if (tipoClassificacao.getSigla().equals(sigla)) {
				return tipoClassificacao;
			}
		}
		return null;
	}
	
}

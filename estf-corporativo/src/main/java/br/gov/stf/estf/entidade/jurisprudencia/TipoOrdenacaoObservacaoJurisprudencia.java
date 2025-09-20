/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

/**
 * @author Paulo.Estevao
 * @since 08.10.2012
 */
public enum TipoOrdenacaoObservacaoJurisprudencia {
	
	INICIAL("I", "Inicial"),
	FINAL("F", "Final");

	private String sigla;
	private String observacao;
	
	private TipoOrdenacaoObservacaoJurisprudencia(String sigla, String observacao) {
		this.sigla = sigla;
		this.observacao = observacao;
	}
	
	public String getSigla() {
		return sigla;
	}
	
	public String getObservacao() {
		return observacao;
	}
	
	public static TipoOrdenacaoObservacaoJurisprudencia valueOfSigla(String sigla) {
		for (TipoOrdenacaoObservacaoJurisprudencia tipoOrdenacao : TipoOrdenacaoObservacaoJurisprudencia.values()) {
			if (tipoOrdenacao.getSigla().equals(sigla)) {
				return tipoOrdenacao;
			}
		}
		
		return null;
	}
}

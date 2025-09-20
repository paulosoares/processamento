package br.gov.stf.estf.model.util;

public enum TipoOrdem {
	CRESCENTE("Crescente"), DECRESCENTE("Decrescente");

	private String descricao;

	private TipoOrdem(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public static TipoOrdem getTipoOrdem(String descricao) {
		if (CRESCENTE.getDescricao().equals(descricao)) {
			return CRESCENTE;
		} else if (DECRESCENTE.getDescricao().equals(descricao)) {
			return DECRESCENTE;
		} else {
			return null;
		}
	}

}

package br.gov.stf.estf.entidade.julgamento;

public enum TipoManifestacao {
	QUESTAO_FATO("QF", "Questao de Fato"),
	MEMORIAL("MM", "Memorial"),
	SUSTENTACAO_ORAL("SO", "Sustentação Oral");
	
	private final String codigo;
	private final String descricao;

	private TipoManifestacao(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static TipoManifestacao valueOfSigla(String sigla) {
		for (TipoManifestacao tipo : values()) {
			if (tipo.getCodigo().equals(sigla)) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Nenhum elemento encontrado com a sigla informada: " + sigla);
	}
}
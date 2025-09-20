package br.gov.stf.estf.entidade.julgamento;

public enum SituacaoManifestacaoRepresentante {
	EM_ANALISE("EM ANÁLISE", "Em análise"),
	APROVADO("APROVADO", "Aprovado"),
	REJEITADO("REJEITADO", "Rejeitado");
	
	private final String codigo;
	private final String descricao;

	private SituacaoManifestacaoRepresentante(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static SituacaoManifestacaoRepresentante valueOfSigla(String sigla) {
		for (SituacaoManifestacaoRepresentante tipo : values()) {
			if (tipo.getCodigo().equals(sigla)) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Nenhum elemento encontrado com a sigla informada: " + sigla);
	}
}
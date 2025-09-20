package br.gov.stf.estf.processosetor.model.util;

public enum TipoGroupBy {
	DISTRIBUICAO("Distribuição"), DESLOCAMENTO("Deslocamento"), FASE("Fase"), FASE_STATUS("Fase Status"), ASSUNTO("Assunto");

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	private TipoGroupBy(String descricao) {
		this.descricao = descricao;
	}

	public static TipoGroupBy getTipoGroupBy(String descricao) {
		if (DISTRIBUICAO.getDescricao().equals(descricao)) {
			return DISTRIBUICAO;
		} else if (DESLOCAMENTO.getDescricao().equals(descricao)) {
			return DESLOCAMENTO;
		} else if (FASE.getDescricao().equals(descricao)) {
			return FASE;
		} else if (FASE_STATUS.getDescricao().equals(descricao)) {
			return FASE_STATUS;
		} else if (ASSUNTO.getDescricao().equals(descricao)) {
			return ASSUNTO;
		} else {
			return null;
		}
	}

}
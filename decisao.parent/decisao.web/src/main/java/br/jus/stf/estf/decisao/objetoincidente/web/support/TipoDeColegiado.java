package br.jus.stf.estf.decisao.objetoincidente.web.support;

public enum TipoDeColegiado {

	PLENO("P", "Tribunal Pleno"), PRIMEIRA_TURMA("1T", "Primeira Turma"), SEGUNDA_TURMA("2T", "Segunda Turma");

	private String chave;
	private String descricao;

	TipoDeColegiado(String chave, String descricao) {
		this.chave = chave;
		this.descricao = descricao;
	}

	public String getChave() {
		return chave;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoDeColegiado getTipoDeColegiadoPelaChave(String chave) {
		for (TipoDeColegiado tipoDeColegiado : values()) {
			if (tipoDeColegiado.getChave().equals(chave)) {
				return tipoDeColegiado;
			}
		}
		return null;
	}
}

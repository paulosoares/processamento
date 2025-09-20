package br.gov.stf.estf.entidade.documento;

/**
 * @author Paulo.Estevao
 * @since 28.11.2013
 */
public enum TipoAdendo {
	COMENTARIO("C", "Comentario"), NOTA("N", "Nota"), ROTULO("R", "Rotulo"), OBSERVACAO("O", "Observação");

	private String sigla;
	private String descricao;

	private TipoAdendo(String sigla, String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}

	public static TipoAdendo valueOfSigla(String sigla) {
		for (TipoAdendo tipoPermissao : values()) {
			if (tipoPermissao.getSigla().equals(sigla)) {
				return tipoPermissao;
			}
		}

		throw new IllegalArgumentException("Nenhum elemento encontrado com a sigla informada: " + sigla);
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getSigla() {
		return sigla;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}

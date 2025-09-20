package br.gov.stf.estf.entidade.processostf;
public enum TipoNivelConfidencialidade {

	NIVEL999(999, "Não classificado"),
	NIVEL0(0, "Público (0)"),
	NIVEL1(1, "Segredo de Justiça (1)"),
	NIVEL2(2, "Sigilo Moderado (2)"),
	NIVEL3(3, "Sigilo Padrão (3)"),
	NIVEL4(4, "Sigilo Máximo (4)");
	
	private final Integer codigo;
	private final String descricao;

	private TipoNivelConfidencialidade(Integer codigo) {
		this(codigo, "Tipo Confidencialidade:" + codigo);
	}

	private TipoNivelConfidencialidade(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoNivelConfidencialidade valueOfCodigo(Integer codigo) {
		for (TipoNivelConfidencialidade tipoConfidencialidade : values()) {
			if (tipoConfidencialidade.getCodigo().equals(codigo)) {
				return tipoConfidencialidade;
			}
		}
		return NIVEL999;
	}
	
	
}

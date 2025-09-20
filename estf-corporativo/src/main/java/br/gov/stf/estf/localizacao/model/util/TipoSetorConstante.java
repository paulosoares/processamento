package br.gov.stf.estf.localizacao.model.util;

public enum TipoSetorConstante {
	
	VICE_PRESIDENCIA_STF("Vice-Presidência do STF", Long.valueOf(600000716)),
	PRESIDENCIA_STF("Presidência do STF", Long.valueOf(600000179)),
	SECRETARIA_GERAL_PRESIDENCIA("SECRETARIA-GERAL DA PRESIDENCIA", Long.valueOf(600000181));

	private Long codigo;
	private String descricao;
	
	private TipoSetorConstante(String descricao, Long codigo) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
}

package br.gov.stf.estf.localizacao.model.util;

public enum TipoLocalizacao {
	
	ADVOGADO("Advogado", Short.valueOf("1")),
	INTERNA("Interna", Short.valueOf("2")),
	EXTERNA("Externa", Short.valueOf("3"));

	private Short codigo;
	private String descricao;
	
	private TipoLocalizacao(String descricao, Short codigo) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public Short getCodigo() {
		return codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
}

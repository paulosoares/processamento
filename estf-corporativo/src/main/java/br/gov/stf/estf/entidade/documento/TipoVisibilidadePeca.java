package br.gov.stf.estf.entidade.documento;

public enum TipoVisibilidadePeca {
	
	PUBLICA("PUB", "Público"),
	INTERNA("INT", "Interno");
	
	private final String id;
	private final String descricao;
	
	private TipoVisibilidadePeca(String id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public String getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

}

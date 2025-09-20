package br.gov.stf.estf.entidade.jurisdicionado.enuns;

public enum EnumTipoTelefone {
	
	C("Celular"), R("Residencial"), F("Comercial");

	private String descricao;

	EnumTipoTelefone(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}

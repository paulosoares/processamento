package br.gov.stf.estf.entidade.processostf;

public enum TipoMeioPeticaoInicial {
	E("Eletrônico", "E"),
	F("Físico", "F");

	private String	codigo;
	private String	descricao;


	private TipoMeioPeticaoInicial(String descricao, String codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}


	public String getDescricao() {
		return this.descricao;
	}


	public String getCodigo() {
		return this.codigo;
	}


	public TipoMeioPeticaoInicial getTipoMeioPeticaoInicial(String codigo) {
		if (TipoMeioPeticaoInicial.E.getCodigo().equals(codigo)) {
			return TipoMeioPeticaoInicial.E;
		} else {
			return TipoMeioPeticaoInicial.F;
		}
	}
}

package br.gov.stf.estf.usuario.model.util;

public enum TipoTurma {
	
	PRIMEIRA_TURMA("1", "Primeira Turma"),
	SEGUNDA_TURMA("2", "Segunda Turma"),
	INDEFINIDO("0", "Indefinido");
	
	private String descricao;
	private String codigo;
	
	private TipoTurma(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public TipoTurma getTipoTurma(String codigo) {

		if( TipoTurma.PRIMEIRA_TURMA.getCodigo().equals(codigo))
			return TipoTurma.PRIMEIRA_TURMA;		
		else if( TipoTurma.SEGUNDA_TURMA.getCodigo().equals(codigo))
			return TipoTurma.SEGUNDA_TURMA;
		return TipoTurma.INDEFINIDO;
	}
}

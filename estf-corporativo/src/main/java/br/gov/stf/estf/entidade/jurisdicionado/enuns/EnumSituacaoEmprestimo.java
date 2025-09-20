package br.gov.stf.estf.entidade.jurisdicionado.enuns;

public enum EnumSituacaoEmprestimo {
	
	EM_ATRASO(1L,"Em atraso"),
	EM_CURSO(2L,"Em curso"),
	DIA_DEVOLUCAO(3L,"Dia de devolução"),
	DEVOLVIDO(4L,"Devolvido"),
	DEVOLVIDO_ATRASO(5L,"Devolvido em atraso");

	private Long codigo;
	private String descricao;
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	private EnumSituacaoEmprestimo(Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

}

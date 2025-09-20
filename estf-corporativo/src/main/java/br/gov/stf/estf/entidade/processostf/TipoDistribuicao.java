package br.gov.stf.estf.entidade.processostf;

public enum TipoDistribuicao {
	COMUM(2L,"DISTRIBUIÇÃO COMUM"),
	REDISTRIBUICAO(4L,"REDISTRIBUIÇÃO"),
	REGISTRADO(5L,"REGISTRADO"),
	SUBSTITUICAO(6L,"SUBSTITUIÇÃO");
	
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
	
	private TipoDistribuicao(Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public static TipoDistribuicao valueOf(Long codigo) {
		if (codigo != null) {
			for (TipoDistribuicao td : values()) {
				if (codigo.equals(td.getCodigo())) {
					return td;
				}
			}
		}
		throw new RuntimeException("Nao existe TipoDistribuicao com codigo: " + codigo);
	}
}

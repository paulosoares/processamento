package br.gov.stf.estf.processostf.model.util;

public enum ProcessoIntegracaoEnum {
	NAO_ENVIADO("Não Lido", "N"),	
	ENVIADO("Lido", "E"),	
	TODOS("Todos", "T");

	private String descricao;
	private String codigo;
	
	private ProcessoIntegracaoEnum(String descricao, String codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}
}

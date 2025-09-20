package br.gov.stf.estf.model.util;

public enum TipoOrdemProcesso {
	PROCESSO("Processo"), 
	PROTOCOLO("Protocolo"), 
	PETICAO("Petição"), 
	VALOR_GUT("Valor GxUxT"), 
	DT_ENTRADA("Dt. entrada"), 
	DT_ANDAMENTO("Dt. andamento"), 
	ASSUNTO("Assunto"),
	ORIGEM("Origem"),
	TEMA("Tema"),
	MOTIVO_INAPTIDAO("Motivo Inaptidão");

	private String descricao;

	private TipoOrdemProcesso(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public static TipoOrdemProcesso getTipoOrdemProcesso(String descricao) {
		TipoOrdemProcesso tipoOrdemProcesso = null;
		
		for (TipoOrdemProcesso tipoOrdem : values()) {
			if (tipoOrdem.getDescricao().equals(descricao)) {
				tipoOrdemProcesso = tipoOrdem;
				break;
			}
		}
		
		return tipoOrdemProcesso;
	}
}
package br.gov.stf.estf.entidade.julgamento;

public enum TipoResultadoJulgamento {
	DECISAO_UNANIME(1, "Decisão unânime"),
	MAIORIA_A_FAVOR_DO_RELATOR(2, "Decisão por maioria"),
	MAIORIA_CONTRA_O_RELATOR(3, "Vencido o relator"),	
	EMPATE(4, "Empate"); 
	
	private final Integer codigo;
	private final String descricao;

	private TipoResultadoJulgamento(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoResultadoJulgamento valueOf(Integer codigo) {
		for (TipoResultadoJulgamento TipoResultado : values()) 
			if (TipoResultado.getCodigo().equals(codigo)) 
				return TipoResultado;
		
		return null;
	}
}
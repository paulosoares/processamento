package br.gov.stf.estf.entidade.processostf;


/**
 *OC - Oculto SJ - Segredo de Justiça SG - Sigiloso
 */
public enum TipoConfidencialidade {

	/**
	 * 
	 */
//	private static final long serialVersionUID = 8615490188288713939L;
//	public static final TipoConfidencialidade OCULTO = new TipoConfidencialidade("OC", "Oculto");
//	public static final TipoConfidencialidade SEGREDO_JUSTICA = new TipoConfidencialidade("SJ", "Segredo de Justiça");
//	public static final TipoConfidencialidade SIGILOSO = new TipoConfidencialidade("SG", "Sigiloso");
	
	OCULTO("OC", "Oculto"),
	SEGREDO_JUSTICA("SJ", "Segredo de Justiça"),
	SIGILOSO("SG", "Sigiloso");

	private final String codigo;
	private final String descricao;

	private TipoConfidencialidade(String codigo) {
		this(codigo, "Tipo Confidencialidade:" + codigo);
	}

	private TipoConfidencialidade(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoConfidencialidade valueOfCodigo(String codigo) {
		for (TipoConfidencialidade tipoConfidencialidade : values()) {
			if (tipoConfidencialidade.getCodigo().equalsIgnoreCase(codigo)) {
				return tipoConfidencialidade;
			}
		}
		return null;
	}
	
	
}

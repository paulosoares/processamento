/**
 * 
 */
package br.gov.stf.estf.entidade.processostf;



/**
 * @author Paulo.Estevao
 * @since 24.06.2011
 */
public enum TipoVinculoObjeto {
	
	DECISAO_STF_DESCUMPRIDA(1L, "Decisão STF descumprida"),
	PRECEDENTE_DE_SUMULA_VINCULANTE(2L, "Precedente de súmula vinculante"),
	DEPENDE_DO_JULGAMENTO(3L, "Depende do julgamento");
	
	private Long codigo;
	private String descricao;
	
	private TipoVinculoObjeto (Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

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
	
	public static TipoVinculoObjeto valueOf(Long codigo) {
		if (codigo != null) {
			for (TipoVinculoObjeto tipoVinculo : values()) {
				if (codigo.equals(tipoVinculo.getCodigo())) {
					return tipoVinculo;
				}
			}
		} 
		throw new RuntimeException("Nao existe TipoVinculoObjeto com codigo: "
				+ codigo);
	}	
}

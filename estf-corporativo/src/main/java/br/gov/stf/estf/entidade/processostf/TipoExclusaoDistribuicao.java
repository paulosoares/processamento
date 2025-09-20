package br.gov.stf.estf.entidade.processostf;

/**
 * Tipos de Exclusão de Ministros da Distribuição
 * @author RicardoLe
 *
 */

public enum TipoExclusaoDistribuicao {
	PRESIDENTESTF(1L,"PRESIDENTE DO STF"),
	PRESIDENTETSE(2L,"PRESIDENTE DO TSE"),
	AUSENCIA(3L,"AUSÊNCIA - ART 67"),
	PRESIDENTECNJ(4L,"PRESIDENTE DO CNJ"),
	APOSENTADORIA(5L,"APOSENTADORIA");

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
	
	private TipoExclusaoDistribuicao(Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public static TipoExclusaoDistribuicao valueOf(Long codigo) {
		if (codigo != null) {
			for (TipoExclusaoDistribuicao td : values()) {
				if (codigo.equals(td.getCodigo())) {
					return td;
				}
			}
		}
		throw new RuntimeException("Nao existe TipoExclusaoDistribuicao com codigo: " + codigo);
	}

}

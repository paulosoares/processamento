/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
public enum TribunalJurisprudenciaConstante {

	STF( 7L, "SUPREMO TRIBUNAL FEDERAL", null),
	STJ(15L, "SUPERIOR TRIBUNAL DE JUSTIÇA", 1L),
	TSE(14L, "TRIBUNAL SUPERIOR ELEITORAL", 2L),
	TST(18L, "TRIBUNAL SUPERIOR DO TRABALHO", 3L),
	STM( 8L, "SUPERIOR TRIBUNAL MILITAR", 4L),
	OUT( 0L, "OUTROS", 5L);
	
	private Long codigo;
	private String descricao;
	private Long ordem;
	
	private TribunalJurisprudenciaConstante(Long codigo, String descricao, Long ordem) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.ordem = ordem;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public Long getOrdem() {
		return ordem;
	}
	
	public static TribunalJurisprudenciaConstante valueOf(Long codigo) {
		if (codigo == null) {
			return null;
		}
		
		for (TribunalJurisprudenciaConstante tribunal : TribunalJurisprudenciaConstante.values()) {
			if (tribunal.getCodigo().equals(codigo)) {
				return tribunal;
			}
		}
		
		return null;
	}
}

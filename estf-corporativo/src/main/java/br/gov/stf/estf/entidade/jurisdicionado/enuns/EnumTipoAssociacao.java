package br.gov.stf.estf.entidade.jurisdicionado.enuns;

/**
 * 
 * 1: Parte-Advogado (Parte-Postulante);
   2: Advogado-Advogado;
   3: Advogado-Estagiário;
   4: Parte-Estagiário.

 * @author RicardoLe
 *
 */

public enum EnumTipoAssociacao {
	PARTE_ADVOGADO(1L,"Parte-Advogado"),
	ADVOGADO_ADVOGADO(2L,"Advogado-Advogado"),
	ADVOGADO_ESTAGIARIO(3L,"Advogado-Estagiário"),
	PARTE_ESTAGIARIO(4L,"Parte-Estagiário"),
	ADVOGADO_PREPOSTO(5L,"Advogado-Preposto"),
	PARTE_PREPOSTO(6L,"Parte-Preposto");

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
	
	private EnumTipoAssociacao(Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public static EnumTipoAssociacao valueOf(Long codigo) {
		if (codigo != null) {
			for (EnumTipoAssociacao td : values()) {
				if (codigo.equals(td.getCodigo())) {
					return td;
				}
			}
		}
		throw new RuntimeException("Nao existe EnumTipoAssociacao com codigo: " + codigo);
	}

}

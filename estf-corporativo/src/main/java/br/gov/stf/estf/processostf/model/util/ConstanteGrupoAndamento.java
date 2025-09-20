package br.gov.stf.estf.processostf.model.util;

public enum ConstanteGrupoAndamento {
	
	BAIXA_NAO_DEFINITIVA((long)4),
	FINALIZADO((long)12);
	
	private Long codigo;
	
	private ConstanteGrupoAndamento(Long codigo){
		this.codigo = codigo;
	}
	
	public Long getCodigo(){
		return this.codigo;
	}
	
}

package br.gov.stf.estf.entidade.processostf;

public enum ConstanteAndamento {
	
	DECISAO_EXISTENCIA_REPERCUSSAO_GERAL((long)6300),
	DECISAO_INEXISTENCIA_REPERCUSSAO_GERAL((long)6301),
	DECISAO_INEXISTENCIA_CONTITUCIONAL_REPERCUSSAO_GERAL((long)6306),
	INICIADA_ANALISE_REPERCUSSAO_GERAL((long)7602),
	DECISAO_MERITO_JULGADO((long)6308),
	LANCAMENTO_INDEVIDO1((long)7700),
	LANCAMENTO_INDEVIDO2((long)1067);
	
	private Long codigo;
	
	private ConstanteAndamento(Long codigo){
		this.codigo = codigo;
	}
	
	public Long getCodigo(){
		return this.codigo;
	}
	
}

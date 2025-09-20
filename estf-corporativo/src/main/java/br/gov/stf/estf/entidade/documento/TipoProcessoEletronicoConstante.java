package br.gov.stf.estf.entidade.documento;

public enum TipoProcessoEletronicoConstante {
	VOTO_SOBRE_REPERCUSSAO_GERAL((int)61,"Voto Sobre Repercussão Geral");
	    
	    private TipoProcessoEletronicoConstante(Integer codigo,String descricao) {
	        this.codigo = codigo;
	        this.descricao = descricao;
	    }
	    
	    private Integer codigo;
	    private String descricao;

	    public Integer getCodigo() {
	        return codigo;
	    }
	    
	    public String getDescricao() {
	        return descricao;
	    }
}

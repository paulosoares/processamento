package br.gov.stf.estf.entidade.processosetor;


public class EstatisticaProcessoSetor {
	
	private Long quantidade;
    private String descricao1;
    private String descricao2;
    private String descricao3;

    
    public EstatisticaProcessoSetor() {
        
    }
    
    public EstatisticaProcessoSetor(Long quantidade) {
        this.quantidade = quantidade;
    }
    
    public EstatisticaProcessoSetor(String descricao1, Long quantidade) {
        this.descricao1 = descricao1;
        this.quantidade = quantidade;
    }
    
    
    public EstatisticaProcessoSetor(String descricao1, String descricao2, Long quantidade) {
        this(descricao1, quantidade);
        
        this.descricao2 = descricao2;
    }    
    
    public EstatisticaProcessoSetor(String descricao1, String descricao2, String descricao3, Long quantidade) {
        this(descricao1, descricao2, quantidade);
        
        this.descricao3 = descricao3;
    }    

    public String getDescricao1() {
        return descricao1;
    }

    public void setDescricao1(String descricao1) {
        this.descricao1 = descricao1;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao2() {
        return descricao2;
    }

    public void setDescricao2(String descricao2) {
        this.descricao2 = descricao2;
    }

	public String getDescricao3() {
		return descricao3;
	}

	public void setDescricao3(String descricao3) {
		this.descricao3 = descricao3;
	}    
    
}

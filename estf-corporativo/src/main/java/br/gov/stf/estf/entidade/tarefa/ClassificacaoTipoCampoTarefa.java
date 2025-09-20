package br.gov.stf.estf.entidade.tarefa;

public enum ClassificacaoTipoCampoTarefa {
	 	NU("Num�rico"),
	    TL("Texto livre"),
	    TA("Tabela"),
	    DA("Data"),
	    CL("Classe Processual"),
	    HA("Hora");
	    private ClassificacaoTipoCampoTarefa(String descricao) {
	        setDescricao(descricao);
	    }
	    
	    private String descricao;

	    public String getDescricao() {
	        return descricao;
	    }
	    
	    public void setDescricao(String descricao) {
	        this.descricao = descricao;
	    }
}

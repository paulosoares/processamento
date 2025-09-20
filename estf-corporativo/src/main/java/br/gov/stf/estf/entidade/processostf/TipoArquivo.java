package br.gov.stf.estf.entidade.processostf;

import java.io.Serializable;


public enum TipoArquivo implements Serializable {
	
	PDF (5,"application/pdf");
        
        private Integer codigo = null;        
	private String descricao = null;        
	
	private TipoArquivo(Integer codigo, String descricao) {
	    this.codigo = codigo;
            this.descricao = descricao;
	}
        
        public Integer getCodigo() {
            return codigo;
        }
	
	public String getDescricao() {
            return descricao;
	}
        
}

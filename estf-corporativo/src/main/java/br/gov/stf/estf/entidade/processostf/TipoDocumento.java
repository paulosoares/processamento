package br.gov.stf.estf.entidade.processostf;

import java.io.Serializable;

public enum TipoDocumento implements Serializable {
	
	ACORDAO (1,"Acórdão"),
	DJ (2,"Diário da Justiça"),
	TXT_PROCESSO (5,"Texto do processo");        
	
	private String descricao = null;
        private Integer codigo = null;
	
	private TipoDocumento(Integer codigo, String descricao) {
            this.descricao = descricao;
	}
	
	public String getDescricao() {
            return descricao;
	}
        
        public Integer getCodigo() {
            return codigo;
        }
        
}
package br.gov.stf.estf.entidade.processostf;

import java.io.Serializable;

public enum TipoDocumento implements Serializable {
	
	ACORDAO (1,"Ac�rd�o"),
	DJ (2,"Di�rio da Justi�a"),
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
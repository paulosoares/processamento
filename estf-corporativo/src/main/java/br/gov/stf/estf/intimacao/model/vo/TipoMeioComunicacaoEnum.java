package br.gov.stf.estf.intimacao.model.vo;

/**
 *
 * @author Roberio.Fernandes
 */
public enum TipoMeioComunicacaoEnum {

	FISICA("F","F�sica"),
    ELETRONICA("E","Eletr�nica");
        
    private TipoMeioComunicacaoEnum(String id, String descricao) {
    	this.id = id;
		this.descricao = descricao;
	}
    
    private String id;
    private String descricao;
    
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
    
}
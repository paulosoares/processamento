package br.gov.stf.estf.intimacao.model.dataaccess;

/**
 *
 * @author Roberio.Fernandes
 */
public enum TipoMeioIntimacaoEnum {

    FISICA("F", "Física"),
    ELETRONICA("E", "Eletrônica");

    private TipoMeioIntimacaoEnum(String id, String descricao) {
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

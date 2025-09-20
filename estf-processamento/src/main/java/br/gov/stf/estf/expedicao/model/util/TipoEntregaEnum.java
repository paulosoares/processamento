package br.gov.stf.estf.expedicao.model.util;


/**
 *
 * @author Roberio.Fernandes
 */
public enum TipoEntregaEnum {

    CORREIOS("Correios"),
    ENTREGA_PORTARIA("Entrega Portaria"),
    MALOTE("Malote");

    private final String descricao;

	private TipoEntregaEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
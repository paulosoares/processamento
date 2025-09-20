package br.gov.stf.estf.expedicao.model.util;

import br.gov.stf.estf.expedicao.entidade.TipoEmbalagem;

/**
 *
 * @author Roberio.Fernandes
 */
public enum TipoEmbalagemEnum {

    ENVELOPE("001", "Envelope"),
    CAIXA_PACOTE("002", "Caixa/Pacote");

    private final String codigo;
    private final String descricao;

    private TipoEmbalagemEnum(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

	public static TipoEmbalagemEnum buscar(String tipo) {
		TipoEmbalagemEnum embalagemEnum = null;
		for (TipoEmbalagemEnum embalagemEnumAux : values()) {
			if (embalagemEnumAux.getCodigo().equals(tipo)) {
				embalagemEnum = embalagemEnumAux;
				break;
			}
		}
		return embalagemEnum;
	}

	public static TipoEmbalagemEnum buscar(TipoEmbalagem tipoEmbalagem) {
		TipoEmbalagemEnum embalagemEnum = null;
		if (tipoEmbalagem != null) {
			embalagemEnum = buscar(tipoEmbalagem.getTipo());
		}
		return embalagemEnum;
	}
}
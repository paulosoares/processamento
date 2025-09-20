package br.gov.stf.estf.expedicao.entidade;

/**
 *
 * @author Roberio.Fernandes
 */
public enum ValorFlagSimNaoEnum {

    SIM("S", true),
    NAO("N", false);

    private final String valorTexto;
    private final boolean valorBoolean;

    private ValorFlagSimNaoEnum(String valorTexto, boolean valorBoolean) {
        this.valorTexto = valorTexto;
        this.valorBoolean = valorBoolean;
    }

    public String getValorTexto() {
        return valorTexto;
    }

    public boolean getValorBoolean() {
        return valorBoolean;
    }

    /**
     * Retorna o elemento cujo valor boolean seja igual ao informado.
     *
     * @param valor
     * @return 
     */
    public static ValorFlagSimNaoEnum buscar(boolean valor) {
        ValorFlagSimNaoEnum valorFlagSimNaoEnum = null;
        for (ValorFlagSimNaoEnum value : values()) {
            if (value.valorBoolean == valor) {
                valorFlagSimNaoEnum = value;
                break;
            }
        }
        return valorFlagSimNaoEnum;
    }
}
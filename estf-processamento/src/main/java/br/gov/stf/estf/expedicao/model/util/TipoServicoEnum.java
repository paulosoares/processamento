package br.gov.stf.estf.expedicao.model.util;

/**
 *
 * @author Roberio.Fernandes
 */
public enum TipoServicoEnum {

    ADICIONAL("C", 0),
    POSTAGEM("P", 0),
    MALOTE("M", 81),
    ENCOMENDA("E", 51);

    private final String codigo;
    private final int valor;

    private TipoServicoEnum(String codigo, int valor) {
        this.codigo = codigo;
        this.valor = valor;
    }

    public String getCodigo() {
        return codigo;
    }
    
    public int getValor(){
    	return valor;
    }
}
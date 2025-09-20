package br.gov.stf.estf.expedicao.model.util;

/**
 *
 * @author Roberio.Fernandes
 */
public enum TipoServicoCodigoEnum {

    MAOS(1L, ""),
    MALOTE(2L, ""),
    PAC(3L, ""),
    SEDEX(4L, ""),
    AR(5L, "AR"),
    MAO_PROPRIA(6L, "MP"),
    RN(7L, ""),
    SEDEX10(8L, ""),
    CARTA(9L, ""),
    DEVOLUCAO_DOCUMENTOS(10L, "DD"),
    VALOR_DECLARADO(11L, "VD");

    private final Long codigo;
    private final String sigla; 

    private TipoServicoCodigoEnum(Long codigo, String sigla) {
        this.codigo = codigo;
        this.sigla = sigla;
    }

    public Long getCodigo() {
        return codigo;
    }
    
    public String getSigla(){
    	return sigla;
    }

    public static TipoServicoCodigoEnum buscarPeloCodigo(Long codigo) {
        TipoServicoCodigoEnum resultado = null;
        for (TipoServicoCodigoEnum value : values()) {
            if (value.getCodigo().equals(codigo)) {
                resultado = value;
                break;
            }
        }
        return resultado;
    }

    public static TipoServicoCodigoEnum buscarPeloCodigo(String codigo) {
        TipoServicoCodigoEnum resultado = null;
        if (codigo != null) {
            resultado = buscarPeloCodigo(Long.parseLong(codigo));
        }
        return resultado;
    }
}

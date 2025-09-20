package br.gov.stf.estf.intimacao.entidade;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Roberio.Fernandes
 */
public enum TipoModeloComunicacaoEnum {

    MANDADO(21, "Mandado", false),
    OFICIO_TELEGRAMA(22, "Ofício Telegrama", false),
    CARTA_POSTAL(23, "Carta Postal", false),
    EDITAL(24, "Edital", false),
    OFICIO(25, "Ofício", false),
    ALVARA(26, "Alvará", false),
    CARTA(31, "Carta", false),
    OFICIO_FAX(33, "Ofício Fax", false),
    MENSAGEM(7050, "Mensagem", false),
    TELEX(22100, "Telex", false),
    SALVO_CONDUTO(20050, "Salvo Conduto", false),
    CITACAO(0, "", true),
    INTIMACAO_DECISAO(0, "", true),
    INTIMACAO_PAUTA(0, "", true);

    private final long codigo;
    private final String descricao;
    private final boolean intimacaoEletronica;

    private TipoModeloComunicacaoEnum(long codigo, String descricao, boolean intimacaoEletronica) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.intimacaoEletronica = intimacaoEletronica;
    }

    public long getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isIntimacaoEletronica() {
        return intimacaoEletronica;
    }

    public static List<TipoModeloComunicacaoEnum> getTiposIntimacaoEletronica() {
        List<TipoModeloComunicacaoEnum> resultado = new ArrayList<TipoModeloComunicacaoEnum>();
        for (TipoModeloComunicacaoEnum value : values()) {
            if (value.isIntimacaoEletronica()) {
                resultado.add(value);
            }
        }
        return resultado;
    }
    
	public static TipoModeloComunicacaoEnum getEnum(long value){
        for(int i=0; i<values().length; i++){
            if(values()[i].getCodigo() == value){
                return values()[i];
            }
        }
        return  null;
    }
}
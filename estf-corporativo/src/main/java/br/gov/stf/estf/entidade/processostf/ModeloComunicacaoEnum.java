package br.gov.stf.estf.entidade.processostf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Roberio.Fernandes
 */
public enum ModeloComunicacaoEnum {

    MANDADO(ModeloComunicacaoEnum.DESCRICAO_TIPO_INTIMACAO_FISICA, "Mandado"), //TODO: alterar para "Mandado de Intimação"
    CARTA(ModeloComunicacaoEnum.DESCRICAO_TIPO_INTIMACAO_FISICA, "Carta"), //TODO: alterar para "Carta de Intimação"
    INTIMACAO_DESPACHO_DECISAO_ACORDAO(ModeloComunicacaoEnum.DESCRICAO_TIPO_INTIMACAO_ELETRONICA, "Intimação de Despacho, Decisão ou Acórdão"),
    CITACAO(ModeloComunicacaoEnum.DESCRICAO_TIPO_INTIMACAO_ELETRONICA, "Citação"),
    NOTIFICACAO_DOWNLOAD(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Notificação para download de peça"),
    ATO_ORDINATORIO(ModeloComunicacaoEnum.DESCRICAO_TIPO_INTIMACAO_ELETRONICA, "Ato Ordinatório"),
    NOTIFICACAO_BAIXA_AUTOS(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Baixa dos Autos"),
    NOTIFICACAO_VISTA(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Notificação de Vista"),
    NOTIFICACA_REQUISICAO_PROCESSO(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Requisição de Processo"),
    NOTIFICACAO_REQUISICAO_PROCESSO_SEJ(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Requisição de Processo SEJ"),
	NOTIFICACAO_DE_PAUTA(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Notificação de Pauta"),
    VISTA_A_PGR(ModeloComunicacaoEnum.DESCRICAO_TIPO_VISTA , "Vista à PGR"),
    VISTA_A_AGU(ModeloComunicacaoEnum.DESCRICAO_TIPO_VISTA , "Vista ao AGU"),
    NOTIFICACAO_INDEVIDO_VISTA_INTIMACAO(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Lançamento Indevido em Vista à PGR para fins de intimação"),
    NOTIFICACAO_INDEVIDO_VISTA(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Lançamento Indevido em Vista à PGR"),
    NOTIFICACAO_INDEVIDO_VISTA_AGU(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Lançamento Indevido em Vista ao AGU"),
    NOTIFICACAO_AUTOS_DISPONIBILIZADOS(ModeloComunicacaoEnum.DESCRICAO_AUTOS_DISPONIBILIZADOS, "Autos disponibilizados à autoridade policial"),
	NOTIFICACAO_INDEVIDO_AUTOS_DISPONIBILIZADOS(ModeloComunicacaoEnum.DESCRICAO_AUTOS_DISPONIBILIZADOS, "Lançamento Indevido em Autos disponibilizados à autoridade policial");
    

    public static final String DESCRICAO_TIPO_INTIMACAO_FISICA = "Intimação Física";
    public static final String DESCRICAO_TIPO_INTIMACAO_ELETRONICA = "Intimação Eletrônica";
    public static final String DESCRICAO_TIPO_NOTIFICACAO = "Notificação";
    public static final String DESCRICAO_TIPO_VISTA = "Vista";
    public static final String DESCRICAO_AUTOS_DISPONIBILIZADOS = "Notificação";

    private final String descricaoTipoComunicacao;
    private final String descricaoModelo;

    private ModeloComunicacaoEnum(String descricaoTipoComunicacao, String descricaoModelo) {
        this.descricaoTipoComunicacao = descricaoTipoComunicacao;
        this.descricaoModelo = descricaoModelo;
    }

    public String getDescricaoTipoComunicacao() {
        return descricaoTipoComunicacao;
    }

    public String getDescricaoModelo() {
        return descricaoModelo;
    }

    public static List<ModeloComunicacaoEnum> getModeloComunicacaoEnum() {
        List<ModeloComunicacaoEnum> resultado = new ArrayList<ModeloComunicacaoEnum>();
        resultado.addAll(Arrays.asList(values()));
        return resultado;
    }

    public static List<ModeloComunicacaoEnum> getModeloComunicacaoEletronicaEnum() {
        List<ModeloComunicacaoEnum> resultado = new ArrayList<ModeloComunicacaoEnum>();
        for (ModeloComunicacaoEnum value : values()) {
            if (value != MANDADO && value != CARTA) {
                resultado.add(value);
            }
        }

        Collections.sort(resultado, new Comparator<ModeloComunicacaoEnum>() {
            @Override
            public int compare(ModeloComunicacaoEnum modeloEnum1, ModeloComunicacaoEnum modeloEnum2) {
                return modeloEnum1.descricaoModelo.compareTo(modeloEnum2.descricaoModelo);
            }
        });

        return resultado;
    }

    public static List<ModeloComunicacaoEnum> getModeloComunicacaoEletronicaEnum(String descricao) {
        List<ModeloComunicacaoEnum> resultado = new ArrayList<ModeloComunicacaoEnum>();
        for (ModeloComunicacaoEnum value : values()) {
            if (value.getDescricaoTipoComunicacao().equalsIgnoreCase(descricao)) {
                resultado.add(value);
            }
        }

        Collections.sort(resultado, new Comparator<ModeloComunicacaoEnum>() {
            @Override
            public int compare(ModeloComunicacaoEnum modeloEnum1,
                    ModeloComunicacaoEnum modeloEnum2) {
                return modeloEnum1.descricaoModelo
                        .compareTo(modeloEnum2.descricaoModelo);
            }
        });

        return resultado;
    }

    public static List<String> getDescricoesTipoComunicacao() {
        List<String> resultado = new ArrayList<String>();

        resultado.add(DESCRICAO_TIPO_INTIMACAO_ELETRONICA);
        resultado.add(DESCRICAO_TIPO_NOTIFICACAO);
        resultado.add(DESCRICAO_TIPO_VISTA);

        return resultado;
    }
}

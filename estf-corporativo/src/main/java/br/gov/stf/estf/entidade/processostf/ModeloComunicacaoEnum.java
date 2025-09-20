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

    MANDADO(ModeloComunicacaoEnum.DESCRICAO_TIPO_INTIMACAO_FISICA, "Mandado"), //TODO: alterar para "Mandado de Intima��o"
    CARTA(ModeloComunicacaoEnum.DESCRICAO_TIPO_INTIMACAO_FISICA, "Carta"), //TODO: alterar para "Carta de Intima��o"
    INTIMACAO_DESPACHO_DECISAO_ACORDAO(ModeloComunicacaoEnum.DESCRICAO_TIPO_INTIMACAO_ELETRONICA, "Intima��o de Despacho, Decis�o ou Ac�rd�o"),
    CITACAO(ModeloComunicacaoEnum.DESCRICAO_TIPO_INTIMACAO_ELETRONICA, "Cita��o"),
    NOTIFICACAO_DOWNLOAD(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Notifica��o para download de pe�a"),
    ATO_ORDINATORIO(ModeloComunicacaoEnum.DESCRICAO_TIPO_INTIMACAO_ELETRONICA, "Ato Ordinat�rio"),
    NOTIFICACAO_BAIXA_AUTOS(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Baixa dos Autos"),
    NOTIFICACAO_VISTA(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Notifica��o de Vista"),
    NOTIFICACA_REQUISICAO_PROCESSO(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Requisi��o de Processo"),
    NOTIFICACAO_REQUISICAO_PROCESSO_SEJ(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Requisi��o de Processo SEJ"),
	NOTIFICACAO_DE_PAUTA(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Notifica��o de Pauta"),
    VISTA_A_PGR(ModeloComunicacaoEnum.DESCRICAO_TIPO_VISTA , "Vista � PGR"),
    VISTA_A_AGU(ModeloComunicacaoEnum.DESCRICAO_TIPO_VISTA , "Vista ao AGU"),
    NOTIFICACAO_INDEVIDO_VISTA_INTIMACAO(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Lan�amento Indevido em Vista � PGR para fins de intima��o"),
    NOTIFICACAO_INDEVIDO_VISTA(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Lan�amento Indevido em Vista � PGR"),
    NOTIFICACAO_INDEVIDO_VISTA_AGU(ModeloComunicacaoEnum.DESCRICAO_TIPO_NOTIFICACAO, "Lan�amento Indevido em Vista ao AGU"),
    NOTIFICACAO_AUTOS_DISPONIBILIZADOS(ModeloComunicacaoEnum.DESCRICAO_AUTOS_DISPONIBILIZADOS, "Autos disponibilizados � autoridade policial"),
	NOTIFICACAO_INDEVIDO_AUTOS_DISPONIBILIZADOS(ModeloComunicacaoEnum.DESCRICAO_AUTOS_DISPONIBILIZADOS, "Lan�amento Indevido em Autos disponibilizados � autoridade policial");
    

    public static final String DESCRICAO_TIPO_INTIMACAO_FISICA = "Intima��o F�sica";
    public static final String DESCRICAO_TIPO_INTIMACAO_ELETRONICA = "Intima��o Eletr�nica";
    public static final String DESCRICAO_TIPO_NOTIFICACAO = "Notifica��o";
    public static final String DESCRICAO_TIPO_VISTA = "Vista";
    public static final String DESCRICAO_AUTOS_DISPONIBILIZADOS = "Notifica��o";

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

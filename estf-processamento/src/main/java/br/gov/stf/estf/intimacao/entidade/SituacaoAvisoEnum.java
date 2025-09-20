package br.gov.stf.estf.intimacao.entidade;

/**
 *
 * @author Roberio.Fernandes
 */
public enum SituacaoAvisoEnum {

    NAO_ENVIADO("Não enviado", "N"),
    ENVIADO("Enviado", "E"),
    RECEBIDO("Recebido", "R"),
    COM_PROBLEMA("Com problema", "P"),
    CONFIRMA_INTIMACAO("Confirma intimação", "I"),
    EM_TRANSMISSAO("Em transmissão", "T");

    private final String descricao;
    private final String codigo;

    private SituacaoAvisoEnum(String descricao, String codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public static SituacaoAvisoEnum buscarPeloCodigo(String codigo) {
        SituacaoAvisoEnum resultado = null;
        for (SituacaoAvisoEnum value : values()) {
            if (value.getCodigo().equals(codigo)) {
                resultado = value;
                break;
            }
        }
        return resultado;
    }
}
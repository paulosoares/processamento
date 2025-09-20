package br.gov.stf.estf.intimacao.model.vo;

/**
 *
 * @author Roberio.Fernandes
 */
public class IntimacaoEletronicaVO {

    private final String nomeParte;
    private final String numeroProcesso;

    public IntimacaoEletronicaVO(String nomeParte, String numeroProcesso) {
        this.nomeParte = nomeParte;
        this.numeroProcesso = numeroProcesso;
    }

    public String getNomeParte() {
        return nomeParte;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }
}
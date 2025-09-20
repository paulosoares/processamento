package br.gov.stf.estf.intimacao.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
*
* @author Roberio.Fernandes
*/
@Entity
@Table(name = "TIPO_COMUNICACAO", schema = "ESTF")
public class TipoComunicacaoESTF extends ESTFBaseEntity<Integer> {

    private static final long serialVersionUID = -3803141335791528327L;

    private String descricao;
    private String sigla;

    @Id
    @Column(name = "COD_TIPO_COMUNICACAO")
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "DSC_TIPO_COMUNICACAO", unique = false, nullable = false, insertable = true, updatable = true, length = 1000)
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Column(name = "SIG_TIPO_COMUNICACAO", unique = false, nullable = false, insertable = true, updatable = true, length = 1000)
    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Transient
    public String getIdentificacao() {
        return id + " - " + descricao;
    }
}
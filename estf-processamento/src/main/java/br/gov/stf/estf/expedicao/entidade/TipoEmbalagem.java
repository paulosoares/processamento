package br.gov.stf.estf.expedicao.entidade;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "TIPO_EMBALAGEM")
public class TipoEmbalagem extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String tipo;
    private Integer larguraCm;
    private Integer comprimentoCm;
    private Integer alturaCm;

    public TipoEmbalagem() {
    }

    @Id
    @Column(name = "SEQ_TIPO_EMBALAGEM")
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "NOM_TIPO_EMBALAGEM")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Basic(optional = false)
    @Column(name = "TIP_EMBALAGEM")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Column(name = "QTD_LARGURA_CM")
    public Integer getLarguraCm() {
        return larguraCm;
    }

    public void setLarguraCm(Integer larguraCm) {
        this.larguraCm = larguraCm;
    }

    @Column(name = "QTD_COMPRIMENTO_CM")
    public Integer getComprimentoCm() {
        return comprimentoCm;
    }

    public void setComprimentoCm(Integer comprimentoCm) {
        this.comprimentoCm = comprimentoCm;
    }

    @Column(name = "QTD_ALTURA_CM")
    public Integer getAlturaCm() {
        return alturaCm;
    }

    public void setAlturaCm(Integer alturaCm) {
        this.alturaCm = alturaCm;
    }
}
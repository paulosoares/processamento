package br.gov.stf.estf.corp.entidade;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "CORP", name = "LOGRADOURO")
public class Logradouro extends ESTFBaseEntity<String> {

    private static final long serialVersionUID = 1L;

    private String tipo;
    private String nome;
    private String cep;
    private Boolean correio;

    private Bairro bairro;
    private Municipio municipio;

    public Logradouro() {
    }

    @Id
    @Basic(optional = false)
    @Column(name = "SEQ_LOGRADOURO")
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "TIP_LOGRADOURO")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Column(name = "NOM_LOGRADOURO")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "NUM_CEP")
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Column(name = "FLG_CORREIO")
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
    public Boolean getCorreio() {
        return correio;
    }

    public void setCorreio(Boolean correio) {
        this.correio = correio;
    }

    @JoinColumn(name = "SEQ_BAIRRO", referencedColumnName = "SEQ_BAIRRO")
    @ManyToOne(fetch = FetchType.EAGER)
    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    @JoinColumn(name = "SEQ_MUNICIPIO", referencedColumnName = "SEQ_MUNICIPIO")
    @ManyToOne(fetch = FetchType.EAGER)
    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }
}
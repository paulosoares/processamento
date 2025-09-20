package br.gov.stf.estf.corp.entidade;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "CORP", name = "BAIRRO")
public class Bairro extends ESTFBaseEntity<String> {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String municipio;
    private Boolean correios;

    private List<Logradouro> logradouros;

    public Bairro() {
    }

    @Id
    @Basic(optional = false)
    @Column(name = "SEQ_BAIRRO")
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "NOM_BAIRRO")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Basic(optional = false)
    @Column(name = "SEQ_MUNICIPIO")
    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    @Column(name = "FLG_CORREIO")
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
    public Boolean getCorreios() {
        return correios;
    }

    public void setCorreios(Boolean correios) {
        this.correios = correios;
    }

    @OneToMany(mappedBy = "bairro")
    public List<Logradouro> getLogradouros() {
        return logradouros;
    }

    public void setLogradouros(List<Logradouro> logradouros) {
        this.logradouros = logradouros;
    }
}
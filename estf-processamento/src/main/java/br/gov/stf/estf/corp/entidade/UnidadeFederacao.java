package br.gov.stf.estf.corp.entidade;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "CORP", name = "UNIDADE_FEDERACAO")
public class UnidadeFederacao extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String sigla;
    private String nome;
    private Boolean ativo;

    private Pais pais;
    private List<Municipio> municipios;

    public UnidadeFederacao() {
    }

    @Id
    @Basic(optional = false)
    @Column(name = "SEQ_UNIDADE_FEDERACAO")
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "SIG_UNIDADE_FEDERACAO")
    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Basic(optional = false)
    @Column(name = "NOM_UNIDADE_FEDERACAO")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Basic(optional = false)
    @Column(name = "FLG_ATIVO")
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @JoinColumn(name = "SEQ_PAIS", referencedColumnName = "SEQ_PAIS")
    @ManyToOne(optional = false)
    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    @OneToMany(mappedBy = "unidadeFederacao")
    public List<Municipio> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
    }
}
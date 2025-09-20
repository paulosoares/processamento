package br.gov.stf.estf.corp.entidade;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(schema = "CORP", name = "PAIS")
public class Pais extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String sigla;
    private String siglaInternacional;
    private String nome;
    private Boolean ativo;
    private String nomeCompleto;

    private List<UnidadeFederacao> unidadesFederacao;

    public Pais() {
    }

    @Id
    @Basic(optional = false)
    @Column(name = "SEQ_PAIS")
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "SIG_PAIS")
    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Column(name = "SIG_PAIS_INTERNACIONAL")
    public String getSiglaInternacional() {
        return siglaInternacional;
    }

    public void setSiglaInternacional(String siglaInternacional) {
        this.siglaInternacional = siglaInternacional;
    }

    @Basic(optional = false)
    @Column(name = "NOM_PAIS")
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

    @Column(name = "NOM_PAIS_COMPLETO")
    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pais")
    public List<UnidadeFederacao> getUnidadesFederacao() {
        return unidadesFederacao;
    }

    public void setUnidadesFederacao(List<UnidadeFederacao> unidadesFederacao) {
        this.unidadesFederacao = unidadesFederacao;
    }
}
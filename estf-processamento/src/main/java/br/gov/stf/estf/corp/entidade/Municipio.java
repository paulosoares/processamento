package br.gov.stf.estf.corp.entidade;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(schema = "CORP", name = "MUNICIPIO")
public class Municipio extends ESTFBaseEntity<String> {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String siglaUf;
    private String tipo;
    private String cep;
    private String situacao;
    private int procedenciaGeografica;
    private Boolean correio;
    private Boolean ativo;
    private String idMunicipioSubordinacao;

    private UnidadeFederacao unidadeFederacao;

    public Municipio() {
    }

    @Id
    @Basic(optional = false)
    @Column(name = "SEQ_MUNICIPIO")
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "NOM_MUNICIPIO")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "SIG_UF")
    public String getSiglaUf() {
        return siglaUf;
    }

    public void setSiglaUf(String siglaUf) {
        this.siglaUf = siglaUf;
    }

    @Column(name = "TIP_MUNICIPIO")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Column(name = "NUM_CEP")
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Column(name = "SIT_MUNICIPIO")
    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @Basic(optional = false)
    @Column(name = "SEQ_PROCEDENCIA_GEOGRAFICA")
    public int getProcedenciaGeografica() {
        return procedenciaGeografica;
    }

    public void setProcedenciaGeografica(int procedenciaGeografica) {
        this.procedenciaGeografica = procedenciaGeografica;
    }

    @Column(name = "FLG_CORREIO")
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
    public Boolean getCorreio() {
        return correio;
    }

    public void setCorreio(Boolean correio) {
        this.correio = correio;
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

    @JoinColumn(name = "SEQ_UNIDADE_FEDERACAO", referencedColumnName = "SEQ_UNIDADE_FEDERACAO")
    @ManyToOne
    public UnidadeFederacao getUnidadeFederacao() {
        return unidadeFederacao;
    }

    public void setUnidadeFederacao(UnidadeFederacao unidadeFederacao) {
        this.unidadeFederacao = unidadeFederacao;
    }

    @Column(name = "SEQ_MUNICIPIO_SUBORDINACAO")
    public String getIdMunicipioSubordinacao() {
        return idMunicipioSubordinacao;
    }

    public void setIdMunicipioSubordinacao(String idMunicipioSubordinacao) {
        this.idMunicipioSubordinacao = idMunicipioSubordinacao;
    }
}
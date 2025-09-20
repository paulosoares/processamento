package br.gov.stf.estf.expedicao.entidade;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.corp.entidade.Municipio;
import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "REMETENTE")
public class Remetente extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String nomeRemetente;
    private String cnpj;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private Municipio municipio;
    private String cep;

    public Remetente() {
    }

    @Id
    @Column(name = "SEQ_REMETENTE")
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "NOM_REMETENTE")
    public String getNomeRemetente() {
        return nomeRemetente;
    }

    public void setNomeRemetente(String nomeRemetente) {
        this.nomeRemetente = nomeRemetente;
    }

    @Column(name = "NUM_CNPJ")
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Basic(optional = false)
    @Column(name = "DSC_LOGRADOURO")
    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    @Basic(optional = false)
    @Column(name = "NUM_NUMERO")
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Column(name = "DSC_COMPLEMENTO")
    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    @Basic(optional = false)
    @Column(name = "DSC_BAIRRO")
    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    @Basic(optional = false)
    @JoinColumn(name = "SEQ_MUNICIPIO", referencedColumnName = "SEQ_MUNICIPIO")
    @ManyToOne(fetch = FetchType.EAGER)
    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    @Basic(optional = false)
    @Column(name = "NUM_CEP")
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
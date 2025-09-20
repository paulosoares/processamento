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
@Table(schema = "EXPEDICAO", name = "VW_ENDERECO")
public class VwEndereco extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String logradouro;
    private String cep;
    private String bairro;
    private String seqMunicipio;
    private String municipio;
    private String uf;
    private String cliente;

    public VwEndereco() {
    }

    @Id
    @Basic(optional = false)
    @Column(name = "SEQ_ENDERECO")
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NOM_LOGRADOURO")
    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    @Column(name = "NUM_CEP")
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Column(name = "NOM_BAIRRO")
    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    @Column(name = "SEQ_MUNICIPIO")
    public String getSeqMunicipio() {
        return seqMunicipio;
    }

    public void setSeqMunicipio(String seqMunicipio) {
        this.seqMunicipio = seqMunicipio;
    }

    @Column(name = "NOM_MUNICIPIO")
    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    @Column(name = "SIG_UF")
    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    @Column(name = "NOM_GRANDE_USUARIO")
    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
}
package br.gov.stf.estf.expedicao.entidade;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.corp.entidade.Municipio;
import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "DESTINATARIO")
public class DestinatarioListaRemessa extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String descricaoAnterior;
    private String descricaoPrincipal;
    private String descricaoPosterior;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private Municipio municipio;
    private String cep;
    private String nomeContato;
    private String email;
    private String codigoAreaTelefone;
    private String numeroTelefone;
    private String codigoAreaFax;
    private String numeroFax;
    private String agrupador;
    private Short codigoOrigem;
    private String usuarioInclusao;
    private String usuarioAlteracao;
    private Date dataInclusao;
    private Date dataAlteracao;
    private String observacao;

    public DestinatarioListaRemessa() {
    }

    @Id
    @Column(name = "SEQ_DESTINATARIO")
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequence", sequenceName = "EXPEDICAO.SEQ_DESTINATARIO", allocationSize = 1)
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "DSC_ANTERIOR")
    public String getDescricaoAnterior() {
        return descricaoAnterior;
    }

    public void setDescricaoAnterior(String descricaoAnterior) {
        this.descricaoAnterior = descricaoAnterior;
    }

    @Column(name = "DSC_PRINCIPAL")
    public String getDescricaoPrincipal() {
        return descricaoPrincipal;
    }

    public void setDescricaoPrincipal(String descricaoPrincipal) {
        this.descricaoPrincipal = descricaoPrincipal;
    }

    @Column(name = "DSC_POSTERIOR")
    public String getDescricaoPosterior() {
        return descricaoPosterior;
    }

    public void setDescricaoPosterior(String descricaoPosterior) {
        this.descricaoPosterior = descricaoPosterior;
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

    @Column(name = "NOM_CONTATO")
    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    @Column(name = "DSC_EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "COD_AREA_TELEFONE")
    public String getCodigoAreaTelefone() {
        return codigoAreaTelefone;
    }

    public void setCodigoAreaTelefone(String codigoAreaTelefone) {
        this.codigoAreaTelefone = codigoAreaTelefone;
    }

    @Column(name = "NUM_TELEFONE")
    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }

    @Column(name = "COD_AREA_FAX")
    public String getCodigoAreaFax() {
        return codigoAreaFax;
    }

    public void setCodigoAreaFax(String codigoAreaFax) {
        this.codigoAreaFax = codigoAreaFax;
    }

    @Column(name = "NUM_FAX")
    public String getNumeroFax() {
        return numeroFax;
    }

    public void setNumeroFax(String numeroFax) {
        this.numeroFax = numeroFax;
    }

    @Column(name = "COD_AGRUPADOR")
    public String getAgrupador() {
        return agrupador;
    }

    public void setAgrupador(String agrupador) {
        this.agrupador = agrupador;
    }

    @Column(name = "COD_ORIGEM")
    public Short getCodigoOrigem() {
        return codigoOrigem;
    }

	public void setCodigoOrigem(Short codigoOrigem) {
        this.codigoOrigem = codigoOrigem;
    }

    @Column(name = "USU_INCLUSAO")
    public String getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(String usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	@Column(name = "USU_ALTERACAO")
	public String getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(String usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}
	
	@Column(name = "DAT_INCLUSAO")
	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	@Column(name = "DAT_ALTERACAO")
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	
	@Column(name = "TXT_OBSERVACAO", length = 500)
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}
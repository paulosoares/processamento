package br.gov.stf.estf.entidade.localizacao;

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

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "JUDICIARIO", name = "ENDERECO_DESTINATARIO")
public class EnderecoDestinatario extends ESTFBaseEntity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Destinatario destinatario;
	private Integer cep;
	private String logradouro;
	private String bairro;
	private String uf;
	private String numeroLocalizacao;
	private String complemento;
	private String municipio;
	private Boolean ativo;
	
	@Id
	@Column(name = "SEQ_ENDERECO_DESTINATARIO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_ENDERECO_DESTINATARIO", allocationSize = 1)
	public Long getId() {
		return (Long) id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DESTINATARIO_ORIGEM", nullable = false, insertable = true, updatable = false)
	public Destinatario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Destinatario destinatario) {
		this.destinatario = destinatario;
	}

	@Column(name = "COD_CEP")
	public Integer getCep() {
		return cep;
	}

	public void setCep(Integer cep) {
		this.cep = cep;
	}

	@Column(name = "DSC_LOGRADOURO")
	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	@Column(name = "NOM_BAIRRO")
	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	@Column(name = "SIG_UF")
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	@Column(name = "NUM_LOCALIZACAO")
	public String getNumeroLocalizacao() {
		return numeroLocalizacao;
	}

	public void setNumeroLocalizacao(String numeroLocalizacao) {
		this.numeroLocalizacao = numeroLocalizacao;
	}

	@Column(name = "DSC_COMPLEMENTO")
	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@Column(name = "NOM_MUNICIPIO")
	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	/**
	 * Indica se o registro está ativo ou não.
	 */
	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}


}

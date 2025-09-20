package br.gov.stf.estf.entidade.jurisdicionado;

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

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "JUDICIARIO", name = "ENDERECO_JURISDICIONADO")
public class EnderecoJurisdicionado extends ESTFBaseEntity<Long> {

	private Long id;
	private Jurisdicionado jurisdicionado;
	private String logradouro;
	private String complemento;
	private String bairro;
	private String municipio;
	private String UF;
	private String pais;
	private String cep;
	private Integer tipoMunicipio;
	private String numero;

	@Id
	@Column(name = "SEQ_ENDERECO_JURISDICIONADO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_ENDERECO_JURISDICIONADO", allocationSize = 1)
	public Long getId() {
		return this.id;
	}

	public void setId(Long identifier) {
		this.id = identifier;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_JURISDICIONADO")
	public Jurisdicionado getJurisdicionado() {
		return jurisdicionado;
	}

	public void setJurisdicionado(Jurisdicionado jurisdicionado) {
		this.jurisdicionado = jurisdicionado;
	}

	@Column(name="DSC_LOGRADOURO")
	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	@Column(name="DSC_BAIRRO")
	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	@Column(name="NOM_MUNICIPIO")
	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	@Column(name="SIG_UF")
	public String getUF() {
		return UF;
	}

	public void setUF(String uf) {
		this.UF = uf;
	}

	@Column(name="NOM_PAIS")
	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	@Column(name="COD_CEP")
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@Column(name="TIP_MUNICIPIO")
	public Integer getTipoMunicipio() {
		return tipoMunicipio;
	}

	public void setTipoMunicipio(Integer tipoMunicipio) {
		this.tipoMunicipio = tipoMunicipio;
	}

	@Column(name="DSC_NUM_LOGRADOURO")
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name="DSC_COMPLEMENTO")
	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	
}

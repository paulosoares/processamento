package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;

/**
 * Classe que mapeia a View de Partes.
 * 
 * @author Demetrius.Jube
 */
@Entity
@Table(name = "VW_JURISDICIONADO_INCIDENTE", schema = "JUDICIARIO")
public class Parte extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -7054192654419734664L;
	private Long id;
	private ObjetoIncidente<?> objetoIncidente;
	private String nomeJurisdicionado;
	private Categoria categoria;
	private Long seqJurisdicionado;
	private Integer numeroOrdem;
	private String dscTipoImpressao;
	private Boolean intimacaoPessoal;
	private Boolean cadastroRatificado;
	private String login;
	private Jurisdicionado jurisdicionado;
	

	@Id
	@Column(name = "SEQ_JURISDICIONADO_INCIDENTE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", nullable = false, insertable = false, updatable = false)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@Column(name = "NOM_JURISDICIONADO")
	public String getNomeJurisdicionado() {
		return nomeJurisdicionado;
	}

	public void setNomeJurisdicionado(String nomeJurisdicionado) {
		this.nomeJurisdicionado = nomeJurisdicionado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_CATEGORIA", nullable = false, insertable = false, updatable = false)
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Column(name = "SEQ_JURISDICIONADO")
	public Long getSeqJurisdicionado() {
		return seqJurisdicionado;
	}

	public void setSeqJurisdicionado(Long seqJurisdicionado) {
		this.seqJurisdicionado = seqJurisdicionado;
	}

	@Column(name = "NUM_ORDEM")
	public Integer getNumeroOrdem() {
		return numeroOrdem;
	}

	public void setNumeroOrdem(Integer numeroOrdem) {
		this.numeroOrdem = numeroOrdem;
	}
	
	@Column(name = "DSC_TIPO_IMPRESSAO")
	public String getDscTipoImpressao() {
		return dscTipoImpressao;
	}

	public void setDscTipoImpressao(String dscTipoImpressao) {
		this.dscTipoImpressao = dscTipoImpressao;
	}

	@Column(name = "FLG_INTIMACAO_PESSOAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getIntimacaoPessoal() {
		return intimacaoPessoal;
	}

	public void setIntimacaoPessoal(Boolean intimacaoPessoal) {
		this.intimacaoPessoal = intimacaoPessoal;
	}

	@Column(name = "FLG_CADASTRO_RATIFICADO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getCadastroRatificado() {
		return cadastroRatificado;
	}

	public void setCadastroRatificado(Boolean cadastroRatificado) {
		this.cadastroRatificado = cadastroRatificado;
	}
	
	@Formula("(SELECT MIN(ue.sig_usuario_externo) FROM corp.usuario_externo ue WHERE ue.seq_jurisdicionado = SEQ_JURISDICIONADO AND ROWNUM = 1)")
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_JURISDICIONADO", nullable = false, insertable = false, updatable = false)
	public Jurisdicionado getJurisdicionado() {
		return jurisdicionado;
	}

	public void setJurisdicionado(Jurisdicionado jurisdicionado) {
		this.jurisdicionado = jurisdicionado;
	}

}

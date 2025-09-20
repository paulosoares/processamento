package br.gov.stf.estf.entidade.documento;

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
@Table(name="CAMPO_CUSTOMIZADO_MODELO", schema="JUDICIARIO")
public class TagsLivresUsuario extends ESTFBaseEntity<Long>{

	private static final long serialVersionUID = 7631912412433630483L;

	private Long id;
	private String dscTagLivres;
	private String codigoRotulo;
	private String nomeRotulo;
	private ModeloComunicacao modeloComunicacao;
	private TipoTagsLivresUsuario tipoTagsLivres;
	
	@Id
	@Column(name = "SEQ_CAMPO_CUSTOMIZADO_MODELO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_CAMPO_CUSTOMIZADO_MODELO", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="DSC_CAMPO_CUSTOMIZADO",insertable = true, updatable = true, nullable=false, unique=false)
	public String getDscTagLivres() {
		return dscTagLivres;
	}
	
	public void setDscTagLivres(String dscTagLivres) {
		this.dscTagLivres = dscTagLivres;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SEQ_MODELO_COMUNICACAO", unique = false, insertable = true, updatable = true)
	public ModeloComunicacao getModeloComunicacao() {
		return modeloComunicacao;
	}
	
	public void setModeloComunicacao(ModeloComunicacao modeloComunicacao) {
		this.modeloComunicacao = modeloComunicacao;
	}

	@Column(name="TXT_ROTULO",insertable = true, updatable = true, nullable=false, unique=false)
	public String getCodigoRotulo() {
		return codigoRotulo;
	}
	
	public void setCodigoRotulo(String codigoRotulo) {
		this.codigoRotulo = codigoRotulo;
	}
	
	@Column(name="NOM_ROTULO",insertable = true, updatable = true, nullable=false, unique=false)
	public String getNomeRotulo() {
		return nomeRotulo;
	}
	
	public void setNomeRotulo(String nomeRotulo) {
		this.nomeRotulo = nomeRotulo;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_TIPO_CAMPO_CUSTOMIZADO", unique = false, insertable = true, updatable = true)	
	public TipoTagsLivresUsuario getTipoTagsLivres() {
		return tipoTagsLivres;
	}

	public void setTipoTagsLivres(TipoTagsLivresUsuario tipoTagsLivres) {
		this.tipoTagsLivres = tipoTagsLivres;
	}
	
}

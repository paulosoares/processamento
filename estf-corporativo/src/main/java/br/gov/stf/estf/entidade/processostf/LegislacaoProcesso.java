package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "STF", name = "LEGISLACAO_PROCESSO")
public class LegislacaoProcesso extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = 3404465407660692226L;
	
	private Short numeroOrdemLegislacao;
	private NormaProcesso normaProcesso;
	private Long numeroLegislacao;
	private Short anoLegislacao;
	private String artigo;
	private String inciso;
	private String paragrafo;
	private String alinea;
	private String observacao;
	private String textoLegislacao;
	private ObjetoIncidente<?> objetoIncidente;
	
	@Id
	@Column( name="SEQ_LEGISLACAO_PROCESSO" )	
	public Long getId() {
		return id;
	}

	@Column( name="NUM_ORDEM_LEGISLACAO" )
	public Short getNumeroOrdemLegislacao() {
		return numeroOrdemLegislacao;
	}

	public void setNumeroOrdemLegislacao(Short numeroOrdemLegislacao) {
		this.numeroOrdemLegislacao = numeroOrdemLegislacao;
	}


	@Column( name="NUM_LEGISLACAO" )
	public Long getNumeroLegislacao() {
		return numeroLegislacao;
	}

	public void setNumeroLegislacao(Long numeroLegislacao) {
		this.numeroLegislacao = numeroLegislacao;
	}


	@Column( name="ANO_LEGISLACAO" )
	public Short getAnoLegislacao() {
		return anoLegislacao;
	}

	public void setAnoLegislacao(Short anoLegislacao) {
		this.anoLegislacao = anoLegislacao;
	}


	@Column( name="ARTIGO" )
	public String getArtigo() {
		return artigo;
	}

	public void setArtigo(String artigo) {
		this.artigo = artigo;
	}


	@Column( name="INCISO" )
	public String getInciso() {
		return inciso;
	}

	public void setInciso(String inciso) {
		this.inciso = inciso;
	}


	@Column( name="PARAGRAFO" )
	public String getParagrafo() {
		return paragrafo;
	}

	public void setParagrafo(String paragrafo) {
		this.paragrafo = paragrafo;
	}


	@Column( name="ALINEA" )
	public String getAlinea() {
		return alinea;
	}

	public void setAlinea(String alinea) {
		this.alinea = alinea;
	}


	@Column( name="OBSERVACAO" )
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	@Column( name="TEXTO_LEGISLACAO" )
	public String getTextoLegislacao() {
		return textoLegislacao;
	}

	public void setTextoLegislacao(String textoLegislacao) {
		this.textoLegislacao = textoLegislacao;
	}

	@OneToOne( fetch=FetchType.LAZY )
	@JoinColumn(name="COD_NORMA", referencedColumnName = "COD_NORMA")
	public NormaProcesso getNormaProcesso() {
		return normaProcesso;
	}

	public void setNormaProcesso(NormaProcesso normaProcesso) {
		this.normaProcesso = normaProcesso;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", updatable = false, insertable = false)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

}

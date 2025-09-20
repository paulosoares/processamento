package br.gov.stf.estf.entidade.processostf;

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

/**
 * @author Rodrigo Barreiros
 * @since 23.03.2010
 */
@Entity
@Table(schema="STF", name="PROCESSO_PLENARIO")
public class ProcessoPlenario extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = 6732438558042992026L;
	
	private Long id;
	private SubtemaPauta subtemaPauta;
	private Boolean relevante;
	private Boolean liberado;
	private Boolean publico;
	private Boolean apresentadoMesa;
	private Boolean sustentacaoOral;
	private Integer numeroOrdem;
	private Boolean julgado;
	private Integer numeroOrdemSessao;
	private String espelho;
	private ObjetoIncidente<?> objetoIncidente;
	
	@Id
    @Column(name="SEQ_PROCESSO_PLENARIO") 
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_PROCESSO_PLENARIO", allocationSize=1)
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_SUBTEMA_PAUTA")
	public SubtemaPauta getSubtemaPauta() {
		return subtemaPauta;
	}

	public void setSubtemaPauta(SubtemaPauta subtemaPauta) {
		this.subtemaPauta = subtemaPauta;
	}

	@Column(name = "FLG_RELEVANTE")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.SimNaoType")
	public Boolean getRelevante() {
		return relevante;
	}

	public void setRelevante(Boolean relevante) {
		this.relevante = relevante;
	}

	@Column(name = "FLG_LIBERADO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.SimNaoType")
	public Boolean getLiberado() {
		return liberado;
	}

	public void setLiberado(Boolean liberado) {
		this.liberado = liberado;
	}

	@Column(name = "FLG_PUBLICO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.SimNaoType")
	public Boolean getPublico() {
		return publico;
	}

	public void setPublico(Boolean publico) {
		this.publico = publico;
	}

	@Column(name = "FLG_APRESENTADO_MESA")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.SimNaoType")
	public Boolean getApresentadoMesa() {
		return apresentadoMesa;
	}

	public void setApresentadoMesa(Boolean apresentadoMesa) {
		this.apresentadoMesa = apresentadoMesa;
	}

	@Column(name = "FLG_SUSTENTACAO_ORAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.SimNaoType")
	public Boolean getSustentacaoOral() {
		return sustentacaoOral;
	}

	public void setSustentacaoOral(Boolean sustentacaoOral) {
		this.sustentacaoOral = sustentacaoOral;
	}

	@Column(name = "NUM_ORDEM")
	public Integer getNumeroOrdem() {
		return numeroOrdem;
	}

	public void setNumeroOrdem(Integer numeroOrdem) {
		this.numeroOrdem = numeroOrdem;
	}
	
	@Column(name = "FLG_JULGADO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.SimNaoType")
	public Boolean getJulgado() {
		return julgado;
	}
	
	public void setJulgado(Boolean julgado) {
		this.julgado = julgado;
	}
	
	@Column(name = "NUM_ORDEM_SESSAO")
	public Integer getNumeroOrdemSessao() {
		return numeroOrdemSessao;
	}
	
	public void setNumeroOrdemSessao(Integer numeroOrdemSessao) {
		this.numeroOrdemSessao = numeroOrdemSessao;
	}

	@Column(name = "DSC_TEMA")
	public String getEspelho() {
		return espelho;
	}
	
	public void setEspelho(String espelho) {
		this.espelho = espelho;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}
	
	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

}

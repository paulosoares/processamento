package br.gov.stf.estf.entidade.processostf;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;



@Entity
@Table(name="SUMULA", schema="JUDICIARIO")
@DiscriminatorValue(value = "SU")
public class Sumula extends ObjetoIncidente<Sumula>{

	private static final long serialVersionUID = -2885820304516469929L;

	private Long seqSumula;
	private Long numeroSumula;
	private String verbete;
	private FlagGenericaSumula flgVinculante;
	private List<SumulaIncidente> sumulaIncidente;
	private Date dataAprovacao;
	private String dscCancelamento;
	private String dscObservacao;
	private FlagGenericaSumula flgCancelada;
	private Date dataCancelamento;
	
	
	public Sumula() {
		this.setTipoObjetoIncidente(TipoObjetoIncidente.SUMULA);
	}
	
	
	@Column(name = "SEQ_SUMULA", nullable= false, unique = true)
	public Long getSeqSumula() {
		return seqSumula;
	}


	public void setSeqSumula(Long seqSumula) {
		this.seqSumula = seqSumula;
	}


	@Column ( name="NUM_SUMULA")
	public Long getNumeroSumula() {
		return numeroSumula;
	}
	public void setNumeroSumula(Long numeroSumula) {
		this.numeroSumula = numeroSumula;
	}
	
	
	@Column ( name="DSC_TEXTO_SUMULA")
	public String getVerbete() {
		return verbete;
	}
	public void setVerbete(String verbete) {
		this.verbete = verbete;
	}
	
	
	@Column(name="FLG_VINCULANTE")
	@Enumerated(EnumType.STRING)
	public FlagGenericaSumula getFlgVinculante() {
		return flgVinculante;
	}
	
	public void setFlgVinculante(FlagGenericaSumula flgVinculante) {
		this.flgVinculante = flgVinculante;
	}

	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="sumula")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<SumulaIncidente> getSumulaIncidente() {
		return sumulaIncidente;
	}
	public void setSumulaIncidente(List<SumulaIncidente> sumulaIncidente) {
		this.sumulaIncidente = sumulaIncidente;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_APROVACAO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataAprovacao() {
		return dataAprovacao;
	}

	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}

	@Column(name="DSC_MOTIVO_CANCELAMENTO", insertable= true, nullable= true, updatable= true, unique = false)
	public String getDscCancelamento() {
		return dscCancelamento;
	}

	public void setDscCancelamento(String dscCancelamento) {
		this.dscCancelamento = dscCancelamento;
	}

	@Column(name="DSC_OBSERVACAO", insertable= true, nullable= true, updatable= true, unique = false)
	public String getDscObservacao() {
		return dscObservacao;
	}

	public void setDscObservacao(String dscObservacao) {
		this.dscObservacao = dscObservacao;
	}

	@Column(name="FLG_CANCELADA")
	@Enumerated(EnumType.STRING)
	public FlagGenericaSumula getFlgCancelada() {
		return flgCancelada;
	}

	public void setFlgCancelada(FlagGenericaSumula flgCancelada) {
		this.flgCancelada = flgCancelada;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_CANCELAMENTO", unique = false, nullable = true, insertable = true, updatable = true)	
	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	@Transient
	public List<String> getListaProcessosPrecedentes(){
		List<String> listaPrecedentes = new LinkedList<String>();
		if (getSumulaIncidente() != null && getSumulaIncidente().size() > 0L){
			for (SumulaIncidente si : getSumulaIncidente()){
				listaPrecedentes.add(si.getObjetoIncidente().getIdentificacao());
			}
		}
		return listaPrecedentes;
	}

	@Transient
	public String getIdentificacao() {
		return getNumeroSumula().toString();
	}	
	

	public enum FlagGenericaSumula{
		N("Não"), 
		S("Sim");

		private String descricao;

		FlagGenericaSumula(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}
	
	@Transient
	public Boolean getMateriaConstitucional() {
		return Boolean.FALSE;
	}


	
}

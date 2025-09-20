package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.Ocorrencia;

@Entity
@Table( schema="STF", name="SIT_MIN_PROCESSOS" )
public class SituacaoMinistroProcesso extends ESTFBaseEntity<Long>{

	private static final long serialVersionUID = 2358235722685890259L;
	private Date dataComposicaoParcial;
	private Ministro ministroRelator;
	private Ministro ministroPresidente;
	private Boolean relatorAtual;
	private Boolean relatorIncidenteAtual;
	private ObjetoIncidente<?> objetoIncidente;
	private Ocorrencia ocorrencia;
	private Date dataOcorrencia;

	

	@Id
	@Column(name="SEQ_SIT_MIN_PROCESSOS")
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="STF.SEQ_SIT_MIN_PROCESSOS", allocationSize=1)
	public Long getId() {
		return id;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="DAT_COMP_PARCIAL", insertable=false, updatable=false)
	public Date getDataComposicaoParcial() {
		return dataComposicaoParcial;
	}

	public void setDataComposicaoParcial(Date dataComposicaoParcial) {
		this.dataComposicaoParcial = dataComposicaoParcial;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="COD_MINISTRO_PRESIDENTE", insertable=false, updatable=false)
	public Ministro getMinistroPresidente() {
		return ministroPresidente;
	}

	public void setMinistroPresidente(Ministro ministroPresidente) {
		this.ministroPresidente = ministroPresidente;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO", insertable=true, updatable=true)
	public Ministro getMinistroRelator() {
		return ministroRelator;
	}

	public void setMinistroRelator(Ministro ministroRelator) {
		this.ministroRelator = ministroRelator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", insertable=true, updatable=true)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}
	
	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
	
	@Column(name = "COD_OCORRENCIA", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.ministro.Ocorrencia"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "valueOfMethod", value = "valueOfCodigo")})
	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}

	@Column(name="FLG_RELATOR_ATUAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getRelatorAtual() {
		return relatorAtual;
	}

	public void setRelatorAtual(Boolean relatorAtual) {
		this.relatorAtual = relatorAtual;
	}
	
	@Column(name="FLG_RELATOR_INCIDENTE")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getRelatorIncidenteAtual() {
		return relatorIncidenteAtual;
	}

	public void setRelatorIncidenteAtual(Boolean relatorIncidenteAtual) {
		this.relatorIncidenteAtual = relatorIncidenteAtual;
	}
	
	@Column(name="DAT_OCORRENCIA", unique=false, nullable=false, insertable=true, updatable=true)
	public Date getDataOcorrencia() {
		return dataOcorrencia;
	}
	
	public void setDataOcorrencia(Date dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}

}
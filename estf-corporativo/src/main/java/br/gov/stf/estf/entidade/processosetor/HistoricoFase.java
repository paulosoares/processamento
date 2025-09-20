package br.gov.stf.estf.entidade.processosetor;

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
import javax.persistence.Transient;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.entidade.localizacao.TipoStatusSetor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.util.SearchData;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true) 
@Table(schema="EGAB", name="HISTORICO_FASE")
public class HistoricoFase extends ESTFBaseEntity<Long> {

	private TipoFaseSetor tipoFaseSetor;
	private ProcessoSetor processoSetor;
	private String observacao;
	private Date dataFase;
	private Usuario usuario;
	private TipoStatusSetor tipoStatusSetor;
	private ObjetoIncidente<?> objetoIncidente;

	@Id
	@Column( name="SEQ_HISTORICO_FASE" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_HISTORICO_FASE", allocationSize = 1) 	 
	public Long getId() {
		return id;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TIPO_FASE_SETOR", unique=false, nullable=true, insertable=true, updatable=true)
	public TipoFaseSetor getTipoFaseSetor() {
		return this.tipoFaseSetor;
	}

	public void setTipoFaseSetor(TipoFaseSetor tipoFaseSetor) {
		this.tipoFaseSetor = tipoFaseSetor;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_PROCESSO_SETOR", unique=false, nullable=true, insertable=true, updatable=true)
	public ProcessoSetor getProcessoSetor() {
		return this.processoSetor;
	}

	public void setProcessoSetor(ProcessoSetor processoSetor) {
		this.processoSetor = processoSetor;
	}

	@Column(name="DSC_OBSERVACAO", unique=false, nullable=true, insertable=true, updatable=true, length=1000)
	public String getObservacao() {
		return this.observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_HISTORICO_FASE", unique=false, nullable=true, insertable=true, updatable=true, length=7)
	public Date getDataFase() {
		return this.dataFase;
	}

	public void setDataFase(Date dataFase) {
		this.dataFase = dataFase;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SIG_USUARIO", unique=false, nullable=true, insertable=true, updatable=true)    
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String toString() {
		return getClass().getName();
	}

	public void setTipoStatusSetor(TipoStatusSetor tipoStatusSetor) {
		this.tipoStatusSetor = tipoStatusSetor;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TIPO_STATUS_SETOR", unique=false, nullable=true, insertable=true, updatable=true)
	public TipoStatusSetor getTipoStatusSetor() {
		return tipoStatusSetor;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_OBJETO_INCIDENTE")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}
	
	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
	@Transient
	public String getDescricaoCompletaFase() {
		StringBuffer descricaoTemp = new StringBuffer();
		if( getTipoFaseSetor() != null && SearchData.stringNotEmpty(getTipoFaseSetor().getDescricao()) ) {
			descricaoTemp.append(getTipoFaseSetor().getDescricao());
		}
		
		if( getTipoStatusSetor() != null && SearchData.stringNotEmpty(getTipoStatusSetor().getDescricao()) ) {
			if( descricaoTemp != null && descricaoTemp.toString().trim().length() > 0 )
				descricaoTemp.append(" / ");
			
			descricaoTemp.append(getTipoStatusSetor().getDescricao());
		}
		
		if( SearchData.stringNotEmpty(descricaoTemp.toString()) )
			return descricaoTemp.toString();
		else
			return null;
	}
}

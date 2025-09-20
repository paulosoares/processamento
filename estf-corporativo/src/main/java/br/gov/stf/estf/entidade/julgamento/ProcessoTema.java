package br.gov.stf.estf.entidade.julgamento;

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
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

@Entity
@Table(name="PROCESSO_TEMA",schema="JULGAMENTO")
public class ProcessoTema extends ESTFBaseEntity<Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6047749630055033202L;
	private Long id;
	private ObjetoIncidente<?> objetoIncidente;
	private TipoOcorrencia tipoOcorrencia;
	private Tema tema;
	private Date dataOcorrencia;
	private String siglaClasse;
	private Long numProcesso;
	private String tipoJulgamento;
	

	@Id
	@Column( name="SEQ_PROCESSO_TEMA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_PROCESSO_TEMA", allocationSize=1 )	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TIPO_OCORRENCIA", unique=false, nullable=true, insertable=true, updatable=true)
	public TipoOcorrencia getTipoOcorrencia() {
		return tipoOcorrencia;
	}

	public void setTipoOcorrencia(TipoOcorrencia tipoOcorrencia) {
		this.tipoOcorrencia = tipoOcorrencia;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TEMA", unique=false, nullable=true, insertable=true, updatable=true)
	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="DAT_OCORRENCIA", unique=false, nullable=true, insertable=true, updatable=true)  
	public Date getDataOcorrencia() {
		return dataOcorrencia;
	}

	public void setDataOcorrencia(Date dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", updatable = true, insertable = true)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
    
	public void setSiglaClasse(String siglaClasse) {
		this.siglaClasse = siglaClasse;
	}

	@Column(name = "SIG_CLASSE_PROCES")
	public String getSiglaClasse() {
		return siglaClasse;
	}

	public void setNumProcesso(Long numProcesso) {
		this.numProcesso = numProcesso;
	}

	@Column(name = "NUM_PROCESSO")
	public Long getNumProcesso() {
		return numProcesso;
	}

	@Column(name = "TIPO_JULGAMENTO")
	public String getTipoJulgamento() {
		return tipoJulgamento;
	}

	public void setTipoJulgamento(String tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
	}

	//============================================ MÉTODOS TRANSIENT ============================================
	@Transient
	public IncidenteJulgamento getIncidenteJulgamento(){
		if( objetoIncidente instanceof IncidenteJulgamento ){
			return (IncidenteJulgamento) objetoIncidente;
		}
		return null;
	}
	
	@Transient
	public String getIdentificacao() {
		String texto = getSiglaClasse();
		texto = texto == null ? "" : texto;
		if(getNumProcesso() != null){
			texto = texto.concat(" ").concat(getNumProcesso().toString());
		}		
		if(getTipoOcorrencia() != null && getTipoOcorrencia().getDescricao() != null){
			texto = texto.concat(" (").concat(getTipoOcorrencia().getDescricao()).concat(")");
		}
		return texto;
	}
	
	@Transient
	public String getIdentificacaoSimples() {
		String texto = getSiglaClasse();
		texto = texto == null ? "" : texto;
		if(getNumProcesso() != null){
			texto = texto.concat(" ").concat(getNumProcesso().toString());
		}		
		return texto;
	}
	
}

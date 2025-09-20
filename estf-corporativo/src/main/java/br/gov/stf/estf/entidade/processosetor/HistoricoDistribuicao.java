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

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.GrupoUsuario;
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true) 
@Table(schema="EGAB", name="HISTORICO_DISTRIBUICAO")
public class HistoricoDistribuicao extends ESTFBaseEntity<Long> {


	private GrupoUsuario grupoUsuario;
	private Usuario usuario;
	private ProcessoSetor processoSetor;
	private Date dataDistribuicao;
	private String descricao;
	private ObjetoIncidente<?> objetoIncidente;


	@Id
	@Column( name="SEQ_HISTORICO_DISTRIBUICAO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_HISTORICO_DISTRIBUICAO", allocationSize=1 )		
	public Long getId() {
		return id;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_PROCESSO_SETOR", unique=false, nullable=true, insertable=true, updatable=true)
	public ProcessoSetor getProcessoSetor() {
		return this.processoSetor;
	}

	public void setProcessoSetor(ProcessoSetor processoSetor) {
		this.processoSetor = processoSetor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_DISTRIBUICAO", unique=false, nullable=false, insertable=true, updatable=true, length=7)
	public Date getDataDistribuicao() {
		return this.dataDistribuicao;
	}

	public void setDataDistribuicao(Date dataDistribuicao) {
		this.dataDistribuicao = dataDistribuicao;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SIG_USUARIO_ANALISE", unique=false, nullable=true, insertable=true, updatable=true)    
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_GRUPO_ANALISE", unique=false, nullable=true, insertable=true, updatable=true)
	public GrupoUsuario getGrupoUsuario() {
		return this.grupoUsuario;
	}

	public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
		this.grupoUsuario = grupoUsuario;
	}
	
	@Column(name="DSC_HISTORICO_DISTRIBUICAO", unique=false, nullable=true, insertable=true, updatable=true, length=500)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public String toString() {
		return getClass().getName();
	}	
}
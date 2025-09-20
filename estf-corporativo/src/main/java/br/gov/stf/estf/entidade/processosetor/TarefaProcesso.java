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

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.Tarefa;
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@Table(name="TAREFA_PROCESSO",schema="EGAB")
public class TarefaProcesso extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 317215176792593148L;
	private Tarefa tarefa;
	private Secao secao;
	private ProcessoSetor processoSetor;
	private Date dataTarefaProcesso;
	private String observacaoTarefaProcesso;
	private Usuario usuario;

	@Id
	@Column( name="SEQ_TAREFA_PROCESSO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_TAREFA_PROCESSO", allocationSize = 1 )	
	public Long getId() {
		return id;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TAREFA", unique=false, nullable=false, insertable=true, updatable=true)
	public Tarefa getTarefa() {
		return this.tarefa;
	}
	
	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_SECAO", unique=false, nullable=true, insertable=true, updatable=true)
	public Secao getSecao() {
		return this.secao;
	}
	
	public void setSecao(Secao secao) {
		this.secao = secao;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_PROCESSO_SETOR", unique=false, nullable=true, insertable=true, updatable=true)
	public ProcessoSetor getProcessoSetor() {
		return this.processoSetor;
	}
	
	public void setProcessoSetor(ProcessoSetor processoSetor) {
		this.processoSetor = processoSetor;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="DAT_TAREFA_PROCESSO", unique=false, nullable=false, insertable=true, updatable=true, length=7)
	public Date getDataTarefaProcesso() {
		return this.dataTarefaProcesso;
	}
	
	public void setDataTarefaProcesso(Date dataTarefaProcesso) {
		this.dataTarefaProcesso = dataTarefaProcesso;
	}
	
	@Column(name="OBS_TAREFA_PROCESSO", unique=false, nullable=true, insertable=true, updatable=true, length=240)
	public String getObservacaoTarefaProcesso() {
		return this.observacaoTarefaProcesso;
	}
	
	public void setObservacaoTarefaProcesso(String observacaoTarefaProcesso) {
		this.observacaoTarefaProcesso = observacaoTarefaProcesso;
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
}
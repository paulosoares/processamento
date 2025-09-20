package br.gov.stf.estf.entidade.tarefa;

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
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@Table(schema="EGAB", name="COMPLEMENTO_TAREFA")
public class ComplementoTarefa extends ESTFBaseEntity<Long> {

	private TarefaSetor tarefaSetor;
	private TipoComplementoTarefa tipoComplementoTarefa;
	private String descricao;
	private Usuario usuario;
	private Date dataComplemento;

	@Id
	@Column( name="SEQ_COMPLEMENTO_TAREFA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_COMPLEMENTO_TAREFA", allocationSize = 1 ) 	 
	public Long getId() {
		return id;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TAREFA_SETOR", unique=false, nullable=true, insertable=true, updatable=true)
	public TarefaSetor getTarefaSetor() {
		return this.tarefaSetor;
	}

	public void setTarefaSetor(TarefaSetor tarefaSetor) {
		this.tarefaSetor = tarefaSetor;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TIPO_COMPLEMENTO_TAREFA", unique=false, nullable=true, insertable=true, updatable=true)
	public TipoComplementoTarefa getTipoComplementoTarefa() {
		return this.tipoComplementoTarefa;
	}

	public void setTipoComplementoTarefa(TipoComplementoTarefa tipoComplementoTarefa) {
		this.tipoComplementoTarefa = tipoComplementoTarefa;
	}

	@Column(name="DSC_COMPLEMENTO_TAREFA", unique=false, nullable=true, insertable=true, updatable=true, length=1000)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SIG_USUARIO", unique=false, nullable=true, insertable=true, updatable=true)    
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_COMPLEMENTO_TAREFA", unique=false, nullable=true, insertable=true, updatable=true, length=7)
	public Date getDataComplemento() {
		return this.dataComplemento;
	}

	public void setDataComplemento(Date dataComplemento) {
		this.dataComplemento = dataComplemento;
	}

	
	public String toString() {
		return getClass().getName();
	}
}

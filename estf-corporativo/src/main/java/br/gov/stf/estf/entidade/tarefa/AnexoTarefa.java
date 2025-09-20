package br.gov.stf.estf.entidade.tarefa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@Table(schema="EGAB", name="ANEXO_TAREFA")
public class AnexoTarefa extends ESTFBaseEntity<Long> {

	private TarefaSetor tarefaSetor;
	private byte[] bytes;
	private String nome;
	private String descricao;
	private Usuario usuario;
	private Date dataCriacao;

	@Id
	@Column( name="SEQ_ANEXO_TAREFA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_ANEXO_TAREFA", allocationSize = 1 ) 	 
	public Long getId() {
		return id;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TAREFA_SETOR", unique=false, nullable=false, insertable=true, updatable=true)
	public TarefaSetor getTarefaSetor() {
		return this.tarefaSetor;
	}

	public void setTarefaSetor(TarefaSetor tarefaSetor) {
		this.tarefaSetor = tarefaSetor;
	}
	
	@Lob
	@Column(name = "DOC_ANEXO")
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	@Column(name="DSC_ANEXO", unique=false, nullable=false, insertable=true, updatable=true, length=1000)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_CRIACAO", unique=false, nullable=false, insertable=true, updatable=true, length=7)
	public Date getDataCriacao() {
		return this.dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SIG_USUARIO", unique=false, nullable=false, insertable=true, updatable=true)    
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@Column(name="NOM_ARQUIVO", unique=false, nullable=false, insertable=true, updatable=true, length=1000)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String toString() {
		return getClass().getName();
	}
}

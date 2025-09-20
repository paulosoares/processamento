package br.gov.stf.estf.entidade.localizacao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@Table(schema = "EGAB", name = "SECAO_SETOR")
public class SecaoSetor extends ESTFBaseEntity<Long> {

	private Secao secao;
	private Setor setor;
	private ParametroSecao parametro;
	private Boolean ativo;
	private Set<Usuario> usuarios = new HashSet<Usuario>(0);
	private Set<Tarefa> tarefas = new HashSet<Tarefa>(0); 

	@Id
	@Column(name = "SEQ_SECAO_SETOR")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "EGAB.SEQ_SECAO_SETOR", allocationSize = 1)
	public Long getId() {
		return id;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_SECAO", unique = false, nullable = true, insertable = true, updatable = true)
	public Secao getSecao() {
		return secao;
	}

	public void setSecao(Secao secao) {
		this.secao = secao;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_PARAMETRO", unique = false, nullable = true, insertable = true, updatable = true)
	public ParametroSecao getParametro() {
		return parametro;
	}

	public void setParametro(ParametroSecao parametro) {
		this.parametro = parametro;
	}

	@ManyToMany
	@JoinTable(schema = "EGAB", name = "SECAO_TAREFA", 
			   joinColumns = { @JoinColumn(name = "SEQ_SECAO_SETOR") }, inverseJoinColumns = { @JoinColumn(name = "SEQ_TAREFA") })
	public Set<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(Set<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}
    
	@ManyToMany 
	@JoinTable(schema = "EGAB", name = "USUARIO_SECAO", joinColumns = { @JoinColumn(name = "SEQ_SECAO_SETOR") }, inverseJoinColumns = { @JoinColumn(name = "SIG_USUARIO") })
	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

    @Column(name="FLG_ATIVO", unique=false, insertable=true, updatable=true, length=1)
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}

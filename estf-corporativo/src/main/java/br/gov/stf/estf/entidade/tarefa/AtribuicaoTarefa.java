package br.gov.stf.estf.entidade.tarefa;

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

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.usuario.Usuario;
@Entity
@Table(schema="EGAB", name="TAREFA_USUARIO")
public class AtribuicaoTarefa extends ESTFBaseEntity<Long> {
    private Usuario usuarioAtribuido;
    private TipoAtribuicaoTarefa tipoAtribuicao;
    private TarefaSetor tarefaSetor;
    private Setor setor;
    
    @Id
	@Column( name="SEQ_TAREFA_USUARIO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_TAREFA_USUARIO", allocationSize = 1 ) 	 
	public Long getId() {
		return id;
	}
	
    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SIG_USUARIO", unique=false, nullable=false, insertable=true, updatable=true)
    public Usuario getUsuarioAtribuido() {
		return usuarioAtribuido;
	}
	public void setUsuarioAtribuido(Usuario usuarioAtribuido) {
		this.usuarioAtribuido = usuarioAtribuido;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TIPO_ATRIBUICAO", unique=false, nullable=false, insertable=true, updatable=true)
	public TipoAtribuicaoTarefa getTipoAtribuicao() {
		return tipoAtribuicao;
	}
	public void setTipoAtribuicao(TipoAtribuicaoTarefa tipoAtribuicao) {
		this.tipoAtribuicao = tipoAtribuicao;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TAREFA_SETOR", unique=false, nullable=false, insertable=true, updatable=true)
	public TarefaSetor getTarefaSetor() {
		return tarefaSetor;
	}
	public void setTarefaSetor(TarefaSetor tarefaSetor) {
		this.tarefaSetor = tarefaSetor;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="COD_SETOR", unique=false, nullable=false, insertable=true, updatable=true)
	public Setor getSetor() {
		return setor;
	}
	public void setSetor(Setor setor) {
		this.setor = setor;
	}
}

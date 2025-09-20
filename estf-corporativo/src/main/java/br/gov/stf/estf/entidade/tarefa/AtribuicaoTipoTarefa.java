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
@Table(schema="EGAB", name="ATRIBUICAO_USU_TIPO_TAREFA")
public class AtribuicaoTipoTarefa extends ESTFBaseEntity<Long>{
    
    private Usuario usuarioAtribuido;
    private TipoAtribuicaoTarefa tipoAtribuicao;
    private TipoTarefaSetor tipoTarefaSetor;
    private Setor setor;
    
    @Id
	@Column( name="SEQ_ATRIB_USU_TIPO_TAREFA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_ATRIB_USU_TIPO_TAREFA", allocationSize = 1 ) 	 
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
	@JoinColumn(name="SEQ_TIPO_TAREFA", unique=false, nullable=false, insertable=true, updatable=true)
	public TipoTarefaSetor getTipoTarefaSetor() {
		return tipoTarefaSetor;
	}

	public void setTipoTarefaSetor(TipoTarefaSetor tipoTarefaSetor) {
		this.tipoTarefaSetor = tipoTarefaSetor;
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

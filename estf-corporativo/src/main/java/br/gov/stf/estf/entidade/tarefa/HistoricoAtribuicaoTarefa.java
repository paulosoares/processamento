package br.gov.stf.estf.entidade.tarefa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(schema="EGAB", name="HISTORICO_ATRIBUICAO_TAREFA")
public class HistoricoAtribuicaoTarefa extends ESTFBaseEntity<Long> {

    private Usuario usuarioAtribuido;
    private TipoAtribuicaoTarefa tipoAtribuicao;
	private TarefaSetor tarefaSetor;
    private Date dataDesligamento;
	private Date dataAtribuicao;
    private Usuario usuarioAtribuicao;
	

    @Id
	@Column( name="SEQ_HIST_ATRIBUICAO_TAREFA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_HIST_ATRIBUICAO_TAREFA", allocationSize = 1 ) 	 
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
    
    @Enumerated(value=EnumType.STRING)
    @Column(name="TIP_ATRIBUICAO", nullable=false)    
    public TipoAtribuicaoTarefa getTipoAtribuicao() {
        return tipoAtribuicao;
    }

    public void setTipoAtribuicao(TipoAtribuicaoTarefa tipoAtribuicao) {
        this.tipoAtribuicao = tipoAtribuicao;
    }    

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_ATRIBUICAO", unique=false, nullable=true, insertable=true, updatable=true, length=7)
	public Date getDataAtribuicao() {
		return this.dataAtribuicao;
	}

	public void setDataAtribuicao(Date dataAtribuicao) {
		this.dataAtribuicao = dataAtribuicao;
	}
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DAT_DESLIGAMENTO", unique=false, nullable=true, insertable=true, updatable=true, length=7)
    public Date getDataDesligamento() {
        return this.dataDesligamento;
    }

    public void setDataDesligamento(Date dataDesligamento) {
        this.dataDesligamento = dataDesligamento;
    }    

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SIG_USUARIO_ATRIBUIDO", unique=false, nullable=true, insertable=true, updatable=true)    
	public Usuario getUsuarioAtribuido() {
		return usuarioAtribuido;
	}

	public void setUsuarioAtribuido(Usuario usuarioAtribuido) {
		this.usuarioAtribuido = usuarioAtribuido;
	}
    
    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="SIG_USUARIO_ATRIBUICAO", unique=false, nullable=true, insertable=true, updatable=true)    
    public Usuario getUsuarioAtribuicao() {
        return usuarioAtribuicao;
    }

    public void setUsuarioAtribuicao(Usuario usuarioAtribuicao) {
        this.usuarioAtribuicao = usuarioAtribuicao;
    }    

	public String toString() {
		return getClass().getName();
	}
}

package br.gov.stf.estf.entidade.tarefa;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OrderBy;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Processo;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true, dynamicUpdate=true)	
@Table( schema="EGAB", name="TAREFA_SETOR")
public class TarefaSetor extends ESTFBaseEntity<Long> {

    private TarefaSetor tarefaPai;
    private TipoTarefaSetor tipoTarefa;
    private HistoricoSituacaoTarefa situacaoAtual;
    
    private Setor setorOrigem;
    private Setor setorDestino;
    
    private Date dataCriacao;
    
    private Date dataInicio;
    private Date dataFim;    
    private Date dataPrevistaInicio;    
    private Date dataPrevistaFim;    
        
    private String descricao;    
    private String observacao;
    
    private Boolean sigiloso;
    private Boolean urgente;
    
    private Integer prioridade;    
    
    private TipoSituacaoTarefa tipoSituacaoTarefa;
    
    private List<HistoricoSituacaoTarefa> historicoSituacoes = new LinkedList<HistoricoSituacaoTarefa>();
    private List<HistoricoAtribuicaoTarefa> historicoAtribuicoes = new LinkedList<HistoricoAtribuicaoTarefa>();
    private List<ComplementoTarefa> complementos = new LinkedList<ComplementoTarefa>();
    private List<AnexoTarefa> anexos = new LinkedList<AnexoTarefa>();
    private List<AtribuicaoTarefa> atribuicoesTarefa = new LinkedList<AtribuicaoTarefa>();
    private Set<Processo> processos = new HashSet<Processo>();
    private Set<TarefaSetor> tarefasVinculadas = new HashSet<TarefaSetor>();
    private Set<CampoTarefaValor> camposTarefaValor = new HashSet<CampoTarefaValor>();
    
    
	@Id
	@Column( name="SEQ_TAREFA_SETOR" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_TAREFA_SETOR", allocationSize=1 )	
	public Long getId() {
		return id;
	}	    
    
    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="SEQ_TAREFA_PAI", unique=false, nullable=true, insertable=true, updatable=true)    
    public TarefaSetor getTarefaPai() {
        return tarefaPai;
    }

    public void setTarefaPai(TarefaSetor tarefaPai) {
        this.tarefaPai = tarefaPai;
    }
    
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="SEQ_TIPO_SITUACAO_TAREFA", unique=false, nullable=true, insertable=true, updatable=true) 
	public TipoSituacaoTarefa getTipoSituacaoTarefa() {
		return tipoSituacaoTarefa;
	}

	public void setTipoSituacaoTarefa(TipoSituacaoTarefa tipoSituacaoTarefa) {
		this.tipoSituacaoTarefa = tipoSituacaoTarefa;
	}

    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="SEQ_TIPO_TAREFA", unique=false, nullable=true, insertable=true, updatable=true)    
    public TipoTarefaSetor getTipoTarefa() {
        return tipoTarefa;
    }

    public void setTipoTarefa(TipoTarefaSetor tipoTarefa) {
        this.tipoTarefa = tipoTarefa;
    }

    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="COD_SETOR_ORIGEM", unique=false, nullable=true, insertable=true, updatable=true)	
	public Setor getSetorOrigem() {
		return setorOrigem;
	}

	public void setSetorOrigem(Setor setorOrigem) {
		this.setorOrigem = setorOrigem;
	}	
    
    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="COD_SETOR_DESTINO", unique=false, nullable=true, insertable=true, updatable=true)  
    public Setor getSetorDestino() {
        return setorDestino;
    }

    public void setSetorDestino(Setor setorDestino) {
        this.setorDestino = setorDestino;
    }    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DAT_CRIACAO", unique=false, nullable=true, insertable=true, updatable=true)    
    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }    
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DAT_INICIO", unique=false, nullable=true, insertable=true, updatable=true)	
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DAT_FIM", unique=false, nullable=true, insertable=true, updatable=true)	
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}	
    
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_PREVISTA_INICIO", unique=false, nullable=true, insertable=true, updatable=true)
    public Date getDataPrevistaInicio() {
        return dataPrevistaInicio;
    }

    public void setDataPrevistaInicio(Date dataPrevistaInicio) {
        this.dataPrevistaInicio = dataPrevistaInicio;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DAT_PREVISTA_FIM", unique=false, nullable=true, insertable=true, updatable=true)
    public Date getDataPrevistaFim() {
        return dataPrevistaFim;
    }

    public void setDataPrevistaFim(Date dataPrevistaFim) {
        this.dataPrevistaFim = dataPrevistaFim;
    }    
    
    @Column(name="NUM_PRIORIDADE", unique=false, nullable=true, insertable=true, updatable=true)
    public Integer getPrioridade() {
        return prioridade;
    }	
    
    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }    
    
	@Column(name="DSC_TAREFA_SETOR", unique=false, nullable=true, insertable=true, updatable=true, length=2000)	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}    

    @Column(name="DSC_OBSERVACAO", unique=false, nullable=true, insertable=true, updatable=true, length=4000)
    public String getObservacao() {
        return this.observacao;
    }
    
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SEQ_HISTORICO_SITUACAO_TAREFA", nullable=true)
	public HistoricoSituacaoTarefa getSituacaoAtual() {
		return situacaoAtual;
	}

	public void setSituacaoAtual(HistoricoSituacaoTarefa situacaoAtual) {
		this.situacaoAtual = situacaoAtual;
	}
    
    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="tarefaSetor")
    @org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	@OrderBy(clause = "DAT_HISTORICO_SITUACAO_TAREFA DESC")    
	public List<HistoricoSituacaoTarefa> getHistoricoSituacoes() {
		return historicoSituacoes;
	}

	public void setHistoricoSituacoes(List<HistoricoSituacaoTarefa> historicoSituacoes) {
		this.historicoSituacoes = historicoSituacoes;
	}
    
    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="tarefaSetor")
    @org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
    @OrderBy(clause = "DAT_ATRIBUICAO DESC")    
    public List<HistoricoAtribuicaoTarefa> getHistoricoAtribuicoes() {
        return historicoAtribuicoes;
    }

    public void setHistoricoAtribuicoes(List<HistoricoAtribuicaoTarefa> historicoAtribuicoes) {
        this.historicoAtribuicoes = historicoAtribuicoes;
    }    
    
    @Column(name="FLG_SIGILOSO", unique=false, insertable=true, updatable=true, length=1)
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )    
    public Boolean getSigiloso() {
        return this.sigiloso;
    }
    
    public void setSigiloso(Boolean sigiloso) {
        this.sigiloso = sigiloso;
    }
    
    @Column(name="FLG_URGENTE", unique=false, insertable=true, updatable=true, length=1)
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )    
    public Boolean getUrgente() {
        return this.urgente;
    }
    
    public void setUrgente(Boolean urgente) {
        this.urgente = urgente;
    } 
    
    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="tarefaSetor")
	@org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	@OrderBy(clause = "DAT_COMPLEMENTO_TAREFA DESC")  
	public List<ComplementoTarefa> getComplementos() {
		return complementos;
	}

	public void setComplementos(List<ComplementoTarefa> complementos) {
		this.complementos = complementos;
	}
	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="tarefaSetor")
	@org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	@OrderBy(clause = "DAT_CRIACAO DESC")  
	public List<AnexoTarefa> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<AnexoTarefa> anexos) {
		this.anexos = anexos;
	}
	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="tarefaSetor")
	@org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	public Set<CampoTarefaValor> getCamposTarefaValor() {
		return camposTarefaValor;
	}

	public void setCamposTarefaValor(Set<CampoTarefaValor> camposTarefaValor) {
		this.camposTarefaValor = camposTarefaValor;
	}
	
	@ManyToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(
			schema = "EGAB",
			name = "TAREFA_ATRIBUIDA_PROCESSO",
			joinColumns = {@JoinColumn(name = "SEQ_TAREFA_SETOR")},
			inverseJoinColumns = {@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")}
	)
	@NotFound(action = NotFoundAction.IGNORE)
	public Set<Processo> getProcessos() {
		return processos;
	}

	public void setProcessos(Set<Processo> processos) {
		this.processos = processos;
	}
	
	@ManyToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(
			schema = "EGAB",
			name = "TAREFA_VINCULADA",
			joinColumns = {@JoinColumn(name = "SEQ_TAREFA_SETOR")},
			inverseJoinColumns = {@JoinColumn(name = "SEQ_TAREFA_VINCULADA")}
	)
	public Set<TarefaSetor> getTarefasVinculadas() {
		return tarefasVinculadas;
	}

	public void setTarefasVinculadas(Set<TarefaSetor> tarefasVinculadas) {
		this.tarefasVinculadas = tarefasVinculadas;
	}
	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="tarefaSetor")
	@org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	@OrderBy(clause = "SIG_USUARIO")  
	public List<AtribuicaoTarefa> getAtribuicoesTarefa() {
		return atribuicoesTarefa;
	}

	public void setAtribuicoesTarefa(List<AtribuicaoTarefa> atribuicoesTarefa) {
		this.atribuicoesTarefa = atribuicoesTarefa;
	}

    
    public String toString() {
        return getClass().getName();
    }


	//Utilitários ----------------------------------------------------------------------------------
	/**
	 * método resposnsavel por multiplicar os valores de Gravidade Urgencia e tendencia;
	 * quanto maior o número maior a prioridade do processo.
	 */
	
	public boolean equals(Object obj) {
		if (obj instanceof TarefaSetor == false) {
			return false;
		}
		
		if (this == obj) {
			return true;
		}
		
        TarefaSetor tarefaSetor = (TarefaSetor) obj;
		
		return new EqualsBuilder()
			.appendSuper(super.equals(obj))
			.append(setorOrigem.getId(), tarefaSetor.getSetorOrigem().getId())
			.isEquals();
	}
	
	public int hashCode() {
	     return new HashCodeBuilder(17, 37).
			append(setorOrigem.getId()).
			toHashCode();
	}

	
	// -------- Métodos de negócio
	
	public Boolean adicionarHistoricoSituacao(HistoricoSituacaoTarefa situacao ) {
		Boolean result = Boolean.FALSE;
		
		if( situacao == null )
			throw new NullPointerException("Objeto de situação nulo.");
		
		setSituacaoAtual(situacao);
		setTipoSituacaoTarefa(situacao.getTipoSituacaoTarefa());
		
		
		List<HistoricoSituacaoTarefa> historico = getHistoricoSituacoes();
		assert(historico != null);
		historico.add(0, situacao);
		
		result = Boolean.TRUE;
		
		return result;
	}

	

	

}
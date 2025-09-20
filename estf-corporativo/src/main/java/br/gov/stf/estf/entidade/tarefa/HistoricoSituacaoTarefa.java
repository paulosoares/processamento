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

@Entity
@Table(schema="EGAB", name="HISTORICO_SITUACAO_TAREFA")
public class HistoricoSituacaoTarefa extends ESTFBaseEntity<Long> {

    private TipoSituacaoTarefa tipoSituacaoTarefa;
	private TarefaSetor tarefaSetor;
	private String observacao;
	private Date dataSituacao;
	

	@Id
	@Column( name="SEQ_HISTORICO_SITUACAO_TAREFA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_HISTORICO_SITUACAO_TAREFA", allocationSize = 1) 	 
	public Long getId() {
		return id;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TIPO_SITUACAO_TAREFA", unique=false, nullable=true, insertable=true, updatable=true)
	public TipoSituacaoTarefa getTipoSituacaoTarefa() {
		return this.tipoSituacaoTarefa;
	}

	public void setTipoSituacaoTarefa(TipoSituacaoTarefa tipoSituacaoTarefa) {
		this.tipoSituacaoTarefa = tipoSituacaoTarefa;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TAREFA_SETOR", unique=false, nullable=true, insertable=true, updatable=true)
	public TarefaSetor getTarefaSetor() {
		return this.tarefaSetor;
	}

	public void setTarefaSetor(TarefaSetor tarefaSetor) {
		this.tarefaSetor = tarefaSetor;
	}

	@Column(name="OBS_HISTORICO_SITUACAO_TAREFA", unique=false, nullable=true, insertable=true, updatable=true, length=1000)
	public String getObservacao() {
		return this.observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_HISTORICO_SITUACAO_TAREFA", unique=false, nullable=true, insertable=true, updatable=true, length=7)
	public Date getDataSituacao() {
		return this.dataSituacao;
	}

	public void setDataSituacao(Date dataSituacao) {
		this.dataSituacao = dataSituacao;
	}

	public String toString() {
		return getClass().getName();
	}
}

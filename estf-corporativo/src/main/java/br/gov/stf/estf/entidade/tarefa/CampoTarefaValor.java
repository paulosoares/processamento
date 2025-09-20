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

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

@Entity
@Table(schema="EGAB", name="CAMPO_TAREFA")
public class CampoTarefaValor extends ESTFAuditavelBaseEntity<Long>{

    private String valor;
    private TarefaSetor tarefaSetor;
    private TipoCampoTarefa tipoCampoTarefa;
    
    public enum TipoValorRepercussao{
		S("S","Há"),
		N("N","Não há"),
		I("I","Impedido"),
		SEM_MANIFESTACAO("","Não se manifestou");

		private String descricao;
		private String sigla;
		private TipoValorRepercussao(String sigla,String descricao){
			this.descricao = descricao;
			this.sigla = sigla;
		}

		public String getDescricao(){
			return this.descricao;
		}

		public String getSigla(){
			return this.sigla;
		}
	}
    
    
    @Id
	@Column( name="SEQ_CAMPO_TAREFA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_CAMPO_TAREFA", allocationSize=1 )	
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
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="SEQ_TIPO_CAMPO_TAREFA", unique=false, nullable=false, insertable=true, updatable=true)
	public TipoCampoTarefa getTipoCampoTarefa() {
		return this.tipoCampoTarefa;
	}

	public void setTipoCampoTarefa(TipoCampoTarefa tipoCampoTarefa) {
		this.tipoCampoTarefa = tipoCampoTarefa;
	}
	
	@Column(name="DSC_VALOR_CAMPO", unique=false, nullable=false, insertable=true, updatable=true, length=2000)
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}

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

@Entity
@Table(schema="EGAB", name="TIPO_CAMPO_TAREFA_VALOR")
public class TipoCampoTarefaValor extends ESTFBaseEntity<Long>{

    private String descricao;
    private TipoCampoTarefa tipoCampoTarefa;
    
    @Id
	@Column( name="SEQ_TIPO_CAMPO_TAREFA_VALOR" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_TIPO_CAMPO_TAREFA_VALOR", allocationSize = 1 ) 	 
	public Long getId() {
		return id;
	}
    
    @Column(name="DSC_TIPO_CAMPO_TAREFA_VALOR", unique=false, nullable=true, insertable=true, updatable=true, length=1000)
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TIPO_CAMPO_TAREFA", unique=false, nullable=true, insertable=true, updatable=true)
    public TipoCampoTarefa getTipoCampoTarefa() {
        return tipoCampoTarefa;
    }
    public void setTipoCampoTarefa(TipoCampoTarefa tipoCampoTarefa) {
        this.tipoCampoTarefa = tipoCampoTarefa;
    }
}

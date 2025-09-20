package br.gov.stf.estf.entidade.localizacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="TAREFA" ,schema="EGAB")
public class Tarefa extends ESTFBaseEntity<Long> {

	private String descricao;
	@Id
	@Column( name="SEQ_TAREFA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_TAREFA", allocationSize=1 )	
	public Long getId() {
		return id;
	}	

    @Column(name="DSC_TAREFA", unique=false, nullable=true, insertable=true, updatable=true, length=240)
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
   
}



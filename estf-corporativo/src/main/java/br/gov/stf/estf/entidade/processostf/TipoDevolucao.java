package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="TIPO_DEVOLUCAO", schema="STF")
public class TipoDevolucao extends ESTFBaseEntity<Long>{

	private static final long serialVersionUID = 4638624385069102135L;
	private String descricao;
	
	@Id
	@Column( name="SEQ_TIPO_DEVOLUCAO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="STF.SEQ_TIPO_DEVOLUCAO", allocationSize = 1 )
	public Long getId() {
		return id;
	}

	@Column( name="DSC_TIPO_DEVOLUCAO" )
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}

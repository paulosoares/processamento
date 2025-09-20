package br.gov.stf.estf.entidade.publicacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( name="TIPO_FASE_TEXTO", schema="PUBLICACAO" )
@Deprecated
public class TipoFaseTexto extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7371894942437789476L;
	private String descricao;
	
	
	@Id
	@Column( name="SEQ_TIPO_FASE_TEXTO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_TIPO_FASE_TEXTO", allocationSize=1 )
	public Long getId() {
		return id;
	}

    
    @Column( name="DSC_TIPO_FASE_TEXTO", nullable=true, insertable=true, updatable=true, unique=false )
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


}

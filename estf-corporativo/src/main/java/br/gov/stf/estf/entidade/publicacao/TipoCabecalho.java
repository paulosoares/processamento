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
@Table( name="TIPO_CABECALHO", schema="PUBLICACAO" )
public class TipoCabecalho extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5696937088601813112L;
	private String descricao;
	
	
	@Id
	@Column( name="SEQ_TIPO_CABECALHO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_TIPO_CABECALHO", allocationSize=1 )
	public Long getId() {
		return id;
	}
    
    @Column( name="DSC_TIPO_CABECALHO", unique=false, nullable=true )
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}

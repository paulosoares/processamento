package br.gov.stf.estf.entidade.julgamento;

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
@Table( name="ENVOLVIDO", schema="JULGAMENTO" )
public class Envolvido extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4244290282707353870L;
	private String nome;
	private CategoriaEnvolvido categoriaEnvolvido;
	
	@Id
	@Column( name="SEQ_ENVOLVIDO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_ENVOLVIDO", allocationSize=1 )	
	public Long getId() {
		return id;
	}	
	
	
	
	@Column( name="NOM_ENVOLVIDO", insertable=true, updatable=true, unique=false, nullable=false)
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
	@JoinColumn(name="COD_CATEGORIA_ENVOLVIDO", unique=false, nullable=true, insertable=true, updatable=true )
	public CategoriaEnvolvido getCategoriaEnvolvido() {
		return categoriaEnvolvido;
	}
	public void setCategoriaEnvolvido(CategoriaEnvolvido categoriaEnvolvido) {
		this.categoriaEnvolvido = categoriaEnvolvido;
	}

}

package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( name="CATEGORIA_ENVOLVIDO", schema="JULGAMENTO" )
public class CategoriaEnvolvido extends ESTFBaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1165683376038005209L;
	/*
	public static final String PROCURADOR_GERAL_DA_REPÚBLICA = "1";	
	public static final String SECRETARIO = "2";
	public static final String COORDENADOR = "3";
	public static final String OUTROS = "4";
	public static final String MINISTRO = "5";
	public static final String MINISTRO_SUBSTITUTO = "6";
	public static final String SUB_PROCURADOR_GERAL_DA_REPÚBLICA = "7";
	*/
	public static final String ADVOGADO = "1";

	private String descricao;
	
	@Id
	@Column( name="COD_CATEGORIA_ENVOLVIDO" )	
	public String getId() {
		return id;
	}	
	
	@Column( name="DSC_CATEGORIA_ENVOLVIDO", unique=false, nullable=false, insertable=true, updatable=true )
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


}

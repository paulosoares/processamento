package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
@Entity
@Table( name="TIPO_TEMA",schema="STF" )
public class TipoTema extends ESTFBaseEntity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String descricao;
	
	public enum TipoTemaConstante{
		REPERCUSSAO_GERAL((long)1),
		REPRESENTATIVO_CONTROVERSIA((long)2),
		PRE_REPRESENTATIVO_CONTROVERSIA((long)3);		

		private Long codigo;

		private TipoTemaConstante(Long codigo){
			this.codigo = codigo;
		}

		public Long getCodigo(){
			return this.codigo;
		}

	}
	
	@Id
	@Column( name="COD_TIPO_TEMA" , unique=true, nullable=false, insertable=true, updatable=true )
	public Long getId() {
		return id;
	}
	
	@Column( name="DSC_TIPO_TEMA" , unique=true, nullable=false, insertable=true, updatable=true )
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}

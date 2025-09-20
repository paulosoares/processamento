package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( name="TIPO_EVENTO", schema="JULGAMENTO" )
public class TipoEvento extends ESTFBaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 171506067907788299L;
	private String descricao;
	
	// TODO ALTERAR OS CÓDIGOS
	public enum TipoEventoConstante {
		ELEICAO("Eleição", "1"),
		HOMENAGEM("Homenagem", "2"),
		REGISTRO("Registro", "3"),
		SUMULA("Súmula", "4");
		
		private String descricao;
		private String codigo;
		
		
		private TipoEventoConstante( String descricao, String codigo ) {
			this.descricao = descricao;
			this.codigo = codigo;
		}

		public String getDescricao() {
			return this.descricao;
		}

		public String getCodigo() {
			return codigo;
		}				
	}
	

	@Id
	@Column( name="COD_TIPO_EVENTO" )	
	public String getId() {
		return id;
	}	
	
	
	@Column( name="DSC_TIPO_EVENTO", nullable=false, updatable=true, insertable=true, unique=false )
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}

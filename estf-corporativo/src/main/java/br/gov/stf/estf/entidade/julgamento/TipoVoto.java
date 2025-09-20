package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( name="TIPO_VOTO", schema="JULGAMENTO" )
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class TipoVoto extends ESTFBaseEntity<String> {
	
	private static final long serialVersionUID = -1939371039058539505L;
	private String descricao;
	
	@Id
	@Column( name="COD_TIPO_VOTO" )	
	public String getId() {
		return super.id;
	}	

	@Column( name="DSC_TIPO_VOTO", unique=false, nullable=false, insertable=true, updatable=true )
	public String getDescricao() {
		return this.descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public enum TipoVotoConstante{
		HA(1L,"Há"),
		NAO_HA(2L,"Não Há"),
		IMPEDIDO(3L,"Impedido"),
		SIM(5L,"Sim"),
		NAO(6L,"Não"),
		DIVERGENTE(7L,"Divirjo do relator"),
		ACOMPANHO_DIVERGENCIA(8L,"Acompanho a divergência"),
		ACOMPANHO_RELATOR(9L,"Acompanho o relator"),
		ACOMPANHO_RELATOR_RESSALVA(10L,"Acompanho o relator com ressalvas"),
		SUSPEITO(11L,"Suspeito"),
		NAO_SE_APLICA(12L,"Não se aplica"),
		ACOMPANHO_A_RESSALVA(13L, "Acompanho a ressalva");

		
		private Long id;
		private String descricao;
		
		private TipoVotoConstante(String codigo,String descricao){
			this.id = Long.parseLong(codigo);
			this.descricao = descricao;
		}
		
		private TipoVotoConstante(Long id, String descricao){
			this.id = id;
			this.descricao = descricao;
		}
		
		public String getCodigo(){
			return String.valueOf(id);
		}
		
		public String getDescricao(){
			return descricao;
		}
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}

		public static TipoVotoConstante getById(Long id) {
			for(TipoVotoConstante value : values())
				if (value.id.equals(id))
					return value;
			
			return null;
		}
		
		public static TipoVotoConstante getById(String id) {
			if (id != null)
				return getById(Long.valueOf(id));
			
			return null;
		}
		
	}
}

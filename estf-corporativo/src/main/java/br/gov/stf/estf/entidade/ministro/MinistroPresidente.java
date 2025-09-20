package br.gov.stf.estf.entidade.ministro;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( schema="STF", name="MINISTROS_PRESIDENTES" )
public class MinistroPresidente extends ESTFBaseEntity<MinistroPresidente.MinistroPresidenteId> {

	private static final long serialVersionUID = -6306407654155945117L;
	private Date dataAfastamento;
	private TipoOcorrenciaMinistro tipoOcorrencia;
	
	@Id
	public MinistroPresidenteId getId() {
		return id;
	}	
	
	@Temporal(TemporalType.DATE)
    @Column(name="DAT_AFASTAMENTO", unique=false, nullable=true, insertable=true, updatable=true)	
	public Date getDataAfastamento() {
		return dataAfastamento;
	}
	public void setDataAfastamento(Date dataAfastamento) {
		this.dataAfastamento = dataAfastamento;
	}
	
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.ministro.TipoOcorrenciaMinistro"),
			@Parameter(name = "identifierMethod", value = "getSigla"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "valueOfMethod", value = "getTipoOcorrenciaMinistro") })
    @Column(name="COD_OCORRENCIA", nullable=true)
	public TipoOcorrenciaMinistro getTipoOcorrencia() {
		return tipoOcorrencia;
	}
	public void setTipoOcorrencia(TipoOcorrenciaMinistro tipoOcorrencia) {
		this.tipoOcorrencia = tipoOcorrencia;
	}
	
	@Embeddable
    public static class MinistroPresidenteId implements java.io.Serializable {
        
		private static final long serialVersionUID = 1168918121961507977L;
		private Ministro ministro;
    	private Date dataPosse;   
        
    	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
    	@JoinColumn(name="COD_MINISTRO", nullable=false)   
    	public Ministro getMinistro() {
    		return ministro;
    	}
    	public void setMinistro(Ministro ministro) {
    		this.ministro = ministro;
    	}
        
    	@Temporal(TemporalType.TIMESTAMP)
        @Column(name="DAT_POSSE")	
    	public Date getDataPosse() {
    		return dataPosse;
    	}
    	public void setDataPosse(Date dataPosse) {
    		this.dataPosse = dataPosse;
    	}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((dataPosse == null) ? 0 : dataPosse.hashCode());
			result = prime * result
					+ ((ministro == null) ? 0 : ministro.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MinistroPresidenteId other = (MinistroPresidenteId) obj;
			if (dataPosse == null) {
				if (other.dataPosse != null)
					return false;
			} else if (!dataPosse.equals(other.dataPosse))
				return false;
			if (ministro == null) {
				if (other.ministro != null)
					return false;
			} else if (!ministro.equals(other.ministro))
				return false;
			return true;
		}    	    

    }
}

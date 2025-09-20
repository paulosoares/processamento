package br.gov.stf.estf.entidade.ministro;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( schema="SRH2", name="VW_AFASTAMENTO_MINISTRO" )
public class AfastamentoMinistroView extends ESTFBaseEntity<AfastamentoMinistroView.AfastamentoMinistroViewId> {
	
	
	private static final long serialVersionUID = 6481360647532061700L;
	
	private String matricula;
	private Date dataInicio;
	private Date dataFim;
	

	@Id
	public AfastamentoMinistroViewId getId() {
		return id;
	}

	
	@Column(name="MAT_SERVIDOR",insertable=false,updatable=false)
	public String getMatricula() {
		return matricula;
	}
	
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_INI_AFASTA",insertable=false,updatable=false)
	public Date getDataInicio() {
		return dataInicio;
	}
	
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_FIM_AFASTA",insertable=false,updatable=false)
	public Date getDataFim() {
		return dataFim;
	}
	
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	@Embeddable
	public static class AfastamentoMinistroViewId implements java.io.Serializable {

		private static final long serialVersionUID = 1L;
		private String matricula;
		private Date dataInicio;
		private Date dataFim;
		
		@Column(name="MAT_SERVIDOR")
		public String getMatricula() {
			return matricula;
		}
		
		public void setMatricula(String matricula) {
			this.matricula = matricula;
		}
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="DT_INI_AFASTA")
		public Date getDataInicio() {
			return dataInicio;
		}
		
		public void setDataInicio(Date dataInicio) {
			this.dataInicio = dataInicio;
		}
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="DT_FIM_AFASTA")
		public Date getDataFim() {
			return dataFim;
		}
		
		public void setDataFim(Date dataFim) {
			this.dataFim = dataFim;
		}
		
		
		public boolean equals(Object obj) {
			if (obj instanceof AfastamentoMinistroViewId == false) {
				return false;
			}
			
			if (this == obj) {
				return true;
			}
			
			AfastamentoMinistroViewId id = (AfastamentoMinistroViewId) obj;
			
			return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(getMatricula() , id.getMatricula())
				.append(getDataInicio() , id.getDataInicio())
				.append(getDataFim() , id.getDataFim())
				.isEquals();
		}
		
		public int hashCode() {
		     return new HashCodeBuilder(17, 37)
		     	.append(getMatricula())
				.append(getDataInicio())
				.append(getDataFim())
		       .toHashCode();
		}
	}

	

}


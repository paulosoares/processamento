package br.gov.stf.estf.entidade.processostf;

import java.io.Serializable;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "STF", name = "ACORDAO_AGENDADO")
public class AcordaoAgendado extends ESTFBaseEntity<AcordaoAgendado.AcordaoAgendadoId> {

	private static final long serialVersionUID = 4123693069371967658L;

	private Boolean publico;
	private Date composicaoDj;
	private ObjetoIncidente<?> objetoIncidente;
	
	
	@Id
	public AcordaoAgendadoId getId() {
		return id;
	}

	@Column(name = "FLG_PUBLICO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPublico() {
		return publico;
	}

	public void setPublico(Boolean publico) {
		this.publico = publico;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_COMPOSICAO_DJ", length = 7)
	public Date getComposicaoDj() {
		return composicaoDj;
	}

	public void setComposicaoDj(Date composicaoDj) {
		this.composicaoDj = composicaoDj;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_OBJETO_INCIDENTE", insertable=false, updatable=false)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}
	
	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	

	@Embeddable
	public static class AcordaoAgendadoId implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8161766787051036702L;
		private Date horaSistema;
		private Long objetoIncidente;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((horaSistema == null) ? 0 : horaSistema.hashCode());
			result = prime
					* result
					+ ((objetoIncidente == null) ? 0 : objetoIncidente
							.hashCode());
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
			AcordaoAgendadoId other = (AcordaoAgendadoId) obj;
			if (horaSistema == null) {
				if (other.horaSistema != null)
					return false;
			} else if (!horaSistema.equals(other.horaSistema))
				return false;
			if (objetoIncidente == null) {
				if (other.objetoIncidente != null)
					return false;
			} else if (!objetoIncidente.equals(other.objetoIncidente))
				return false;
			return true;
		}
		
		@Column(name="SEQ_OBJETO_INCIDENTE")
		public Long getObjetoIncidente() {
			return objetoIncidente;
		}

		public void setObjetoIncidente(Long objetoIncidente) {
			this.objetoIncidente = objetoIncidente;
		}

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "DAT_HORA_SISTEMA", length = 7)
		public Date getHoraSistema() {
			return horaSistema;
		}

		public void setHoraSistema(Date horaSistema) {
			this.horaSistema = horaSistema;
		}

	}

	@Transient
	public String getDescricaoProcesso() {
		return this.getObjetoIncidente().getIdentificacaoCompleta();

	}

}

package br.gov.stf.estf.entidade.processostf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;


@Entity
@Table(name="AUTORIZA_ANDAMENTOS", schema="STF")
public class AutorizaAndamento extends ESTFBaseEntity<AutorizaAndamento.AutorizaAndamentoId> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5350213945472841734L;

	private Andamento tipoAndamento;

	@Id
	public AutorizaAndamentoId getId() {
		return id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_ANDAMENTO", nullable = false, insertable = false, updatable = false)
	public Andamento getTipoAndamento() {
		return tipoAndamento;
	}

	public void setTipoAndamento(Andamento tipoAndamento) {
		this.tipoAndamento = tipoAndamento;
	}
	
	@Embeddable
	public static class AutorizaAndamentoId implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7594934945655288998L;
		private Long codigoSetor;
		private Long codigoAndamento;
		private String tipoObjeto;
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((codigoAndamento == null) ? 0 : codigoAndamento
							.hashCode());
			result = prime * result
					+ ((codigoSetor == null) ? 0 : codigoSetor.hashCode());
			result = prime * result
					+ ((tipoObjeto == null) ? 0 : tipoObjeto.hashCode());
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
			final AutorizaAndamentoId other = (AutorizaAndamentoId) obj;
			if (codigoAndamento == null) {
				if (other.codigoAndamento != null)
					return false;
			} else if (!codigoAndamento.equals(other.codigoAndamento))
				return false;
			if (codigoSetor == null) {
				if (other.codigoSetor != null)
					return false;
			} else if (!codigoSetor.equals(other.codigoSetor))
				return false;
			if (tipoObjeto == null) {
				if (other.tipoObjeto != null)
					return false;
			} else if (!tipoObjeto.equals(other.tipoObjeto))
				return false;
			return true;
		}
		@Column(name="COD_SETOR")
		public Long getCodigoSetor() {
			return codigoSetor;
		}
		public void setCodigoSetor(Long codigoSetor) {
			this.codigoSetor = codigoSetor;
		}
		
		@Column(name="COD_ANDAMENTO")
		public Long getCodigoAndamento() {
			return codigoAndamento;
		}
		public void setCodigoAndamento(Long codigoAndamento) {
			this.codigoAndamento = codigoAndamento;
		}
		
		@Column(name="TIP_OBJETO_ANDAMENTO")
		public String getTipoObjeto() {
			return tipoObjeto;
		}
		public void setTipoObjeto(String tipoObjeto) {
			this.tipoObjeto = tipoObjeto;
		}
		
		
		
	}

	

	
}

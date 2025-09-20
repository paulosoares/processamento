/**
 * 
 */
package br.gov.stf.estf.entidade.documento;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.documento.RotuloObjetoIncidente.RotuloObjetoIncidenteId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

/**
 * @author Paulo.Estevao
 * @since 28.11.2013
 */
@Entity
@Table(name = "ROTULO_OBJETO_INCIDENTE", schema = "JUDICIARIO")
public class RotuloObjetoIncidente extends ESTFAuditavelBaseEntity<RotuloObjetoIncidenteId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5395902823471460104L;
	private RotuloObjetoIncidenteId id;
	
	@Id
	@Override
	public RotuloObjetoIncidenteId getId() {
		return id;
	}
	
	public void setId(RotuloObjetoIncidenteId id) {
		this.id = id;
	}
	
	@Embeddable
	public static class RotuloObjetoIncidenteId implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8507045548263822025L;
		
		private Rotulo rotulo;
		private ObjetoIncidente<?> objetoIncidente;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "SEQ_ADENDO_TEXTUAL")
		public Rotulo getRotulo() {
			return rotulo;
		}
		
		public void setRotulo(Rotulo rotulo) {
			this.rotulo = rotulo;
		}
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
		@LazyToOne(LazyToOneOption.NO_PROXY)
		public ObjetoIncidente<?> getObjetoIncidente() {
			return objetoIncidente;
		}
		
		public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
			this.objetoIncidente = objetoIncidente;
		}
		
	}

}

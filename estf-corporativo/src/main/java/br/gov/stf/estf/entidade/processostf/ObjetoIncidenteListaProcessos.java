/**
 * 
 */
package br.gov.stf.estf.entidade.processostf;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidenteListaProcessos.ObjetoIncidenteListaProcessosId;

/**
 * @author Paulo.Estevao
 * @since 25.05.2011
 */
@Entity
@Table(schema = "EGAB", name = "PROCESSO_SETOR_GRUPO")
public class ObjetoIncidenteListaProcessos extends ESTFBaseEntity<ObjetoIncidenteListaProcessosId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2436895944808370102L;
	
	private ObjetoIncidenteListaProcessosId id;

	@Id
	@Override
	public ObjetoIncidenteListaProcessosId getId() {
		return this.id;
	}
	
	public void setId(ObjetoIncidenteListaProcessosId id) {
		this.id = id;
	}
	
	@Embeddable
	public static class ObjetoIncidenteListaProcessosId implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5567203194355645715L;
		
		private ObjetoIncidente<?> objetoIncidente;
		private ListaProcessos listaProcessos;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
		public ObjetoIncidente<?> getObjetoIncidente() {
			return objetoIncidente;
		}
		
		public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
			this.objetoIncidente = objetoIncidente;
		}
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "SEQ_GRUPO_PROCESSO_SETOR")
		public ListaProcessos getListaProcessos() {
			return listaProcessos;
		}
		
		public void setListaProcessos(ListaProcessos listaProcessos) {
			this.listaProcessos = listaProcessos;
		}
	}
}

/**
 * 
 */
package br.gov.stf.estf.entidade.julgamento;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.julgamento.PrevisaoImpedimentoMinistro.PrevisaoImpedimentoMinistroId;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

/**
 * @author Paulo.Estevao
 * @since 29.06.2011
 */
@Entity
@Table(name="PREVISAO_IMPEDIMENTO_MINISTRO", schema="JULGAMENTO")
public class PrevisaoImpedimentoMinistro extends ESTFBaseEntity<PrevisaoImpedimentoMinistroId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4293099914923637443L;
	
	private PrevisaoImpedimentoMinistroId id;
	
	@Override
	@Id
	public PrevisaoImpedimentoMinistroId getId() {
		return id;
	}
	
	public void setId(PrevisaoImpedimentoMinistroId id) {
		this.id = id;
	}

	@Embeddable
	public static class PrevisaoImpedimentoMinistroId implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1389217802663921214L;
		
		private Ministro ministro;
		private ObjetoIncidente<?> objetoIncidente;
		
		@ManyToOne
		@JoinColumn(name = "COD_MINISTRO")
		public Ministro getMinistro() {
			return ministro;
		}
		
		public void setMinistro(Ministro ministro) {
			this.ministro = ministro;
		}
		
		@ManyToOne
		@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
		public ObjetoIncidente<?> getObjetoIncidente() {
			return objetoIncidente;
		}
		
		public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
			this.objetoIncidente = objetoIncidente;
		}		
		
	}

}

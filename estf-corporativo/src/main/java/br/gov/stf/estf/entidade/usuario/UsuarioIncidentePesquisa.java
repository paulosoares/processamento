package br.gov.stf.estf.entidade.usuario;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.usuario.UsuarioIncidentePesquisa.UsuarioIncidentePesquisaId;

@Entity
@Table(schema = "EGAB", name = "USUARIO_INCIDENTE_PESQUISA")
public class UsuarioIncidentePesquisa extends ESTFBaseEntity<UsuarioIncidentePesquisaId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4616171260375599934L;

	@Override
	@Id
	public UsuarioIncidentePesquisaId getId() {
		return id;
	}
	
	public void setId(UsuarioIncidentePesquisaId id) {
		this.id = id;
	}
	
	@Embeddable
	public static class UsuarioIncidentePesquisaId implements Serializable {


		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2952582704424038190L;
		private Long seqObjetoIncidente;
		private String sigUsuario;
		
		@Column(name = "SEQ_OBJETO_INCIDENTE")
		public Long getSeqObjetoIncidente() {
			return seqObjetoIncidente;
		}
		
		public void setSeqObjetoIncidente(Long seqObjetoIncidente) {
			this.seqObjetoIncidente = seqObjetoIncidente;
		}
		
		@Column(name = "SIG_USUARIO_PESQUISA")
		public String getSigUsuario() {
			return sigUsuario;
		}
		
		public void setSigUsuario(String sigUsuario) {
			this.sigUsuario = sigUsuario;
		}

		
		

		
	}

}

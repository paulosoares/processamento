/**
 * 
 */
package br.gov.stf.estf.entidade.tesauro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.tesauro.TermoRelacao.TermoRelacaoId;

/**
 * @author Paulo.Estevao
 * @since 16.01.2013
 */
@Entity
@Table(schema = "TESAURO", name = "TERMO_RELACAO")
public class TermoRelacao extends ESTFAuditavelBaseEntity<TermoRelacaoId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8441237205605698768L;
	
	public static Long NUMERO_CATEGORIA_USE = 10L;
	
	private TermoRelacaoId id;
	
	@Id
	public TermoRelacaoId getId() {
		return id;
	}
	
	public void setId(TermoRelacaoId id) {
		this.id = id;
	}
	
	@Embeddable
	public static class TermoRelacaoId implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4359441720515773999L;
		
		private Termo termo;
		private Termo termoRelacionado;
		private Long categoria;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "NUM_TERMO")
		public Termo getTermo() {
			return termo;
		}
		
		public void setTermo(Termo termo) {
			this.termo = termo;
		}
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "NUM_TERMO_RELACIONADO")
		public Termo getTermoRelacionado() {
			return termoRelacionado;
		}
		
		public void setTermoRelacionado(Termo termoRelacionado) {
			this.termoRelacionado = termoRelacionado;
		}
		
		@Column(name = "NUM_CATEGORIA")
		public Long getCategoria() {
			return categoria;
		}
		
		public void setCategoria(Long categoria) {
			this.categoria = categoria;
		}
	}
}

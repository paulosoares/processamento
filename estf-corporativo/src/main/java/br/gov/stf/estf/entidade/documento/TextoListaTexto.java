package br.gov.stf.estf.entidade.documento;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

/**
 * Classe que faz o relacionamento n:n entre as entidades Texto e ListaTextos.
 * Criada para permitir a consulta via HQL das listas a que um texto está
 * vinculado.
 * 
 * @author Demetrius.Jube
 * @since 08/01/2010
 */
@Entity
@Table(schema = "DOC", name = "TEXTO_LISTA_TEXTO")
public class TextoListaTexto extends ESTFAuditavelBaseEntity<TextoListaTexto.TextoListaTextoId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -803403711537059697L;
	private TextoListaTextoId id;

	@Override
	@Id
	public TextoListaTextoId getId() {
		return this.id;
	}
	
	public void setId(TextoListaTextoId textoListaTextoId) {
		this.id = textoListaTextoId;
	}

	@Embeddable
	public static class TextoListaTextoId implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -502374401086735071L;

		private ListaTextos listaTexto;
		private Texto texto;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "SEQ_LISTA_TEXTO", nullable = false)
		public ListaTextos getListaTexto() {
			return listaTexto;
		}

		public void setListaTexto(ListaTextos listaDeTexto) {
			this.listaTexto = listaDeTexto;
		}

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "SEQ_TEXTOS", nullable = false)
		public Texto getTexto() {
			return texto;
		}

		public void setTexto(Texto texto) {
			this.texto = texto;
		}

		public boolean equals(Object obj) {
			if (obj instanceof TextoListaTextoId == false) {
				return false;
			}

			if (this == obj) {
				return true;
			}

			TextoListaTextoId id = (TextoListaTextoId) obj;

			return new EqualsBuilder().append(getListaTexto(), id.getListaTexto()).append(getTexto(), id.getTexto()).isEquals();
		}

		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(getListaTexto()).append(getTexto()).toHashCode();
		}

	}

}

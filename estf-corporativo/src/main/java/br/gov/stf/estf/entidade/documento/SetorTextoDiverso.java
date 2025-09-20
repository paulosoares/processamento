package br.gov.stf.estf.entidade.documento;

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
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(name = "SETOR_TEXTO_DIVERSO", schema = "STF")
public class SetorTextoDiverso extends ESTFAuditavelBaseEntity<SetorTextoDiverso.SetorTextoDiversoId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7987406760551275404L;
	private TextoDiverso textoDiverso;
	private Setor setor;

	@Id
	public SetorTextoDiversoId getId() {
		return this.id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TEXTO_DIVERSO", unique = false, nullable = false, insertable = false, updatable = false)
	public TextoDiverso getTextoDiverso() {
		return this.textoDiverso;
	}

	public void setTextoDiverso(TextoDiverso textoDiverso) {
		this.textoDiverso = textoDiverso;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = false, insertable = false, updatable = false)
	public Setor getSetor() {
		return this.setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	
	@Embeddable
	public static class SetorTextoDiversoId implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2087823993515536891L;
		private Integer codigoSetor;
		private Long textoDiverso;

		@Column(name = "COD_SETOR", unique = false, nullable = false, insertable = true, updatable = true, precision = 9, scale = 0)
		public Integer getCodigoSetor() {
			return this.codigoSetor;
		}

		public void setCodigoSetor(Integer codigoSetor) {
			this.codigoSetor = codigoSetor;
		}

		@Column(name = "SEQ_TEXTO_DIVERSO", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
		public Long getTextoDiverso() {
			return this.textoDiverso;
		}

		public void setTextoDiverso(Long textoDiverso) {
			this.textoDiverso = textoDiverso;
		}

		public String toString() {
			return getClass().getName();
		}

		public boolean equals(Object other) {
			if ((this == other))
				return true;
			if ((other == null))
				return false;
			if (!(other instanceof SetorTextoDiversoId))
				return false;
			SetorTextoDiversoId castOther = (SetorTextoDiversoId) other;

			return ((this.getCodigoSetor() == castOther.getCodigoSetor()) || (this
					.getCodigoSetor() != null
					&& castOther.getCodigoSetor() != null && this
					.getCodigoSetor().equals(castOther.getCodigoSetor())))
					&& ((this.getTextoDiverso() == castOther.getTextoDiverso()) || (this
							.getTextoDiverso() != null
							&& castOther.getTextoDiverso() != null && this
							.getTextoDiverso().equals(
									castOther.getTextoDiverso())));
		}

		public int hashCode() {
			int result = 17;

			result = 37
					* result
					+ (getCodigoSetor() == null ? 0 : this.getCodigoSetor()
							.hashCode());
			result = 37
					* result
					+ (getTextoDiverso() == null ? 0 : this.getTextoDiverso()
							.hashCode());
			return result;
		}

	}

}

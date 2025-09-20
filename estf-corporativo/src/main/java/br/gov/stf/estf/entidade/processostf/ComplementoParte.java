package br.gov.stf.estf.entidade.processostf;

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
@Table(name = "COMPLEMENTO_PARTES", schema = "STF")
public class ComplementoParte extends ESTFBaseEntity<ComplementoParte.ComplementoParteId> {

	private static final long serialVersionUID = 2671032970925519873L;
	private Parte parte;
	private String nome;

	@Id
	public ComplementoParteId getId() {
		return id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_PARTE", referencedColumnName = "SEQ_JURISDICIONADO", nullable = false, insertable = false, updatable = false)
	public Parte getParte() {
		return this.parte;
	}

	public void setParte(Parte parte) {
		this.parte = parte;
	}

	@Column(name = "NOM_COMPLEMENTO", length = 4000)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nomComplemento) {
		this.nome = nomComplemento;
	}

	@Embeddable
	public static class ComplementoParteId implements java.io.Serializable {

		private static final long serialVersionUID = 4127363124802636108L;
		private Long codigoParte;
		private Short tipoComplemento;

		public ComplementoParteId() {
		}

		public ComplementoParteId(Long codParte, short tipComplemento) {
			this.codigoParte = codParte;
			this.tipoComplemento = tipComplemento;
		}

		@Column(name = "COD_PARTE", nullable = false, precision = 8, scale = 0)
		public Long getCodigoParte() {
			return this.codigoParte;
		}

		public void setCodigoParte(Long codParte) {
			this.codigoParte = codParte;
		}

		@Column(name = "TIP_COMPLEMENTO", nullable = false, precision = 1, scale = 0)
		public Short getTipoComplemento() {
			return this.tipoComplemento;
		}

		public void setTipoComplemento(Short tipComplemento) {
			this.tipoComplemento = tipComplemento;
		}

		public boolean equals(Object other) {
			if ((this == other))
				return true;
			if ((other == null))
				return false;
			if (!(other instanceof ComplementoParteId))
				return false;
			ComplementoParteId castOther = (ComplementoParteId) other;

			return (this.getCodigoParte() == castOther.getCodigoParte())
					&& (this.getTipoComplemento() == castOther.getTipoComplemento());
		}

		public int hashCode() {
			int result = 17;

			result = (int) (37 * result + this.getCodigoParte());
			result = 37 * result + this.getTipoComplemento();
			return result;
		}

	}

}

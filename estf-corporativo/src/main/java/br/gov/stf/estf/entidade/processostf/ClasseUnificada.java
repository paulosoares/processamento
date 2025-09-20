package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "CLASSE_UNIF", schema = "STF")
public class ClasseUnificada extends ESTFBaseEntity<ClasseUnificada.ClasseUnificadaId> {

	private static final long serialVersionUID = 1769534174098480032L;
	private String siglaClasseUnificada;
	private String descricaoClasseUnificada;
	private String complementoClasse;
	private Integer codigoClasseNovo;
	private Short codigoClasse;

	@Id
	public ClasseUnificadaId getId() {
		return this.id;
	}

	@Column(name = "SIG_CLASSE_UNIF", length = 56)
	public String getSiglaClasseUnificada() {
		return this.siglaClasseUnificada;
	}

	public void setSiglaClasseUnificada(String sigClasseUnif) {
		this.siglaClasseUnificada = sigClasseUnif;
	}

	@Column(name = "DSC_CLASSE_UNIF", length = 200)
	public String getDescricaoClasseUnificada() {
		return this.descricaoClasseUnificada;
	}

	public void setDescricaoClasseUnificada(String dscClasseUnif) {
		this.descricaoClasseUnificada = dscClasseUnif;
	}

	@Column(name = "CMP_CLASSE", length = 50)
	public String getComplementoClasse() {
		return this.complementoClasse;
	}

	public void setComplementoClasse(String cmpClasse) {
		this.complementoClasse = cmpClasse;
	}

	@Column(name = "COD_CLASSE_NOVO", precision = 5, scale = 0)
	public Integer getCodigoClasseNovo() {
		return this.codigoClasseNovo;
	}

	public void setCodigoClasseNovo(Integer codClasseNovo) {
		this.codigoClasseNovo = codClasseNovo;
	}
	
	@Column(name = "COD_CLASSE", nullable = false, precision = 4, scale = 0)
	public Short getCodigoClasse() {
		return this.codigoClasse;
	}

	public void setCodigoClasse(Short codClasse) {
		this.codigoClasse = codClasse;
	}
	
	@Embeddable
	public static class ClasseUnificadaId implements java.io.Serializable {
	
		private static final long serialVersionUID = 3223234469706891800L;
		private String sigla;
		private String tipoJulgamento;
		private Long codigoRecurso;		
	
		@Column(name = "SIG_CLASSE", nullable = false, length = 6)
		public String getSigla() {
			return this.sigla;
		}
	
		public void setSigla(String sigClasse) {
			this.sigla = sigClasse;
		}
	
		@Column(name = "TIP_JULGAMENTO", nullable = false, length = 6)
		public String getTipoJulgamento() {
			return this.tipoJulgamento;
		}
	
		public void setTipoJulgamento(String tipJulgamento) {
			this.tipoJulgamento = tipJulgamento;
		}
	
		@Column(name = "COD_RECURSO", nullable = false, precision = 4, scale = 0)
		public Long getCodigoRecurso() {
			return this.codigoRecurso;
		}
	
		public void setCodigoRecurso(Long codRecurso) {
			this.codigoRecurso = codRecurso;
		}
	
		public boolean equals(Object other) {
			if ((this == other))
				return true;
			if ((other == null))
				return false;
			if (!(other instanceof ClasseUnificadaId))
				return false;
			ClasseUnificadaId castOther = (ClasseUnificadaId) other;
	
			return ((this.getSigla() == castOther.getSigla()) || (this
					.getSigla() != null
					&& castOther.getSigla() != null && this.getSigla()
					.equals(castOther.getSigla())))
					&& ((this.getTipoJulgamento() == castOther.getTipoJulgamento()) || (this
							.getTipoJulgamento() != null
							&& castOther.getTipoJulgamento() != null && this
							.getTipoJulgamento()
							.equals(castOther.getTipoJulgamento())))
					&& (this.getCodigoRecurso() == castOther.getCodigoRecurso());
					//&& (this.getCodigoClasse() == castOther.getCodigoClasse());
		}
	
		public int hashCode() {
			int result = 17;
	
			result = 37 * result
					+ (getSigla() == null ? 0 : this.getSigla().hashCode());
			result = 37
					* result
					+ (getTipoJulgamento() == null ? 0 : this.getTipoJulgamento()
							.hashCode());
			result = 37 * result + this.getCodigoRecurso().intValue();
			//result = 37 * result + this.getCodigoClasse();
			return result;
		}
	}
}
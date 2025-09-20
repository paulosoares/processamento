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
import br.gov.stf.estf.entidade.localizacao.Origem;

@Entity
@Table(name="INTEGRACAO_ORGAO_PARTE", schema="ESTF")
public class IntegracaoOrgaoParte extends ESTFBaseEntity<IntegracaoOrgaoParte.IntegracaoOrgaoParteId>{

	@Id
	public IntegracaoOrgaoParteId getId() {
		return id;
	}
	
	private static final long serialVersionUID = 4418541167493011321L;

	@Embeddable
	public static class IntegracaoOrgaoParteId implements Serializable {

		private static final long serialVersionUID = 7464500396586751326L;
		
		private Origem origem;
		private Parte parte;
		
		@ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "COD_ORIGEM", nullable = false)
		public Origem getOrigem() {
			return origem;
		}
		
		public void setOrigem(Origem origem) {
			this.origem = origem;
		}
		
		@ManyToOne(fetch = FetchType.LAZY)
//	    @JoinColumn(name = "COD_PARTE", referencedColumnName = "SEQ_JURISDICIONADO", nullable = false)
	    @JoinColumn(name = "COD_PARTE", nullable = false)
		public Parte getParte() {
			return parte;
		}
		public void setParte(Parte parte) {
			this.parte = parte;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((origem == null) ? 0 : origem.hashCode());
			result = prime * result + ((parte == null) ? 0 : parte.hashCode());
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
			final IntegracaoOrgaoParteId other = (IntegracaoOrgaoParteId) obj;
			if (origem == null) {
				if (other.origem != null)
					return false;
			} else if (!origem.equals(other.origem))
				return false;
			if (parte == null) {
				if (other.parte != null)
					return false;
			} else if (!parte.equals(other.parte))
				return false;
			return true;
		}
		
		
		
	}

	
}

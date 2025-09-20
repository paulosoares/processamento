package br.gov.stf.estf.entidade.processostf;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

@Entity
@Table(name = "DESLOCA_PETICAOS", schema = "STF")
public class DeslocaPeticao extends
		ESTFAuditavelBaseEntity<DeslocaPeticao.DeslocaPeticaoId> {

	private static final long serialVersionUID = 3090910853435605699L;
	private Long codigoOrgaoDestino;
	private Date dataRecebimento;
	private Integer numeroSequencia;
	private Guia guia;
	private Long numeroPeticao;
	private Long anoPeticao;
	private Boolean ultimoDeslocamento;

	@Id
	public DeslocaPeticaoId getId() {
		return this.id;
	}


	@Column(name = "COD_ORGAO_DESTINO", unique = false, nullable = true, insertable = true, updatable = true, precision = 9, scale = 0)
	public Long getCodigoOrgaoDestino() {
		return this.codigoOrgaoDestino;
	}

	public void setCodigoOrgaoDestino(Long codigoOrgaoDestino) {
		this.codigoOrgaoDestino = codigoOrgaoDestino;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_RECEBIMENTO", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getDataRecebimento() {
		return this.dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	@Column(name = "NUM_SEQUENCIA", unique = false, nullable = true, insertable = true, updatable = true, precision = 4, scale = 0)
	public Integer getNumeroSequencia() {
		return this.numeroSequencia;
	}

	public void setNumeroSequencia(Integer numeroSequencia) {
		this.numeroSequencia = numeroSequencia;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns( {
			@JoinColumn(name = "COD_ORGAO_ORIGEM", referencedColumnName = "COD_ORGAO_ORIGEM", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "NUM_GUIA", referencedColumnName = "NUM_GUIA", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "ANO_GUIA", referencedColumnName = "ANO_GUIA", nullable = false, insertable = false, updatable = false) })
	public Guia getGuia() {
		return guia;
	}
	
	public void setGuia(Guia guia) {
		this.guia = guia;
	}
	
	@Column(name="NUM_PETICAO")
	public Long getNumeroPeticao() {
		return numeroPeticao;
	}


	public void setNumeroPeticao(Long numeroPeticao) {
		this.numeroPeticao = numeroPeticao;
	}

	@Column(name="ANO_PETICAO")
	public Long getAnoPeticao() {
		return anoPeticao;
	}


	public void setAnoPeticao(Long anoPeticao) {
		this.anoPeticao = anoPeticao;
	}

	@Column(name = "FLG_ULTIMO_DESLOCAMENTO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getUltimoDeslocamento() {
		return ultimoDeslocamento;
	}

	public void setUltimoDeslocamento(Boolean ultimoDeslocamento) {
		this.ultimoDeslocamento = ultimoDeslocamento;
	}



	@Embeddable
	public static class DeslocaPeticaoId implements Serializable {

		private static final long serialVersionUID = 6360659628875357532L;
		private Long numeroGuia;
		private Short anoGuia;
		private Peticao peticao;
		private Long codigoOrgaoOrigem;


		@Column(name = "NUM_GUIA", unique = false, nullable = false, insertable = true, updatable = true, precision = 6, scale = 0)
		public Long getNumeroGuia() {
			return this.numeroGuia;
		}

		public void setNumeroGuia(Long numeroGuia) {
			this.numeroGuia = numeroGuia;
		}

		@Column(name = "ANO_GUIA", unique = false, nullable = false, insertable = true, updatable = true, precision = 4, scale = 0)
		public Short getAnoGuia() {
			return this.anoGuia;
		}

		public void setAnoGuia(Short anoGuia) {
			this.anoGuia = anoGuia;
		}
		
		@ManyToOne (fetch = FetchType.LAZY)
		@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", insertable = false, updatable = false)
		public Peticao getPeticao() {
			return peticao;
		}
	
		public void setPeticao(Peticao peticao) {
			this.peticao = peticao;
		}

		@Column(name = "COD_ORGAO_ORIGEM", unique = false, nullable = false, insertable = true, updatable = true, precision = 9, scale = 0)
		public Long getCodigoOrgaoOrigem() {
			return this.codigoOrgaoOrigem;
		}

		public void setCodigoOrgaoOrigem(Long codigoOrgaoOrigem) {
			this.codigoOrgaoOrigem = codigoOrgaoOrigem;
		}
		
		public String toString() {
			return getClass().getName();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((anoGuia == null) ? 0 : anoGuia.hashCode());
			result = prime
					* result
					+ ((codigoOrgaoOrigem == null) ? 0 : codigoOrgaoOrigem
							.hashCode());
			result = prime * result
					+ ((numeroGuia == null) ? 0 : numeroGuia.hashCode());
			result = prime * result
					+ ((peticao == null) ? 0 : peticao.hashCode());
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
			DeslocaPeticaoId other = (DeslocaPeticaoId) obj;
			if (anoGuia == null) {
				if (other.anoGuia != null)
					return false;
			} else if (!anoGuia.equals(other.anoGuia))
				return false;
			if (codigoOrgaoOrigem == null) {
				if (other.codigoOrgaoOrigem != null)
					return false;
			} else if (!codigoOrgaoOrigem.equals(other.codigoOrgaoOrigem))
				return false;
			if (numeroGuia == null) {
				if (other.numeroGuia != null)
					return false;
			} else if (!numeroGuia.equals(other.numeroGuia))
				return false;
			if (peticao == null) {
				if (other.peticao != null)
					return false;
			} else if (!peticao.equals(other.peticao))
				return false;
			return true;
		}



	}

}

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

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

@Entity
@Table(name = "DESLOCA_PROTOCOLOS", schema = "STF")
public class DeslocaProtocolo extends ESTFAuditavelBaseEntity<DeslocaProtocolo.DeslocaProtocoloId> {

	private static final long serialVersionUID = 3090910853435605699L;
	private Long codigoOrgaoDestino;
	private Integer numeroSequencia;
	private Date dataRecebimento;

	@Id
	public DeslocaProtocoloId getId() {
		return this.id;
	}


	@Column(name = "COD_ORGAO_DESTINO", unique = false, nullable = true, insertable = true, updatable = true, precision = 9, scale = 0)
	public Long getCodigoOrgaoDestino() {
		return this.codigoOrgaoDestino;
	}

	public void setCodigoOrgaoDestino(Long codigoOrgaoDestino) {
		this.codigoOrgaoDestino = codigoOrgaoDestino;
	}

	@Temporal(TemporalType.DATE)
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

	@Embeddable
	public static class DeslocaProtocoloId implements Serializable {

		private static final long serialVersionUID = 6360659628875357532L;
		private Guia guia;
		private Protocolo protocolo;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumns({
			@JoinColumn(name = "ANO_GUIA", referencedColumnName = "ANO_GUIA"), 
			@JoinColumn(name = "NUM_GUIA", referencedColumnName = "NUM_GUIA"),
			@JoinColumn(name = "COD_ORGAO_ORIGEM", referencedColumnName = "COD_ORGAO_ORIGEM")
			})
		public Guia getGuia() {
			return guia;
		}
		
		public void setGuia(Guia guia) {
			this.guia = guia;
		}
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", insertable = false, updatable = false) 
		public Protocolo getProtocolo() {
			return protocolo;
		}
		
		public void setProtocolo(Protocolo protocolo) {
			this.protocolo = protocolo;
		}

		public String toString() {
			return getClass().getName();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((guia == null) ? 0 : guia.hashCode());
			result = prime * result
					+ ((protocolo == null) ? 0 : protocolo.hashCode());
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
			DeslocaProtocoloId other = (DeslocaProtocoloId) obj;
			if (guia == null) {
				if (other.guia != null)
					return false;
			} else if (!guia.equals(other.guia))
				return false;
			if (protocolo == null) {
				if (other.protocolo != null)
					return false;
			} else if (!protocolo.equals(other.protocolo))
				return false;
			return true;
		}

		
	}

}

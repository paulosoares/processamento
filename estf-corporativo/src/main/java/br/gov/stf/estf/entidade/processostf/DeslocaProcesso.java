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
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

@Entity
@Table(name = "DESLOCA_PROCESSOS", schema = "STF")
public class DeslocaProcesso extends ESTFAuditavelBaseEntity<DeslocaProcesso.DeslocaProcessoId> {

	private static final long serialVersionUID = 3090910853435605699L;

	public static final String TIPO_DESLOCAMENTO_EL = "EL";
	
	
	/* Essas constantes retornaram ao código, pois o Decisão as utiliza. */
	public static final String TIPO_DESLOCAMENTO_AP = "AP";
	
	public static final Integer TIPO_ORGAO_ADVOGADO = 1;
	public static final Integer TIPO_ORGAO_INTERNO = 2;
	public static final Integer TIPO_ORGAO_EXTERNO = 3;

	private Long codigoOrgaoDestino;
	private Date dataRecebimento;
	private Short quantidadeVolumes;
	private Short quantidadeApensos;
	private Short quantidadeJuntadaLinha;
	private Integer numeroSequencia;
	private String tipoDeslocamento;
	private Date dataRemessa;
	private Boolean ultimoDeslocamento;
	private Guia guia;
	private Long numeroProcesso;
	private String classeProcesso;
	private AndamentoProcesso andamentoProcesso;
	private Long seqDeslocaProcesso;
	private String nomeMinistroRelatorAtual;
	
	
	@Transient
	public String getNomeMinistroRelatorAtual() {
		return nomeMinistroRelatorAtual;
	}
	
	@Transient
	public void setNomeMinistroRelatorAtual(String nomeMinistroRelatorAtual) {
		this.nomeMinistroRelatorAtual = nomeMinistroRelatorAtual;
	}
	
	@Id
	public DeslocaProcessoId getId() {
		return this.id;
	}

	public void setId(DeslocaProcessoId id) {
		this.id = id;
	}

	@Column(name = "COD_ORGAO_DESTINO")
	public Long getCodigoOrgaoDestino() {
		return this.codigoOrgaoDestino;
	}

	public void setCodigoOrgaoDestino(Long codigoOrgaoDestino) {
		this.codigoOrgaoDestino = codigoOrgaoDestino;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_RECEBIMENTO")
	public Date getDataRecebimento() {
		return this.dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	@Column(name = "QTD_VOLUMES")
	public Short getQuantidadeVolumes() {
		return this.quantidadeVolumes;
	}

	public void setQuantidadeVolumes(Short quantidadeVolumes) {
		this.quantidadeVolumes = quantidadeVolumes;
	}

	@Column(name = "QTD_APENSOS")
	public Short getQuantidadeApensos() {
		return this.quantidadeApensos;
	}

	public void setQuantidadeApensos(Short quantidadeApensos) {
		this.quantidadeApensos = quantidadeApensos;
	}

	@Column(name = "QTD_JUNTADA_LINHA")
	public Short getQuantidadeJuntadaLinha() {
		return this.quantidadeJuntadaLinha;
	}

	public void setQuantidadeJuntadaLinha(Short quantidadeJuntadaLinha) {
		this.quantidadeJuntadaLinha = quantidadeJuntadaLinha;
	}

	@Column(name = "NUM_SEQUENCIA")
	public Integer getNumeroSequencia() {
		return this.numeroSequencia;
	}

	public void setNumeroSequencia(Integer numeroSequencia) {
		this.numeroSequencia = numeroSequencia;
	}

	@Column(name = "TIP_DESLOCAMENTO")
	public String getTipoDeslocamento() {
		return this.tipoDeslocamento;
	}

	public void setTipoDeslocamento(String tipoDeslocamento) {
		this.tipoDeslocamento = tipoDeslocamento;
	}

	@Formula("(SELECT g.dat_remessa FROM stf.guias g"
			+ " WHERE g.num_guia = num_guia AND g.ano_guia = ano_guia AND g.cod_orgao_origem = cod_orgao_origem)")
	public Date getDataRemessa() {
		return dataRemessa;
	}

	public void setDataRemessa(Date dataRemessa) {
		this.dataRemessa = dataRemessa;
	}

	@Column(name = "FLG_ULTIMO_DESLOCAMENTO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getUltimoDeslocamento() {
		return ultimoDeslocamento;
	}

	public void setUltimoDeslocamento(Boolean ultimoDeslocamento) {
		this.ultimoDeslocamento = ultimoDeslocamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns( {
			@JoinColumn(name = "COD_ORGAO_ORIGEM", referencedColumnName = "COD_ORGAO_ORIGEM", insertable = false, updatable = false),
			@JoinColumn(name = "NUM_GUIA", referencedColumnName = "NUM_GUIA", insertable = false, updatable = false),
			@JoinColumn(name = "ANO_GUIA", referencedColumnName = "ANO_GUIA", insertable = false, updatable = false) })
	public Guia getGuia() {
		return guia;
	}
	
	public void setGuia(Guia guia) {
		this.guia = guia;
	}
	
	@Column(name="NUM_PROCESSO")
	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	@Column(name="SIG_CLASSE_PROCES")
	public String getClasseProcesso() {
		return classeProcesso;
	}

	public void setClasseProcesso(String classeProcesso) {
		this.classeProcesso = classeProcesso;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ANDAMENTO_PROCESSO")
	public AndamentoProcesso getAndamentoProcesso() {
		return andamentoProcesso;
	}

	public void setAndamentoProcesso(AndamentoProcesso andamentoProcesso) {
		this.andamentoProcesso = andamentoProcesso;
	}

	@Column(name="SEQ_DESLOCA_PROCESSOS")
	public Long getSeqDeslocaProcesso() {
		return this.seqDeslocaProcesso;
	}

	public void setSeqDeslocaProcesso(Long seqDeslocaProcesso) {
		this.seqDeslocaProcesso = seqDeslocaProcesso;
	}


	@Embeddable
	public static class DeslocaProcessoId implements Serializable {

		private static final long serialVersionUID = 6360659628875357532L;
		private Long numeroGuia;
		private Short anoGuia;
		private Long codigoOrgaoOrigem;
		private Processo processo;

		@Column(name = "NUM_GUIA")
		public Long getNumeroGuia() {
			return this.numeroGuia;
		}

		public void setNumeroGuia(Long numeroGuia) {
			this.numeroGuia = numeroGuia;
		}

		@Column(name = "ANO_GUIA")
		public Short getAnoGuia() {
			return this.anoGuia;
		}

		public void setAnoGuia(Short anoGuia) {
			this.anoGuia = anoGuia;
		}

		@Column(name = "COD_ORGAO_ORIGEM")
		public Long getCodigoOrgaoOrigem() {
			return this.codigoOrgaoOrigem;
		}

		public void setCodigoOrgaoOrigem(Long codigoOrgaoOrigem) {
			this.codigoOrgaoOrigem = codigoOrgaoOrigem;
		}

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
		public Processo getProcesso() {
			return processo;
		}

		public void setProcesso(Processo processo) {
			this.processo = processo;
		}

		public String toString() {
			return getClass().getName();
		}

		public boolean equals(Object other) {
			if ((this == other))
				return true;
			if ((other == null))
				return false;
			if (!(other instanceof DeslocaProcessoId))
				return false;
			DeslocaProcessoId castOther = (DeslocaProcessoId) other;

			return ((this.getNumeroGuia() == castOther.getNumeroGuia()) || (this.getNumeroGuia() != null
					&& castOther.getNumeroGuia() != null && this.getNumeroGuia().equals(castOther.getNumeroGuia())))
					&& ((this.getAnoGuia() == castOther.getAnoGuia()) || (this.getAnoGuia() != null
							&& castOther.getAnoGuia() != null && this.getAnoGuia().equals(castOther.getAnoGuia())))
					&& ((this.getCodigoOrgaoOrigem() == castOther.getCodigoOrgaoOrigem()) || (this.getCodigoOrgaoOrigem() != null
							&& castOther.getCodigoOrgaoOrigem() != null && this.getCodigoOrgaoOrigem().equals(
							castOther.getCodigoOrgaoOrigem())))
					&& ((this.getProcesso() == castOther.getProcesso()) || (this.getProcesso() != null
							&& castOther.getProcesso() != null && this.getProcesso().equals(castOther.getProcesso())));
		}

		public int hashCode() {
			int result = 17;

			result = 37 * result + (getNumeroGuia() == null ? 0 : this.getNumeroGuia().hashCode());
			result = 37 * result + (getAnoGuia() == null ? 0 : this.getAnoGuia().hashCode());
			result = 37 * result + (getCodigoOrgaoOrigem() == null ? 0 : this.getCodigoOrgaoOrigem().hashCode());
			result = 37 * result + (getProcesso() == null ? 0 : this.getProcesso().hashCode());
			return result;
		}

	}

}

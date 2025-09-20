package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "PROCESSO_DEPENDENCIA", schema = "STF")
public class ProcessoDependencia extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 3876563813330551509L;
	private String classeProcesso;
	private Long numeroProcesso;
	private Long tipoDependenciaProcesso;
	private Long andamentoProcesso;
	private Long andamentoPeticao;
	private Long andamentoProcessoDesvinculador;
	private Long andamentoPeticaoDesvinculador;
	private String classeProcessoVinculador;
	private Long numeroProcessoVinculador;
	private Long numeroProtocoloVinculador;
	private Long anoProtocoloVinculador;
	private Date dataFimDependencia;
	private Date dataInicioDependencia;
	private Long idObjetoIncidente;
	private Long idObjetoIncidenteVinculado;

	public enum TipoProcessoDependenciaEnum {
		SOBRESTADO(1L, "Sobrestado"), REAUTUADO(2L, "Reautuado"), DESAPENSADO_DO_PROCESSO_NO(3L, "Desapensado do Processo nº"), AUTOS_EM_RESTAURACAO(4L,
				"Autos em Restauração"), APENSADO_AO_PROCESSO_NO(5L, "Apensado ao Processo nº"), DETERMINADA_A_DEVOLUCAO(6L, "Determinada a devolução"), DETERMINADA_A_DEVOLUCAO_ART543B(
				7L, "Determinada a devolução, art. 543-B do CPC"), ANEXADO_O_PROTOCOLO_NO(8L, "Anexado o Protocolo nº"), AGRAVO_DE_INSTRUMENTO_APENSADO_AO_RE_NO(
				9L, "Agravo de Instrumento apensado ao RE nº"), JULGAMENTO_REPERCUSSAO_GERAL_SUBSTITUIDO_PELO_PROCESSO_NO(10L,
				"Julgamento repercussão geral substituído pelo processo nº"), MERITO_DA_REPERCUSSAO_GERAL_JULGADO_NO_PROCESSO_NO(11L,
				"Mérito da repercussão geral julgado no processo nº"), JUNTADA(12L, "Juntada"), DETERMINADA_A_DEVOLUCAO_PELA_REPERCUSSAO_GERAL (14L, "Determinada a devolução pelo regime da repercussão geral");

		private Long codigo;
		private String descricao;

		private TipoProcessoDependenciaEnum(Long codigo, String descricao) {
			this.codigo = codigo;
			this.setDescricao(descricao);
		}

		public Long getCodigo() {
			return this.codigo;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

	}

	@Id
	@Column(name = "SEQ_PROCESSO_DEPENDENCIA")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_PROCESSO_DEPENDENCIA", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setClasseProcesso(String classeProcesso) {
		this.classeProcesso = classeProcesso;
	}

	@Column(name = "SIG_CLASSE_PROCES")
	public String getClasseProcesso() {
		return classeProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	@Column(name = "NUM_PROCESSO")
	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setTipoDependenciaProcesso(Long tipoDependenciaProcesso) {
		this.tipoDependenciaProcesso = tipoDependenciaProcesso;
	}

	@Column(name = "COD_TIPO_DEPENDENCIA_PROCESSO")
	public Long getTipoDependenciaProcesso() {
		return tipoDependenciaProcesso;
	}

	public void setAndamentoProcesso(Long andamentoProcesso) {
		this.andamentoProcesso = andamentoProcesso;
	}

	@Column(name = "SEQ_ANDAMENTO_PROCESSO")
	public Long getAndamentoProcesso() {
		return andamentoProcesso;
	}

	public void setAndamentoPeticao(Long andamentoPeticao) {
		this.andamentoPeticao = andamentoPeticao;
	}

	@Column(name = "SEQ_ANDAMENTO_PETICAO")
	public Long getAndamentoPeticao() {
		return andamentoPeticao;
	}

	public void setAndamentoProcessoDesvinculador(Long andamentoProcessoDesvinculador) {
		this.andamentoProcessoDesvinculador = andamentoProcessoDesvinculador;
	}

	@Column(name = "SEQ_ANDAMENTO_DESVINCULADOR")
	public Long getAndamentoProcessoDesvinculador() {
		return andamentoProcessoDesvinculador;
	}

	public void setAndamentoPeticaoDesvinculador(Long andamentoPeticaoDesvinculador) {
		this.andamentoPeticaoDesvinculador = andamentoPeticaoDesvinculador;
	}

	@Column(name = "SEQ_ANDAMENTO_DESV_PETICAO")
	public Long getAndamentoPeticaoDesvinculador() {
		return andamentoPeticaoDesvinculador;
	}

	public void setClasseProcessoVinculador(String classeProcessoVinculador) {
		this.classeProcessoVinculador = classeProcessoVinculador;
	}

	@Column(name = "SIG_CLASSE_VINCULADOR")
	public String getClasseProcessoVinculador() {
		return classeProcessoVinculador;
	}

	public void setNumeroProcessoVinculador(Long numeroProcessoVinculador) {
		this.numeroProcessoVinculador = numeroProcessoVinculador;
	}

	@Column(name = "NUM_PROCESSO_VINCULADOR")
	public Long getNumeroProcessoVinculador() {
		return numeroProcessoVinculador;
	}

	public void setNumeroProtocoloVinculador(Long numeroProtocoloVinculador) {
		this.numeroProtocoloVinculador = numeroProtocoloVinculador;
	}

	@Column(name = "NUM_PROTOCOLO_VINCULADOR")
	public Long getNumeroProtocoloVinculador() {
		return numeroProtocoloVinculador;
	}

	public void setAnoProtocoloVinculador(Long anoProtocoloVinculador) {
		this.anoProtocoloVinculador = anoProtocoloVinculador;
	}

	@Column(name = "ANO_PROTOCOLO_VINCULADOR")
	public Long getAnoProtocoloVinculador() {
		return anoProtocoloVinculador;
	}

	public void setDataFimDependencia(Date dataFimDependencia) {
		this.dataFimDependencia = dataFimDependencia;
	}

	@Column(name = "DAT_FIM_DEPENDENCIA")
	public Date getDataFimDependencia() {
		return dataFimDependencia;
	}

	public void setDataInicioDependencia(Date dataInicioDependencia) {
		this.dataInicioDependencia = dataInicioDependencia;
	}

	@Column(name = "DAT_INICIO_DEPENDENCIA")
	public Date getDataInicioDependencia() {
		return dataInicioDependencia;
	}

	public void setIdObjetoIncidente(Long idObjetoIncidente) {
		this.idObjetoIncidente = idObjetoIncidente;
	}

	@Column(name = "SEQ_OBJETO_INCIDENTE")
	public Long getIdObjetoIncidente() {
		return idObjetoIncidente;
	}

	public void setIdObjetoIncidenteVinculado(Long idObjetoIncidenteVinculado) {
		this.idObjetoIncidenteVinculado = idObjetoIncidenteVinculado;
	}

	@Column(name = "SEQ_OBJETO_INCIDENTE_VINC")
	public Long getIdObjetoIncidenteVinculado() {
		return idObjetoIncidenteVinculado;
	}
}

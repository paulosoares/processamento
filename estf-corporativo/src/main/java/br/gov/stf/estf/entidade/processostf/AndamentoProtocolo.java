package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(name = "ANDAMENTO_PROTOCOLO", schema = "STF")
public class AndamentoProtocolo extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8588915834438684031L;

	@Id
	@Column(name = "SEQ_ANDAMENTO_PROTOCOLO", insertable = false, updatable = false)
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_ANDAMENTO_PROTOCOLO", allocationSize = 1)
	public Long getId() {
		return id;
	}

	// TODO criado o objeto: TipoAndamento
	private Long codigoAndamento;
	private Andamento tipoAndamento;
	private Date dataAndamento;
	private Protocolo protocolo;
	private Date dataHoraSistema;
	private String descricaoObservacaoAndamento;
	private Long numeroSequencia;
	private String siglaUsuario;
	private Setor setor;
	private Boolean valido;
	private String observacaoInterna;
	private Long numeroSequenciaErrado;

	@Column(name = "COD_ANDAMENTO")
	public Long getCodigoAndamento() {
		return codigoAndamento;
	}

	public void setCodigoAndamento(Long codigoAnamento) {
		this.codigoAndamento = codigoAnamento;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_ANDAMENTO", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getDataAndamento() {
		return dataAndamento;
	}

	public void setDataAndamento(Date dataAndamento) {
		this.dataAndamento = dataAndamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public Protocolo getProtocolo() {
		return this.protocolo;
	}

	public void setProtocolo(Protocolo protocolo) {
		this.protocolo = protocolo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_HORA_SISTEMA", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getDataHoraSistema() {
		return this.dataHoraSistema;
	}

	public void setDataHoraSistema(Date dataHoraSistema) {
		this.dataHoraSistema = dataHoraSistema;
	}

	@Column(name = "DSC_OBSER_AND", unique = false, nullable = true, insertable = true, updatable = true, length = 4000)
	public String getDescricaoObservacaoAndamento() {
		return this.descricaoObservacaoAndamento;
	}

	public void setDescricaoObservacaoAndamento(String descricaoObserAnd) {
		this.descricaoObservacaoAndamento = descricaoObserAnd;
	}

	@Column(name = "NUM_SEQUENCIA")
	public Long getNumeroSequencia() {
		return numeroSequencia;
	}

	public void setNumeroSequencia(Long numeroSequencia) {
		this.numeroSequencia = numeroSequencia;
	}

	@Column(name = "SIG_USUARIO")
	public String getSiglaUsuario() {
		return siglaUsuario;
	}

	public void setSiglaUsuario(String siglaUsuario) {
		this.siglaUsuario = siglaUsuario;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Setor getSetor() {
		return this.setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Column(name = "FLG_VALIDO", unique = false, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getValido() {
		return valido;
	}

	public void setValido(Boolean valido) {
		this.valido = valido;
	}

	@Column(name = "NUM_SEQ_ERRADO")
	public Long getNumeroSequenciaErrado() {
		return numeroSequenciaErrado;
	}

	public void setNumeroSequenciaErrado(Long numeroSequenciaErrado) {
		this.numeroSequenciaErrado = numeroSequenciaErrado;
	}

	@Column(name = "DSC_OBS_INTERNA")
	public String getObservacaoInterna() {
		return observacaoInterna;
	}

	public void setObservacaoInterna(String observacaoInterna) {
		this.observacaoInterna = observacaoInterna;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_ANDAMENTO", insertable = false, updatable = false)
	public Andamento getTipoAndamento() {
		return tipoAndamento;
	}

	public void setTipoAndamento(Andamento tipoAndamento) {
		this.tipoAndamento = tipoAndamento;
	}

}

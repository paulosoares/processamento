package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

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
import javax.persistence.Transient;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(name = "ANDAMENTO_PETICAO", schema = "STF")
public class AndamentoPeticao extends ESTFBaseEntity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8743842053361112923L;
	private Long id;
	private Long numeroPeticao;
	private Short anoPeticao;
	//private Long codigoAndamento;
	private Andamento tipoAndamento;
	private Date dataAndamento;
	private ObjetoIncidente<?> objetoIncidente;
	private Date dataHoraSistema;
	private String descricaoObservacaoAndamento;
	private Long numeroSequencia;
	private Setor setor;
	private String codigoUsuario;
	private String descricaoObservacaoInterna;
	private Long numeroSequenciaErrado;
	private Boolean ultimoAndamento;
	private Boolean valido;

	@Id
	@Column(name = "SEQ_ANDAMENTO_PETICAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_ANDAMENTO_PETICAO", allocationSize = 1)
	public Long getId() {
		return this.id;
	}
	public void setId(Long id){
		this.id = id;
	}
	
	@Column(name = "NUM_PETICAO")
	public Long getNumeroPeticao() {
		return numeroPeticao;
	}

	public void setNumeroPeticao(Long numero) {
		this.numeroPeticao = numero;
	}


	@Column(name = "ANO_PETICAO")
	public Short getAnoPeticao() {
		return anoPeticao;
	}

	public void setAnoPeticao(Short ano) {
		this.anoPeticao = ano;
	}


	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_ANDAMENTO")
	public Andamento getTipoAndamento() {
		return this.tipoAndamento;
	}

	public void setTipoAndamento(Andamento tipoAndamento) {
		this.tipoAndamento = tipoAndamento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_ANDAMENTO")
	public Date getDataAndamento() {
		return dataAndamento;
	}

	public void setDataAndamento(Date dataAndamento) {
		this.dataAndamento = dataAndamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return this.objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR")
	public Setor getSetor() {
		return this.setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_HORA_SISTEMA")
	public Date getDataHoraSistema() {
		return this.dataHoraSistema;
	}

	public void setDataHoraSistema(Date dataHoraSistema) {
		this.dataHoraSistema = dataHoraSistema;
	}

	@Column(name = "DSC_OBSER_AND")
	public String getDescricaoObservacaoAndamento() {
		return this.descricaoObservacaoAndamento;
	}

	public void setDescricaoObservacaoAndamento(String descricaoObserAnd) {
		this.descricaoObservacaoAndamento = descricaoObserAnd;
	}

	@Column(name = "SIG_USUARIO")
	public String getCodigoUsuario() {
		return this.codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	@Column(name = "DSC_OBS_INTERNA")
	public String getDescricaoObservacaoInterna() {
		return this.descricaoObservacaoInterna;
	}

	public void setDescricaoObservacaoInterna(String descricaoobservacaoInterna) {
		this.descricaoObservacaoInterna = descricaoobservacaoInterna;
	}

	@Column(name = "NUM_SEQUENCIA")
	public Long getNumeroSequencia() {
		return numeroSequencia;
	}

	public void setNumeroSequencia(Long numeroSequencia) {
		this.numeroSequencia = numeroSequencia;
	}

	@Column(name = "NUM_SEQ_ERRADO")
	public Long getNumeroSequenciaErrado() {
		return numeroSequenciaErrado;
	}

	public void setNumeroSequenciaErrado(Long numeroSequenciaErrado) {
		this.numeroSequenciaErrado = numeroSequenciaErrado;
	}

	/*@Column(name = "COD_ANDAMENTO")
	public Long getCodigoAndamento() {
		return codigoAndamento;
	}

	public void setCodigoAndamento(Long codigoAndamento) {
		this.codigoAndamento = codigoAndamento;
	}*/

	@Transient
	public Long getIdAndamentoProcesso() {
		return getId();
	}

	@Column(name = "flg_ultimo_andamento")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getUltimoAndamento() {
		return ultimoAndamento;
	}

	public void setUltimoAndamento(Boolean ultimoAndamento) {
		this.ultimoAndamento = ultimoAndamento;
	}

	@Column(name = "FLG_VALIDO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getValido() {
		return valido;
	}

	public void setValido(Boolean valido) {
		this.valido = valido;
	}
}

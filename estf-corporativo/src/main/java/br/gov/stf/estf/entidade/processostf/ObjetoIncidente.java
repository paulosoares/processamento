package br.gov.stf.estf.entidade.processostf;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.Rotulo;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.usuario.Usuario;

/**
 * Generalização das entidades: Processo, Recurso, Petição e Protocolo.
 * 
 * @author Rodrigo Barreiros
 * @author Demétrius Jubé
 * 
 * @since 15.07.2009
 */
@Entity
@Table(name = "OBJETO_INCIDENTE", schema = "JUDICIARIO")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ObjetoIncidente<PROCESSO extends ObjetoIncidente<?>>
		extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 803504231433521575L;

	private Long id;

	private TipoObjetoIncidente tipoObjetoIncidente;

	private PROCESSO principal;

	private ObjetoIncidente<?> anterior;

	private ObjetoIncidente<?> pai;

	private List<ObjetoIncidente<?>> cadeia;

	private TipoConfidencialidade tipoConfidencialidade;

	private List<Parte> partesVinculadas;

	private List<ParteOtimizada> partesVinculadasOtimizadas;

	private List<Texto> textos;

	private SituacaoMinistroProcesso situacaoMinistroProcesso;

	private List<ListaProcessos> listasProcessos;

	private String tipoMeio;

	private List<IncidentePreferencia> preferencias;

	private List<Rotulo> rotulos;
	
	private Date dataPublicacao;
	
	private boolean objetoIncidentePodeSerDeslocado = false;
	private boolean deslocarObjetoIncidente = false;
	private Usuario usuarioCriacao;

	private Long relatorIncidenteId;
	
	private Long redatorIncidenteId;
	
	private List<ObservacaoProcesso> observacaoProcesso;
	private Boolean criminal;
	
	private Integer numeroNivelSigilo;
	
	private String nivelSigilo;
	/**
	 * Identifica um objeto processual (Processo, Protocolo, Petição, Súmula,
	 * Recurso ou Incidente Julgamento).
	 */
	@Id
	@Column(name = "SEQ_OBJETO_INCIDENTE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_OBJETO_INCIDENTE", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Tipo do Objeto Incidente. Pode ter os valores: PR (Processo), PI
	 * (Protocolo), PA (Petição), RC (Recurso) ou IJ (Incidente Julgamento),
	 * SU(Súmula).
	 */
	@Column(name = "TIP_OBJETO_INCIDENTE", updatable = false)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public TipoObjetoIncidente getTipoObjetoIncidente() {
		return tipoObjetoIncidente;
	}

	public void setTipoObjetoIncidente(TipoObjetoIncidente tipoObjetoIncidente) {
		this.tipoObjetoIncidente = tipoObjetoIncidente;
	}

	/**
	 * Identifica o objeto incidente principal. Este objeto será sempre o
	 * processo. Ficará nulo nos casos de Protocolo e Petição.
	 */
	@Type(type = "")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE_PRINCIPAL")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public PROCESSO getPrincipal() {
		return principal;
	}

	public void setPrincipal(PROCESSO principal) {
		this.principal = principal;
	}

	/**
	 * Identifica o antecessor físico que originou o objeto processual. Será
	 * sempre referente a um Protocolo ou a uma Petição.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE_ANTERIOR")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getAnterior() {
		return anterior;
	}

	public void setAnterior(ObjetoIncidente<?> anterior) {
		this.anterior = anterior;
	}

	/**
	 * Identifica o antecessor lógico que originou o objeto processual. Ex.: O
	 * identificador do processo em cima do qual o recurso está sendo
	 * interposto, o identificador do Recurso em cima do qual outro Recurso está
	 * sendo interposto (Recurso do Recurso) ou o identificador do processo em
	 * que o incidente de julgamento está sendo feito.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE_PAI")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getPai() {
		return pai;
	}

	public void setPai(ObjetoIncidente<?> pai) {
		this.pai = pai;
	}

	@Transient
	public abstract String getIdentificacao();
	
	
	@Transient
	public String getIdentificacaoCompleta() {
		return getIdentificacao();
	}

	@Transient
	public String getDescricao() {
		return "";
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE_PRINCIPAL", insertable = false, updatable = false)
	@Where(clause = "SEQ_OBJETO_INCIDENTE != SEQ_OBJETO_INCIDENTE_PRINCIPAL")
	public List<ObjetoIncidente<?>> getCadeia() {
		return cadeia;
	}

	public void setCadeia(List<ObjetoIncidente<?>> filhos) {
		this.cadeia = filhos;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", insertable = false, updatable = false)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public List<Parte> getPartesVinculadas() {
		return partesVinculadas;
	}

	public void setPartesVinculadas(List<Parte> listaPartesVinculadas) {
		this.partesVinculadas = listaPartesVinculadas;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", insertable = false, updatable = false)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public List<ParteOtimizada> getPartesVinculadasOtimizadas() {
		return partesVinculadasOtimizadas;
	}

	public void setPartesVinculadasOtimizadas(
			List<ParteOtimizada> listaPartesVinculadas) {
		this.partesVinculadasOtimizadas = listaPartesVinculadas;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", insertable = false, updatable = false)
	public List<Texto> getTextos() {
		return textos;
	}

	public void setTextos(List<Texto> textos) {
		this.textos = textos;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", insertable = false, updatable = false)
	public SituacaoMinistroProcesso getSituacaoMinistroProcesso() {
		return situacaoMinistroProcesso;
	}

	public void setSituacaoMinistroProcesso(
			SituacaoMinistroProcesso situacaoMinistroProcesso) {
		this.situacaoMinistroProcesso = situacaoMinistroProcesso;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(schema = "EGAB", name = "PROCESSO_SETOR_GRUPO", joinColumns = @JoinColumn(name = "SEQ_OBJETO_INCIDENTE"), inverseJoinColumns = @JoinColumn(name = "SEQ_GRUPO_PROCESSO_SETOR"))
	public List<ListaProcessos> getListasProcessos() {
		return listasProcessos;
	}

	public void setListasProcessos(List<ListaProcessos> listasProcessos) {
		this.listasProcessos = listasProcessos;
	}

	@Column(name = "TIP_CONFIDENCIALIDADE", updatable = false)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoConfidencialidade"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "valueOfMethod", value = "valueOfCodigo") })
	public TipoConfidencialidade getTipoConfidencialidade() {
		return tipoConfidencialidade;
	}

	public void setTipoConfidencialidade(
			TipoConfidencialidade tipoConfidencialidade) {
		this.tipoConfidencialidade = tipoConfidencialidade;
	}

	@Formula("(select pr.tip_meio_processo from judiciario.objeto_incidente oi, judiciario.processo pr "
			+ "where pr.seq_objeto_incidente = SEQ_OBJETO_INCIDENTE and oi.tip_objeto_incidente = 'PR' and oi.seq_objeto_incidente = pr.seq_objeto_incidente)")
	public String getTipoMeio() {
		return tipoMeio;
	}

	public void setTipoMeio(String tipoMeio) {
		this.tipoMeio = tipoMeio;
	}

	// somente para processo, retorna true se eletronico ou false físico
	@Transient
	public boolean getEletronico() {
		return (tipoMeio != null && tipoMeio.equals("E"));
	}

	public void setPreferencias(List<IncidentePreferencia> preferencias) {
		this.preferencias = preferencias;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public List<IncidentePreferencia> getPreferencias() {
		return preferencias;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_INCLUSAO", insertable = false, updatable = false)
	public Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(schema = "JUDICIARIO", name = "ROTULO_OBJETO_INCIDENTE", joinColumns = @JoinColumn(name = "SEQ_OBJETO_INCIDENTE"), inverseJoinColumns = @JoinColumn(name = "SEQ_ADENDO_TEXTUAL"))
	public List<Rotulo> getRotulos() {
		return rotulos;
	}

	public void setRotulos(List<Rotulo> rotulos) {
		this.rotulos = rotulos;
	}

	@Formula("(SELECT STF.FNC_DATA_ACORDAO(SEQ_OBJETO_INCIDENTE) FROM DUAL)")
	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	@Transient
	public boolean isObjetoIncidentePodeSerDeslocado() {
		return objetoIncidentePodeSerDeslocado;
	}

	public void setObjetoIncidentePodeSerDeslocado(boolean objetoIncidentePodeSerDeslocado) {
		this.objetoIncidentePodeSerDeslocado = objetoIncidentePodeSerDeslocado;
	}

	@Transient
	public boolean isDeslocarObjetoIncidente() {
		return deslocarObjetoIncidente;
	}

	public void setDeslocarObjetoIncidente(boolean deslocarObjetoIncidente) {
		this.deslocarObjetoIncidente = deslocarObjetoIncidente;
	}

	public void setUsuarioCriacao(Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}
	
	@Formula("(SELECT JUDICIARIO.PKG_RELATORIA.FNC_RECUPERA_RELATOR(SEQ_OBJETO_INCIDENTE) FROM DUAL)")
	public Long getRelatorIncidenteId() {
		return relatorIncidenteId;
	}
	
	public void setRelatorIncidenteId(Long relatorIncidenteId) {
		this.relatorIncidenteId = relatorIncidenteId;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(schema = "JUDICIARIO", name = "OBSERVACAO_PROCESSO", joinColumns = @JoinColumn(name = "SEQ_OBJETO_INCIDENTE"), inverseJoinColumns = @JoinColumn(name = "SEQ_ADENDO_TEXTUAL"))
	public List<ObservacaoProcesso> getObservacaoProcesso() {
		return observacaoProcesso;
	}

	public void setObservacaoProcesso(List<ObservacaoProcesso> observacaoProcesso) {
		this.observacaoProcesso = observacaoProcesso;
	}

	@Formula("(SELECT count(*) FROM JUDICIARIO.INCIDENTE_PREFERENCIA ip WHERE ip.SEQ_TIPO_PREFERENCIA = 2 AND ip.SEQ_OBJETO_INCIDENTE = SEQ_OBJETO_INCIDENTE)")
	public Boolean isCriminal() {
		return criminal;
	}

	public void setCriminal(Boolean criminal) {
		this.criminal = criminal;
	}

	@Column(name = "NUM_NIVEL_SIGILO")
	public Integer getNumeroNivelSigilo() {
		return numeroNivelSigilo;
	}

	public void setNumeroNivelSigilo(Integer numeroNivelSigilo) {
		this.numeroNivelSigilo = numeroNivelSigilo;
	}
	
	@Transient
	public String getNivelSigilo() {
		 return TipoNivelConfidencialidade.valueOfCodigo(numeroNivelSigilo).getDescricao();
	}

	public void setNivelSigilo(String nivelSigilo) {
		this.nivelSigilo = nivelSigilo;
	}
	
	
}

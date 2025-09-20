package br.gov.stf.estf.entidade.documento;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.TipoVoto.TipoVotoConstante;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.publicacao.FaseTextoProcesso;
import br.gov.stf.estf.entidade.usuario.GrupoUsuario;
import br.gov.stf.estf.entidade.usuario.Responsavel;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.util.DateTimeHelper;

@Entity
@Table(schema = "STF", name = "TEXTOS")
public class Texto extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -3206700955877563697L;

	private Long id;

	private TipoTexto tipoTexto;
	private Ministro ministro;
	private Long codigoMinistro;
	private Long codigoRecurso;

	private Boolean publico;
	private Boolean textosIguais;
	private Boolean salaJulgamento;
	private Boolean publiccaoRTJ;

	private Date dataSessao;
	private String observacao;

	private TipoRestricao tipoRestricao;
	private FaseTexto tipoFaseTextoDocumento;

	private List<FaseTextoProcesso> fasesTextoProcesso;

	private ArquivoEletronico arquivoEletronico;
	private List<DocumentoTexto> documentosTexto;

	private Integer codigoBrs;
	private Usuario usuarioInclusao;
	private Usuario usuarioAlteracao;
	private Date dataInclusao;
	private Date dataAlteracao;
	private Date dataCriacao;
	private Long sequenciaVoto;

	private String siglaClasseProcessual;
	private Long numeroProcessual;

	private Date dataComposicaoParcial;

	private String tipoJulgamento;

	private Usuario usuarioResponsavel;
	private GrupoUsuario grupoResponsavel;

	protected Long tipoVotoId;

	/**
	 * Listas em que este texto foi inserido
	 */
	private List<ListaTextos> listasTexto;

	private ObjetoIncidente<?> objetoIncidente;

	private Long sequenciaObjetoIncidente;

	private ControleVoto controleVoto;

	private Boolean favoritoNoGabinete;

	private Boolean liberacaoAntecipada = Boolean.FALSE;

	private Boolean origemDigital = Boolean.FALSE;

	private Colegiado colegiado;

	private Sessao sessao; 
	
	private TipoLiberacao tipoLiberacao;

	private Boolean inclusaoAutomatica;

	private ObjetoIncidente<?> objetoIncidenteReferendo;

	private Ministro ministroDivergente;
	
	private Boolean julgamentoDigital;


	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumns({ @JoinColumn(name = "COD_TIPO_TEXTO", updatable = false, insertable = false, referencedColumnName = "COD_TIPO_TEXTO"),
			@JoinColumn(name = "SEQ_VOTOS", referencedColumnName = "SEQ_VOTO", updatable = false, insertable = false),
			@JoinColumn(name = "DAT_SESSAO", updatable = false, insertable = false, referencedColumnName = "DAT_SESSAO"),
			@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", updatable = false, insertable = false, referencedColumnName = "SEQ_OBJETO_INCIDENTE"),
			@JoinColumn(name = "COD_MINISTRO", updatable = false, insertable = false, referencedColumnName = "COD_MINISTRO") })
	public ControleVoto getControleVoto() {
		return controleVoto;
	}

	public void setControleVoto(ControleVoto controleVoto) {
		this.controleVoto = controleVoto;
	}

	@Id
	@Column(name = "SEQ_TEXTOS")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_TEXTOS", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO", nullable = false, insertable = true, updatable = true)
	// @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public ArquivoEletronico getArquivoEletronico() {
		return arquivoEletronico;
	}

	public void setArquivoEletronico(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}

	@LazyCollection(LazyCollectionOption.EXTRA)
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "texto")
	public List<DocumentoTexto> getDocumentosTexto() {
		return documentosTexto;
	}

	public void setDocumentosTexto(List<DocumentoTexto> documentosTexto) {
		this.documentosTexto = documentosTexto;
	}

	@Column(name = "COD_TIPO_TEXTO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = { @Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.TipoTexto"),
			@Parameter(name = "identifierMethod", value = "getCodigo") })
	public TipoTexto getTipoTexto() {
		return tipoTexto;
	}

	public void setTipoTexto(TipoTexto tipoTexto) {
		this.tipoTexto = tipoTexto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO", nullable = false, insertable = true, updatable = false)
	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	@Column(name = "COD_MINISTRO", insertable = false, updatable = false)
	public Long getCodigoMinistro() {
		return codigoMinistro;
	}

	public void setCodigoMinistro(Long codigoMinistro) {
		this.codigoMinistro = codigoMinistro;
	}

	@Column(name = "FLG_PUBLICO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPublico() {
		return publico;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TEXTOS", updatable = false, insertable = false)
	@OrderBy(value = "dataTransicao DESC")
	public List<FaseTextoProcesso> getFasesTextoProcesso() {
		return fasesTextoProcesso;
	}

	public void setFasesTextoProcesso(List<FaseTextoProcesso> fasesTextoProcesso) {
		this.fasesTextoProcesso = fasesTextoProcesso;
	}

	@Transient
	public Date getDateTransicaoAtual() {
		Date data = null;
		List<FaseTextoProcesso> fases = getFasesTextoProcesso();
		if (fases != null && fases.size() > 0) {
			data = fases.get(0).getDataTransicao();
		}
		return data;

	}

	public void setPublico(Boolean publico) {
		this.publico = publico;
	}

	@Column(name = "FLG_TEXTOS_IGUAIS")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getTextosIguais() {
		return textosIguais;
	}

	public void setTextosIguais(Boolean textosIguais) {
		this.textosIguais = textosIguais;
	}

	@Column(name = "FLG_SALA_JULG")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getSalaJulgamento() {
		return salaJulgamento;
	}

	public void setSalaJulgamento(Boolean salaJulgamento) {
		this.salaJulgamento = salaJulgamento;
	}

	@Column(name = "FLG_PUBLICACAO_RTJ")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPubliccaoRTJ() {
		return publiccaoRTJ;
	}

	public void setPubliccaoRTJ(Boolean publiccaoRTJ) {
		this.publiccaoRTJ = publiccaoRTJ;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_SESSAO")
	public Date getDataSessao() {
		return dataSessao;
	}

	public void setDataSessao(Date dataSessao) {
		this.dataSessao = dataSessao;
	}

	@Column(name = "OBS_TEXTO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name = "TIP_RESTRICAO")
	@Enumerated(EnumType.STRING)
	public TipoRestricao getTipoRestricao() {
		return tipoRestricao;
	}

	public void setTipoRestricao(TipoRestricao tipoRestricao) {
		this.tipoRestricao = tipoRestricao;
	}

	@Column(name = "COD_TIPO_FASE_TEXTO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = { @Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.tipofase.FaseTexto"),
			@Parameter(name = "identifierMethod", value = "getCodigoFase"), @Parameter(name = "nullValue", value = "NAO_ELABORADO") })
	public FaseTexto getTipoFaseTextoDocumento() {
		return tipoFaseTextoDocumento;
	}

	public void setTipoFaseTextoDocumento(FaseTexto tipoFaseTextoDocumento) {
		this.tipoFaseTextoDocumento = tipoFaseTextoDocumento;
	}

	@Transient
	public Responsavel getResponsavel() {
		if (getGrupoResponsavel() != null)
			return getGrupoResponsavel();
		else
			return getUsuarioResponsavel();
	}

	public void setResponsavel(Responsavel responsavel) {
		if (responsavel == null) {
			setUsuarioResponsavel(null);
			setGrupoResponsavel(null);
		}

		if (responsavel instanceof GrupoUsuario) {
			setGrupoResponsavel((GrupoUsuario) responsavel);
			setUsuarioResponsavel(null);
		}

		if (responsavel instanceof Usuario) {
			setGrupoResponsavel(null);
			setUsuarioResponsavel((Usuario) responsavel);
		}
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_INCLUSAO", insertable = true, updatable = false)
	@org.hibernate.annotations.NotFound(action = NotFoundAction.IGNORE)
	public Usuario getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(Usuario usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_ALTERACAO", insertable = true, updatable = false)
	@org.hibernate.annotations.NotFound(action = NotFoundAction.IGNORE)
	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_INCLUSAO", updatable = false)
	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_ALTERACAO", updatable = false)
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_CRIACAO")
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Transient
	public Boolean getPossuiPDF() {
		if (documentosTexto != null && documentosTexto.size() > 0) {
			for (DocumentoTexto doc : documentosTexto) {
				if (doc.getDocumentoEletronicoView() != null && !doc.getDocumentoEletronicoView().getDescricaoStatusDocumento().equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_CANCELADO)) {
					// Alterado por Demétrius Jubé - 18/12/2009
					// Só existirá PDF se o documento eletrônico não
					// estiver com o status CANCELADO
					return true;
				}
			}
		}
		return false;
	}

	@Transient
	public String getSituacao() {
		if (tipoFaseTextoDocumento != null) {
			tipoFaseTextoDocumento.getDescricao();
		} else if (arquivoEletronico == null || arquivoEletronico.getId() == null) {
			return FaseTexto.NAO_ELABORADO.getDescricao();
		}
		return "";
	}

	@Transient
	public String getIdentificacao() {
		StringBuffer identificacaoProcessual = new StringBuffer();
		if (getObjetoIncidente() != null) {
			identificacaoProcessual.append(getObjetoIncidente().getIdentificacao());
		}
		return identificacaoProcessual.toString();
	}

	@Transient
	public String getIdentificacaoCompleta() {
		StringBuffer identificacaoProcessual = new StringBuffer();
		identificacaoProcessual.append(getIdentificacao());
		if (tipoTexto != null) {
			identificacaoProcessual.append("  " + getTipoTexto().getDescricao());
		}
		return identificacaoProcessual.toString();
	}

	@Transient
	public String getAuditoriaInclusao() {
		if (usuarioInclusao != null && dataInclusao != null) {
			return usuarioInclusao.getNome() + " - " + DateTimeHelper.getDataHoraString(dataInclusao);
		}
		return "";
	}

	@Transient
	public String getAuditoriaAlteracao() {
		if (usuarioAlteracao != null && dataAlteracao != null) {
			return usuarioAlteracao.getNome() + " - " + DateTimeHelper.getDataHoraString(dataAlteracao);
		}
		return "";
	}

	@Column(name = "SEQ_VOTOS")
	public Long getSequenciaVoto() {
		return sequenciaVoto;
	}

	public void setSequenciaVoto(Long sequencialVoto) {
		this.sequenciaVoto = sequencialVoto;
	}

	// @OneToMany(fetch = FetchType.LAZY)
	// @JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO", referencedColumnName =
	// "SEQ_ARQUIVO_ELETRONICO", updatable = false, insertable = false)
	// @Where(clause = "FLG_TEXTOS_IGUAIS = 'S'")
	// public List<Texto> getListaTextosIguais() {
	// return listaTextosIguais;
	// }

	// public void setListaTextosIguais(List<Texto> listaTextosIguais) {
	// this.listaTextosIguais = listaTextosIguais;
	// }

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(schema = "DOC", name = "TEXTO_LISTA_TEXTO", joinColumns = @JoinColumn(name = "SEQ_TEXTOS"), inverseJoinColumns = @JoinColumn(name = "SEQ_LISTA_TEXTO"))
	public List<ListaTextos> getListasTexto() {
		return listasTexto;
	}

	public void setListasTexto(List<ListaTextos> listasTexto) {
		this.listasTexto = listasTexto;
	}

	@Column(name = "SEQ_ARQUIVO_ELETRONICO", insertable = false, updatable = false)
	public Integer getCodigoBrs() {
		return codigoBrs;
	}

	public void setCodigoBrs(Integer codigoBrs) {
		this.codigoBrs = codigoBrs;
	}

	public void adicionarDocumentoTexto(DocumentoTexto documentoTexto) {
		documentoTexto.setTexto(this);
		getDocumentosTexto().add(documentoTexto);
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {})
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", updatable = true, insertable = true)
//	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@Column(name = "SEQ_OBJETO_INCIDENTE", updatable = false, insertable = false)
	public Long getSequenciaObjetoIncidente() {
		return sequenciaObjetoIncidente;
	}

	public void setSequenciaObjetoIncidente(Long sequenciaObjetoIncidente) {
		this.sequenciaObjetoIncidente = sequenciaObjetoIncidente;
	}

	@Column(name = "SIG_CLASSE_PROCES")
	public String getSiglaClasseProcessual() {
		return siglaClasseProcessual;
	}

	public void setSiglaClasseProcessual(String siglaClasseProcessual) {
		this.siglaClasseProcessual = siglaClasseProcessual;
	}

	@Column(name = "NUM_PROCESSO")
	public Long getNumeroProcessual() {
		return numeroProcessual;
	}

	public void setNumeroProcessual(Long numeroProcessual) {
		this.numeroProcessual = numeroProcessual;
	}

	public void setDataComposicaoParcial(Date dataComposicaoParcial) {
		this.dataComposicaoParcial = dataComposicaoParcial;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_COMPOSICAO_PARCIAL")
	public Date getDataComposicaoParcial() {
		return dataComposicaoParcial;
	}

	@Column(name = "TIP_JULGAMENTO")
	public String getTipoJulgamento() {
		return tipoJulgamento;
	}

	public void setTipoJulgamento(String tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
	};

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_AUTOR_INTELECTUAL", nullable = true, insertable = true, updatable = true)
	public Usuario getUsuarioResponsavel() {
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(Usuario usuarioResponsavel) {
		if (usuarioResponsavel instanceof Usuario)
			this.usuarioResponsavel = usuarioResponsavel;
		else
			this.usuarioResponsavel = null;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_GRUPO_RESPONSAVEL", nullable = true, insertable = true, updatable = true)
	public GrupoUsuario getGrupoResponsavel() {
		return grupoResponsavel;
	}

	public void setGrupoResponsavel(GrupoUsuario grupoResponsavel) {
		if (grupoResponsavel instanceof GrupoUsuario)
			this.grupoResponsavel = grupoResponsavel;
		else
			this.grupoResponsavel = null;
	}

	@Column(name = "FLG_FAVORITO_GABINETE")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getFavoritoNoGabinete() {
		return favoritoNoGabinete;
	}

	public void setFavoritoNoGabinete(Boolean favoritoNoGabinete) {
		this.favoritoNoGabinete = favoritoNoGabinete;
	}

	@Column(name = "FLG_LIBERACAO_ANTECIPADA", nullable = false)
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getLiberacaoAntecipada() {
		return liberacaoAntecipada;
	}

	public void setLiberacaoAntecipada(Boolean liberacaoAntecipada) {
		this.liberacaoAntecipada = liberacaoAntecipada;
	}

	@Column(name = "COD_TIPO_VOTO")
	protected Long getTipoVotoId() {
		return tipoVotoId;
	}

	protected void setTipoVotoId(Long tipoVotoId) {
		this.tipoVotoId = tipoVotoId;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_MINISTRO_DIVERGENTE", updatable = true, insertable = true)
	public Ministro getMinistroDivergente() {
		return ministroDivergente;
	}

	public void setMinistroDivergente(Ministro ministroDivergente) {
		this.ministroDivergente = ministroDivergente;
	}

	@Transient
	public TipoVotoConstante getTipoVoto() {
		return TipoVotoConstante.getById(tipoVotoId);
	}

	public void setTipoVoto(TipoVotoConstante tipoVoto) {
		if (tipoVoto != null)
			this.tipoVotoId = tipoVoto.getId();
		else
			this.tipoVotoId = null;
	}

	@Column(name = "COD_RECURSO")
	public Long getCodigoRecurso() {
		return codigoRecurso;
	}

	public void setCodigoRecurso(Long codigoRecurso) {
		this.codigoRecurso = codigoRecurso;
	}

	public enum TipoRestricao {
		P("Público"), S("Restritos aos usuários do setor"), U("Restrito ao usuário que criou o documento"), N("Não permitido alteração");

		private String descricao;

		TipoRestricao(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

	}

	@Column(name = "FLG_ORIGEM_DIGITAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getOrigemDigital() {
		return origemDigital;
	}

	public void setOrigemDigital(Boolean origemDigital) {
		this.origemDigital = origemDigital;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_COLEGIADO", insertable = true, updatable = true)
	public Colegiado getColegiado() {
		return colegiado;
	}

	public void setColegiado(Colegiado colegiado) {
		this.colegiado = colegiado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_SESSAO", insertable = true, updatable = true)
	public Sessao getSessao() {
		return sessao;
	}

	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}
	
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.TipoLiberacao"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "valueOfMethod", value = "valueOfSigla"),
			@Parameter(name = "identifierMethod", value = "getSigla")})
	@Column(name="TIP_LIBERACAO")
	public TipoLiberacao getTipoLiberacao() {
		return tipoLiberacao;
	}

	public void setTipoLiberacao(TipoLiberacao tipoLiberacao) {
		this.tipoLiberacao = tipoLiberacao;
	}

	@Column(name = "FLG_INCLUSAO_AUTOMATICA")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getInclusaoAutomatica() {
		return inclusaoAutomatica;
	}

	public void setInclusaoAutomatica(Boolean inclusaoAutomatica) {
		this.inclusaoAutomatica = inclusaoAutomatica;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE_REFERENDO", updatable = true, insertable = false)
	public ObjetoIncidente<?> getObjetoIncidenteReferendo() {
		return objetoIncidenteReferendo;
	}

	public void setObjetoIncidenteReferendo(ObjetoIncidente<?> objetoIncidenteReferendo) {
		this.objetoIncidenteReferendo = objetoIncidenteReferendo;
	}
	
	@Column(name = "FLG_JULGAMENTO_DIGITAL", insertable = false, updatable = false)
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getJulgamentoDigital() {
		return julgamentoDigital;
	}

	public void setJulgamentoDigital(Boolean origemDigital) {
		this.julgamentoDigital = origemDigital;
	}
}

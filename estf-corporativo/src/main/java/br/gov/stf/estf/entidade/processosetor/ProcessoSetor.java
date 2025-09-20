package br.gov.stf.estf.entidade.processosetor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OrderBy;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.AbrangenciaRotulo;
import br.gov.stf.estf.entidade.documento.Rotulo;
import br.gov.stf.estf.entidade.julgamento.Tema;
import br.gov.stf.estf.entidade.julgamento.TipoTema;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.IncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.PrescricaoReu;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.ReferenciaPrescricao;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(schema = "EGAB", name = "PROCESSO_SETOR")
public class ProcessoSetor extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -7319368018320649720L;
	public static final String RELATORIO_ANALITICO_COMPLETO = "RelatorioAnaliticoCompleto";
	public static final String RELATORIO_ANALITICO_FASE = "RelatorioAnaliticoFase";
	public static final String RELATORIO_ANALITICO_DESLOCAMENTO = "RelatorioAnaliticoDeslocamento";
	public static final String RELATORIO_ANALITICO_DISTRIBUICAO = "RelatorioAnaliticoDistribuicao";
	public static final String RELATORIO_ANALITICO_ASSUNTO = "RelatorioAnaliticoAssunto";
	public static final String RELATORIO_SINTETICO_FASE = "RelatorioSinteticoFase";
	public static final String RELATORIO_SINTETICO_DESLOCAMENTO = "RelatorioSinteticoDeslocamento";
	public static final String RELATORIO_SINTETICO_DISTRIBUICAO = "RelatorioSinteticoDistribuicao";
	public static final String RELATORIO_SINTETICO_ASSUNTO = "RelatorioSinteticoAssunto";

	public enum TipoIncidenteJulgamentoConstante {
		MERITO("M", "Mérito"), MEDIDA_CAUTELAR(TipoIncidenteJulgamento.SIGLA_MEDIDA_CAUTELAR, "Medida Cautelar");

		private String sigla;
		private String descricao;

		private TipoIncidenteJulgamentoConstante(String sigla, String descricao) {
			this.sigla = sigla;
			this.descricao = descricao;
		}

		public String getSigla() {
			return sigla;
		}

		public String getDescricao() {
			return descricao;
		}

	}

	private Long id;
	private Setor setor;
	private Date dataEntrada;
	private Date dataSaida;
	private Date dataFimTramite;
	private String siglaClasseProcessual;
	private Long numeroProcessual;

	private String complementoAssunto;
	private String observacaoProcessoSetor;

	private Date dataArquivo;

	private Boolean liminar;
	private Boolean preferencia;
	private Boolean sobrestado;

	private Boolean agravoRegimental;
	private Boolean pgr;
	private Boolean justicaGratuita;
	private Boolean maior60Anos;

	private HistoricoDeslocamento deslocamentoAtual;
	private HistoricoDistribuicao distribuicaoAtual;
	private HistoricoFase faseAtual;

	private Short valorGravidade;
	private Short valorUrgencia;
	private Short valorTendencia;

	private List<HistoricoDeslocamento> historicoDeslocamentos = new LinkedList<HistoricoDeslocamento>();
	private List<HistoricoDistribuicao> historicoDistribuicoes = new LinkedList<HistoricoDistribuicao>();
	private List<HistoricoFase> historicoFases = new LinkedList<HistoricoFase>();

	private ObjetoIncidente<?> objetoIncidente;

	private Set<Prioridade> prioridades = new HashSet<Prioridade>(0);
	private Set<TarefaProcesso> tarefaProcessos = new HashSet<TarefaProcesso>(0);
	private List<GrupoProcessoSetor> grupos = new LinkedList<GrupoProcessoSetor>();
	private List<PrescricaoReu> prescricaoReu = new LinkedList<PrescricaoReu>();
	private String preferencias;
	private String descricaoOrigemAtual;
	private String  possuiDecisaoDigital;
	private String  possuiDecisaoDigitalTodos;
	private boolean ordemTemasCrescente = true;
	
	@Id
	@Column(name = "SEQ_PROCESSO_SETOR")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "EGAB.SEQ_PROCESSO_SETOR", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_ENTRADA_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_SAIDA_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_FIM_TRAMITE", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataFimTramite() {
		return dataFimTramite;
	}

	public void setDataFimTramite(Date dataFimTramite) {
		this.dataFimTramite = dataFimTramite;
	}

	@Column(name = "SIG_CLASSE_PROCES", unique = false, nullable = true, insertable = false, updatable = false, length = 6)
	public String getSiglaClasseProcessual() {
		return this.siglaClasseProcessual;
	}

	public void setSiglaClasseProcessual(String siglaClasseProcessual) {
		this.siglaClasseProcessual = siglaClasseProcessual;
	}

	@Column(name = "NUM_PROCESSO", unique = false, nullable = true, insertable = false, updatable = false, precision = 7, scale = 0)
	public Long getNumeroProcessual() {
		return this.numeroProcessual;
	}

	public void setNumeroProcessual(Long numeroProcessual) {
		this.numeroProcessual = numeroProcessual;
	}

	@Column(name = "DSC_COMPLEMENTO_ASSUNTO", unique = false, nullable = true, insertable = true, updatable = true, length = 2000)
	public String getComplementoAssunto() {
		return complementoAssunto;
	}

	public void setComplementoAssunto(String complementoAssunto) {
		this.complementoAssunto = complementoAssunto;
	}

	@Column(name = "OBS_PROCESSO_SETOR", unique = false, nullable = true, insertable = true, updatable = true, length = 4000)
	public String getObservacaoProcessoSetor() {
		return this.observacaoProcessoSetor;
	}

	public void setObservacaoProcessoSetor(String observacaoProcessoSetor) {
		this.observacaoProcessoSetor = observacaoProcessoSetor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_ARQUIVO", unique = false, insertable = true, updatable = true, length = 7)
	public Date getDataArquivo() {
		return this.dataArquivo;
	}

	public void setDataArquivo(Date dataArquivo) {
		this.dataArquivo = dataArquivo;
	}

	@Column(name = "FLG_LIMINAR", unique = false, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getLiminar() {
		return this.liminar;
	}

	public void setLiminar(Boolean liminar) {
		this.liminar = liminar;
	}

	@Column(name = "FLG_SOBRESTADO", unique = false, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getSobrestado() {
		return this.sobrestado;
	}

	public void setSobrestado(Boolean sobrestado) {
		this.sobrestado = sobrestado;
	}

	@Column(name = "FLG_AGRAVO_REGIMENTAL", unique = false, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAgravoRegimental() {
		return this.agravoRegimental;
	}

	public void setAgravoRegimental(Boolean agravoRegimental) {
		this.agravoRegimental = agravoRegimental;
	}

	@Column(name = "FLG_PREFERENCIA", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPreferencia() {
		return this.preferencia;
	}

	public void setPreferencia(Boolean preferencia) {
		this.preferencia = preferencia;
	}

	@Column(name = "FLG_PGR", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPgr() {
		return this.pgr;
	}

	public void setPgr(Boolean pgr) {
		this.pgr = pgr;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DESLOCAMENTO_ATUAL", nullable = true)
	public HistoricoDeslocamento getDeslocamentoAtual() {
		return deslocamentoAtual;
	}

	public void setDeslocamentoAtual(HistoricoDeslocamento deslocamentoAtual) {
		this.deslocamentoAtual = deslocamentoAtual;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DISTRIBUICAO_ATUAL", nullable = true)
	public HistoricoDistribuicao getDistribuicaoAtual() {
		return distribuicaoAtual;
	}

	public void setDistribuicaoAtual(HistoricoDistribuicao distribuicaoAtual) {
		this.distribuicaoAtual = distribuicaoAtual;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_FASE_ATUAL", nullable = true)
	public HistoricoFase getFaseAtual() {
		return faseAtual;
	}

	public void setFaseAtual(HistoricoFase faseAtual) {
		this.faseAtual = faseAtual;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "processoSetor")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy(clause = "DAT_REMESSA DESC, SEQ_HISTORICO_DESLOCAMENTO DESC")
	public List<HistoricoDeslocamento> getHistoricoDeslocamentos() {
		return historicoDeslocamentos;
	}

	public void setHistoricoDeslocamentos(List<HistoricoDeslocamento> historicoDeslocamentos) {
		this.historicoDeslocamentos = historicoDeslocamentos;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "processoSetor")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy(clause = "DAT_DISTRIBUICAO DESC, SEQ_HISTORICO_DISTRIBUICAO DESC")
	public List<HistoricoDistribuicao> getHistoricoDistribuicoes() {
		return historicoDistribuicoes;
	}

	public void setHistoricoDistribuicoes(List<HistoricoDistribuicao> historicoDistribuicoes) {
		this.historicoDistribuicoes = historicoDistribuicoes;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "processoSetor")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy(clause = "DAT_HISTORICO_FASE DESC, SEQ_HISTORICO_FASE DESC")
	public List<HistoricoFase> getHistoricoFases() {
		return historicoFases;
	}

	public void setHistoricoFases(List<HistoricoFase> historicoFases) {
		this.historicoFases = historicoFases;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "processoSetor")
	public Set<Prioridade> getPrioridades() {
		return this.prioridades;
	}

	public void setPrioridades(Set<Prioridade> prioridades) {
		this.prioridades = prioridades;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "processoSetor")
	public Set<TarefaProcesso> getTarefaProcessos() {
		return this.tarefaProcessos;
	}

	public void setTarefaProcessos(Set<TarefaProcesso> tarefaProcessos) {
		this.tarefaProcessos = tarefaProcessos;
	}

	public String toString() {
		return getClass().getName();
	}

	@ManyToMany(cascade = {}, mappedBy = "processosSetor", targetEntity = br.gov.stf.estf.entidade.processosetor.GrupoProcessoSetor.class, fetch = FetchType.LAZY)
	public List<GrupoProcessoSetor> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<GrupoProcessoSetor> grupos) {
		this.grupos = grupos;
	}

	@Column(name = "NUM_INDICADOR_GRAVIDADE", unique = false, nullable = true, insertable = true, updatable = true, precision = 1, scale = 0)
	public Short getValorGravidade() {
		if (valorGravidade == null)
			return 1;
		else
			return valorGravidade;
	}

	public void setValorGravidade(Short valorGravidade) {
		this.valorGravidade = valorGravidade;
	}

	@Column(name = "NUM_INDICADOR_TENDENCIA", unique = false, nullable = true, insertable = true, updatable = true, precision = 1, scale = 0)
	public Short getValorTendencia() {
		if (valorTendencia == null)
			return 1;
		else
			return valorTendencia;
	}

	public void setValorTendencia(Short valorTendencia) {
		this.valorTendencia = valorTendencia;
	}

	@Column(name = "NUM_INDICADOR_URGENCIA", unique = false, nullable = true, insertable = true, updatable = true, precision = 1, scale = 0)
	public Short getValorUrgencia() {
		if (valorUrgencia == null)
			return 1;
		else
			return valorUrgencia;
	}

	public void setValorUrgencia(Short valorUrgencia) {
		this.valorUrgencia = valorUrgencia;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	// Utilitários
	// ----------------------------------------------------------------------------------
	/**
	 * método resposnsavel por multiplicar os valores de Gravidade Urgencia e
	 * tendencia; quanto maior o número maior a prioridade do processo.
	 */
	@Transient
	public Short getValorGut() {
		int valorGut = 1;
		if (valorGravidade != null && valorTendencia != null && valorUrgencia != null) {
			valorGut = valorGravidade.intValue() * valorUrgencia.intValue() * valorTendencia.intValue();
		}
		return (short) valorGut;
	}
	

	@Formula("(SELECT o.dsc_origem "
			+ "  FROM judiciario.origem o "
			+ "  JOIN judiciario.historico_processo_origem hpo ON hpo.cod_origem = o.cod_origem " 
			+ " WHERE hpo.seq_objeto_incidente = SEQ_OBJETO_INCIDENTE "
			+ "   AND hpo.seq_historico_processo_origem = " 
			+ "       CASE WHEN (SELECT COUNT(*) FROM judiciario.historico_processo_origem hpoaux WHERE hpoaux.seq_objeto_incidente = hpo.seq_objeto_incidente AND hpoaux.tip_historico = 'O' AND hpoaux.flg_principal = 'S') > 0 "
			+ "            THEN (SELECT MIN (hpoaux.seq_historico_processo_origem) FROM judiciario.historico_processo_origem hpoaux WHERE hpoaux.seq_objeto_incidente = hpo.seq_objeto_incidente AND hpoaux.tip_historico = 'O' AND hpoaux.flg_principal = 'S') "
			+ "            WHEN (SELECT COUNT(*) FROM judiciario.historico_processo_origem hpoaux WHERE hpoaux.seq_objeto_incidente = hpo.seq_objeto_incidente AND hpoaux.tip_historico = 'O' AND hpoaux.flg_principal = 'N') > 0 "
			+ "            THEN (SELECT MIN (hpoaux.seq_historico_processo_origem) FROM judiciario.historico_processo_origem hpoaux WHERE hpoaux.seq_objeto_incidente = hpo.seq_objeto_incidente AND hpoaux.tip_historico = 'O' AND hpoaux.flg_principal = 'N') "
			+ "            ELSE NULL "
			+ "       END)")
	public String getDescricaoOrigemAtual() {
		return descricaoOrigemAtual;
	}
	
	public void setDescricaoOrigemAtual(String descricaoOrigemAtual) {
		this.descricaoOrigemAtual = descricaoOrigemAtual;
	}

	@Formula("(SELECT decode((count(*)),0,'Não','Sim') " + 
			"  FROM judiciario.objeto_incidente oi, " + 
			"       judiciario.peca_processual pp, " + 
			"       doc.vw_documento dc,  " + 
			"       stf.desloca_processos dp, " + 
			"       stf.setores se, " +
			"       stf.ministros mi " + 
			" WHERE     oi.seq_objeto_incidente = SEQ_OBJETO_INCIDENTE " + 
			"       AND oi.seq_objeto_incidente_principal = pp.seq_objeto_incidente_proc " + 
			"       AND mi.dat_afast_ministro is null and (mi.cod_setor = se.cod_setor or (se.cod_setor in (600000716,600000568,600001102) and se.flg_ativo = 'S')) " +
			"       AND pp.seq_documento = dc.seq_documento " + 
			"       AND oi.seq_objeto_incidente_principal = dp.seq_objeto_incidente " + 
			"       AND dp.flg_ultimo_deslocamento = 'S' " + 
			"       AND dp.cod_orgao_origem = se.cod_setor " + 
			"       AND pp.cod_tipo_peca_processual IN (1060, 1065) " + 
			"       AND dc.sig_sistema = 'SD_ACL_PROCESSO' " + 
			"       AND pp.num_ordem_peca = " + 
			"               (SELECT MAX (UP.num_ordem_peca) " + 
			"                  FROM judiciario.peca_processual UP " + 
			"                 WHERE UP.seq_objeto_incidente_proc = " + 
			"                           oi.seq_objeto_incidente_principal " + 
			"                       AND UP.cod_tipo_peca_processual IN (1060, 1065) and UP.seq_tipo_situacao_peca = 3 ))")
	public String getPossuiDecisaoDigital() {
		return possuiDecisaoDigital;
	}
	
	public void setPossuiDecisaoDigital(String possuiDecisaoDigital) {
		this.possuiDecisaoDigital = possuiDecisaoDigital;
	}
	
	@Formula("(SELECT decode((count(*)),0,'Não','Sim') " +
			"  FROM judiciario.objeto_incidente oi, " + 
			"       judiciario.peca_processual pp, " + 
			"       doc.vw_documento dc  " + 
			" WHERE     oi.seq_objeto_incidente = SEQ_OBJETO_INCIDENTE " + 
			"       AND oi.seq_objeto_incidente_principal = pp.seq_objeto_incidente_proc " + 
			"       AND pp.seq_documento = dc.seq_documento " +  
			"       AND pp.cod_tipo_peca_processual IN (1060, 1065) " + 
			"       AND dc.sig_sistema = 'SD_ACL_PROCESSO' " + 
			"       AND pp.num_ordem_peca = " + 
			"               (SELECT MAX (UP.num_ordem_peca) " + 
			"                  FROM judiciario.peca_processual UP " + 
			"                 WHERE UP.seq_objeto_incidente_proc = " + 
			"                           oi.seq_objeto_incidente_principal " + 
			"                       AND UP.cod_tipo_peca_processual IN (1060, 1065) and UP.seq_tipo_situacao_peca = 3 ))")
	public String getPossuiDecisaoDigitalTodos() {
		return possuiDecisaoDigitalTodos;
	}
	
	public void setPossuiDecisaoDigitalTodos(String possuiDecisaoDigitalTodos) {
		this.possuiDecisaoDigitalTodos = possuiDecisaoDigitalTodos;
	}
	
	@Transient
	public RecursoProcesso getRecursoProcesso() {
		return ObjetoIncidenteUtil.getRecursoProcesso(getObjetoIncidente());
	}

	@Transient
	public Processo getProcesso() {
		return ObjetoIncidenteUtil.getProcesso(getObjetoIncidente());
	}

	@Transient
	public IncidenteJulgamento getIncidenteJulgamento() {
		return ObjetoIncidenteUtil.getIncidenteJulgamento(getObjetoIncidente());
	}

	@Transient
	public Protocolo getProtocolo() {
		return ObjetoIncidenteUtil.getProtocolo(getObjetoIncidente());
	}

	@Transient
	public Peticao getPeticao() {
		return ObjetoIncidenteUtil.getPeticao(getObjetoIncidente());
	}

	@Transient
	public Boolean getIsEletronico() {
		if ((getProcesso() != null && getProcesso().getTipoMeioProcesso() != null && TipoMeioProcesso.ELETRONICO
				.getCodigo().equals(getProcesso().getTipoMeioProcesso().getCodigo()))
				|| (getProtocolo() != null && getProtocolo().getTipoMeioProcesso() != null && TipoMeioProcesso.ELETRONICO
						.getCodigo().equals(getProtocolo().getTipoMeioProcesso().getCodigo())))
			return true;
		else
			return false;
	}

	@Transient
	public Boolean getPossuiIndentificacaoProcessual() {
		if (getProcesso() != null && getProcesso().getClasseProcessual() != null
				&& getProcesso().getNumeroProcessual() != null)
			return true;
		else
			return false;

	}

	@Transient
	public Boolean getPossuiIndentificacaoProtocolo() {
		if (getProtocolo() != null && getProtocolo().getAnoProtocolo() != null
				&& getProtocolo().getNumeroProtocolo() != null)
			return true;
		else
			return false;

	}

	@Transient
	public String getIdentificacao() {
		StringBuffer identificacao = new StringBuffer();

		if (getPossuiIndentificacaoProcessual()) {
			identificacao.append(getIdentificacaoProcessual() + " (" + getIdentificacaoProtocolo() + ")");
		} else {
			identificacao.append(getIdentificacaoProtocolo());
		}

		return identificacao.toString();
	}

	@Transient
	public String getIdentificacaoProcessual() {
		if (getObjetoIncidente() != null)
			return getObjetoIncidente().getIdentificacao();
		else
			return null;
	}

	public static String getSiglaTipoJulgamento(ObjetoIncidente<?> objetoIncidente) {
		if (objetoIncidente instanceof IncidenteJulgamento) {
			return ((IncidenteJulgamento) objetoIncidente).getTipoJulgamento().getSigla();
		} else if (objetoIncidente instanceof Processo || objetoIncidente instanceof RecursoProcesso) {
			return ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla();
		} else
			return "";
	}

	public static Long getCodigoRecursoProcesso(ObjetoIncidente<?> objetoIncidente) {
		if (objetoIncidente instanceof RecursoProcesso) {
			return ((RecursoProcesso) objetoIncidente).getCodigoRecurso();
		} else {
			return 0L;
		}
	}

	public static Long getCodigoTipoRecurso(ObjetoIncidente<?> objetoIncidente) {
		if (objetoIncidente instanceof RecursoProcesso) {
			return ((RecursoProcesso) objetoIncidente).getTipoRecursoProcesso().getId();
		} else if (objetoIncidente instanceof Peticao) {
			return ((Peticao) objetoIncidente).getTipoRecurso().getId();
		} else if (objetoIncidente instanceof Processo) {
			return 0L;
		} else {
			return null;
		}

	}

	@Transient
	public String getIdentificacaoProtocolo() {
		if (getPossuiIndentificacaoProtocolo().booleanValue())
			return getProtocolo().getNumeroProtocolo() + "/" + getProtocolo().getAnoProtocolo();
		else
			return null;
	}

	@Transient
	public List<Assunto> getAssuntos() {
		if (getPossuiIndentificacaoProcessual() && getProcesso() != null && getProcesso().getAssuntos() != null
				&& getProcesso().getAssuntos().size() > 0) {

			return getProcesso().getAssuntos();
		} else if (getPossuiIndentificacaoProtocolo() && getProtocolo() != null && getProtocolo().getAssuntos() != null
				&& getProtocolo().getAssuntos().size() > 0) {
			return getProtocolo().getAssuntos();
		} else
			return null;
	}

	@Transient
	public String getObservacao() {

		StringBuffer observacao = new StringBuffer();

		boolean appended = false;

		if (getPreferencia() != null && getPreferencia()) {
			observacao.append("Preferência");
			appended = true;
		}

		if (getLiminar() != null && getLiminar()) {

			if (appended)
				observacao.append("<br>");

			observacao.append("Liminar");
			appended = true;
		}

		if (getSobrestado() != null && getSobrestado()) {

			if (appended)
				observacao.append("<br>");

			observacao.append("Sobrestado");
			appended = true;
		}

		if (getPossuiResp() != null && getPossuiResp() == true) {

			if (appended)
				observacao.append("<br>");

			observacao.append("Possui RESP");
			appended = true;
		}

		if (getProcesso() != null) {
			if (getProcesso().getTema() != null && getProcesso().getTema().size() > 0) {
				List<Tema> temas = getTemas();
				
				if (temas.size()>0) {
					if (appended)
						observacao.append("<br>");

					observacao.append("Tema(s): ");
					
					for (Tema tema: temas)
						observacao.append(" " + tema.getNumeroSequenciaTema() + ",");
					
					observacao.deleteCharAt(observacao.length()-1);
					appended = true;
				}
			}
			
			if (getProcesso().getRepresentativoControversiaIndicadoOrigem() != null && getProcesso().getRepresentativoControversiaIndicadoOrigem()) {
				if (appended)
					observacao.append("<br>");

				observacao.append("Controvérsia indicada pela origem");
				appended = true;
			}

			if (getProcesso().getQuantidadePreTema() != null && getProcesso().getQuantidadePreTema() > 0) {
				if (appended)
					observacao.append("<br>");

				observacao.append("É um Pré-Tema da Controvérsia");
				appended = true;
			}
		}

		return observacao.toString();
	}
	
	@Transient
	public List<Tema> getTemas() {
		List<Tema> temas = new ArrayList<Tema>();
		if (getProcesso() != null && getProcesso().getTema() != null && getProcesso().getTema().size() > 0) {
			for (Tema tema: getProcesso().getTema())
				if(TipoTema.TipoTemaConstante.REPERCUSSAO_GERAL.getCodigo().equals(tema.getTipoTema().getId()))
					temas.add(tema);
			
			Collections.sort(temas);
			if (!isOrdemTemasCrescente() )
				Collections.reverse(temas);
		}
		return temas;
	}

	public void setPrescricaoReu(List<PrescricaoReu> prescricaoReu) {
		this.prescricaoReu = prescricaoReu;
	}

	@Transient
	public List<PrescricaoReu> getPrescricaoReu() {
		// Prescrição Réu
		if (((Processo) objetoIncidente.getPrincipal()).getReferenciaPrescricao() != null
				&& ((Processo) objetoIncidente.getPrincipal()).getReferenciaPrescricao().size() > 0) {
			ReferenciaPrescricao referenciaPrescricao = ((Processo) objetoIncidente.getPrincipal())
					.getReferenciaPrescricao().get(0);
			prescricaoReu = referenciaPrescricao.getPrescricaoReus();
		}

		return prescricaoReu;
	}
	
	@Transient
	public String getRotulos() {
		List<Rotulo> rotulos = getRotulosPermitidos();
		StringBuilder builder = new StringBuilder();
		for (Rotulo rotulo : rotulos) {
			if (rotulos.indexOf(rotulo) > 0) {
				if (rotulos.indexOf(rotulo) == rotulos.size() - 1) {
					builder.append(" e ");
				} else {
					builder.append(", ");
				}
			}
			builder.append(rotulo.getNome());
		}
		
		return builder.toString();
	}

	@Transient
	private List<Rotulo> getRotulosPermitidos() {
		List<Rotulo> rotulos = objetoIncidente.getRotulos();
		List<Rotulo> rotulosPermitidos = new ArrayList<Rotulo>();
		for (Rotulo rotulo : rotulos) {
			if (rotulo.getAbrangencia().equals(AbrangenciaRotulo.INSTITUCIONAL) || (rotulo.getSetor().getId().equals(getSetor().getId()))) {
				rotulosPermitidos.add(rotulo);
			}
		}
		return rotulosPermitidos;
	}

	@Transient
	// Preferências
	public String getPreferencias() {

		List<IncidentePreferencia> listaPreferencias = ((Processo) objetoIncidente.getPrincipal())
				.getIncidentePreferencia();
		StringBuffer preferenciasSB = new StringBuffer();
		for (IncidentePreferencia preferencia : listaPreferencias) {
			if (listaPreferencias.indexOf(preferencia) > 0) {
				if (listaPreferencias.indexOf(preferencia) == listaPreferencias.size() - 1) {
					preferenciasSB.append(" e ");
				} else {
					preferenciasSB.append(", ");
				}
			}
			preferenciasSB.append(preferencia.getTipoPreferencia().getDescricao());
		}
		preferencias = preferenciasSB.toString();

		return preferencias;
	}

	public void setPreferencias(String preferencias) {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((objetoIncidente == null) ? 0 : objetoIncidente.hashCode());
		result = prime * result + ((setor == null) ? 0 : setor.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessoSetor other = (ProcessoSetor) obj;
		if (objetoIncidente == null) {
			if (other.objetoIncidente != null)
				return false;
		} else if (!objetoIncidente.equals(other.objetoIncidente))
			return false;
		if (setor == null) {
			if (other.setor != null)
				return false;
		} else if (!setor.equals(other.setor))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public Boolean getPossuiResp() {

		Boolean possuiResp = false;

		if (!getPossuiIndentificacaoProcessual()) {

			Protocolo protocolo = getProtocolo();

			if (protocolo != null)
				// System.out.println("Is eletrônico: "+protocolo.getIsEletronico());
				possuiResp = protocolo.getResposta();

			if (possuiResp == null)
				possuiResp = false;
		}

		return possuiResp;
	}

	@Transient
	public Boolean getJusticaGratuita() {
		return justicaGratuita;
	}

	public void setJusticaGratuita(Boolean justicaGratuita) {
		this.justicaGratuita = justicaGratuita;
	}

	@Transient
	public Boolean getMaior60Anos() {
		return maior60Anos;
	}

	public void setMaior60Anos(Boolean maior60Anos) {
		this.maior60Anos = maior60Anos;
	}

	// -------- Métodos de negócio

	public Boolean adicionarHistoricoDeslocamento(HistoricoDeslocamento deslocamento) {
		Boolean result = Boolean.FALSE;

		if (deslocamento == null)
			throw new NullPointerException("Objeto de deslocamento nulo.");

		setDeslocamentoAtual(deslocamento);

		List<HistoricoDeslocamento> historico = getHistoricoDeslocamentos();
		assert (historico != null);
		historico.add(0, deslocamento);

		result = Boolean.TRUE;

		return result;
	}

	public Boolean adicionarHistoricoFase(HistoricoFase fase) {
		Boolean result = Boolean.FALSE;

		if (fase == null)
			throw new NullPointerException("Objeto de fase nulo.");

		setFaseAtual(fase);

		List<HistoricoFase> historico = getHistoricoFases();
		assert (historico != null);
		historico.add(0, fase);

		result = Boolean.TRUE;

		return result;
	}

	@Transient
	public boolean isOrdemTemasCrescente() {
		return ordemTemasCrescente;
	}

	public void setOrdemTemasCrescente(boolean ordemTemasCrescente) {
		this.ordemTemasCrescente = ordemTemasCrescente;
	}
}

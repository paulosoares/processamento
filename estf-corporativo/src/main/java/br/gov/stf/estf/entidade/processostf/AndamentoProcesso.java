package br.gov.stf.estf.entidade.processostf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;

@Entity
@Table(name = "ANDAMENTO_PROCESSOS", schema = "STF")
public class AndamentoProcesso extends ESTFBaseEntity<Long> {

	public static final Long COD_ANDAMENTO_APRESENTACAO_MESA_JULG = 7600L;
	public static final Long COD_ANDAMENTO_INCLUA_EM_PAUTA_MIN_EXT = 7601L;
	public static final Long COD_ANDAMENTO_RETIRADO_MESA = 7203L;
	public static final Long COD_ANDAMENTO_RETIRADO_PAUTA = 7204L;
	public static final Long COD_ANDAMENTO_INICIADO_JULGAMENTO_VIRTUAL = 8525L;
	public static final Long COD_ANDAMENTO_FINALIZADO_JULGAMENTO_VIRTUAL = 8526L;
	
	protected static int BAIXA_AUTOS_DILIGENCIAS = 7100;	
	public static int BAIXA_EXTENA_AUTOS = 7101;
	protected static int BAIXA_DEFINITIVA = 7104;
	protected static int BAIXA_REMESSA_JUIZO = 7108;
	
	public static List<Integer> CODIGOS_ANDAMENTOS_BAIXA = new ArrayList<Integer>(Arrays.asList(BAIXA_AUTOS_DILIGENCIAS, BAIXA_EXTENA_AUTOS, BAIXA_DEFINITIVA, BAIXA_REMESSA_JUIZO));

	private static final long serialVersionUID = -3303348608977289298L;
	
	private Long id;
	private Long codigoAndamento;
	private Andamento tipoAndamento;
	private Date dataAndamento;
	@SuppressWarnings("rawtypes")
	private ObjetoIncidente objetoIncidente;
	private Date dataHoraSistema;
	private String descricaoObservacaoAndamento;
	private Long numeroSequencia;
	private Setor setor;
	private String codigoUsuario;
	private String descricaoObservacaoInterna;
	private OrigemAndamentoDecisao origemAndamentoDecisao;
	private Long numeroSequenciaErrado;
	private Boolean ultimoAndamento;
	private Boolean lancamentoIndevido;
	private String sigClasseProces;
	private Long numProcesso;
	private Ministro presidenteInterino;
	private TipoDevolucao tipoDevolucao;
	private Long recurso;
	private List<TextoAndamentoProcesso> listaTextoAndamentoProcessos;
	
	private Date dataInclusao;
	private Date dataAlteracao;
	private String usuarioInclusao;
	private String usuarioAlteracao;
	
	
	@Id
	@Column(name = "SEQ_ANDAMENTO_PROCESSO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_ANDAMENTO_PROCESSO", allocationSize = 1)
	public Long getId() {
		return this.id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_ANDAMENTO", unique = false, nullable = true, insertable = false, updatable = false)
	public Andamento getTipoAndamento() {
		return this.tipoAndamento;
	}

	public void setTipoAndamento(Andamento tipoAndamento) {
		this.tipoAndamento = tipoAndamento;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_PRESIDENTE_INTERINO", unique = false, nullable = true, insertable = true, updatable = true)
	public Ministro getPresidenteInterino() {
		return presidenteInterino;
	}
	
	public void setPresidenteInterino(Ministro presidenteInterino) {
		this.presidenteInterino = presidenteInterino;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_ANDAMENTO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataAndamento() {
		return dataAndamento;
	}

	public void setDataAndamento(Date dataAndamento) {
		this.dataAndamento = dataAndamento;
	}

	@ManyToOne(cascade = {})
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", insertable = true, updatable = true)
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

	@Column(name = "COD_USUARIO")
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

	public void setDescricaoObservacaoInterna(String descricaoObservacaoInterna) {
		this.descricaoObservacaoInterna = descricaoObservacaoInterna;
	}

	@Column(name = "NUM_SEQUENCIA")
	public Long getNumeroSequencia() {
		return numeroSequencia;
	}

	public void setNumeroSequencia(Long numeroSequencia) {
		this.numeroSequencia = numeroSequencia;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ORIGEM_DECISAO")
	public OrigemAndamentoDecisao getOrigemAndamentoDecisao() {
		return origemAndamentoDecisao;
	}

	public void setOrigemAndamentoDecisao(OrigemAndamentoDecisao origemAndamentoDecisao) {
		this.origemAndamentoDecisao = origemAndamentoDecisao;
	}

	@Column(name = "NUM_SEQ_ERRADO")
	public Long getNumeroSequenciaErrado() {
		return numeroSequenciaErrado;
	}

	public void setNumeroSequenciaErrado(Long numeroSequenciaErrado) {
		this.numeroSequenciaErrado = numeroSequenciaErrado;
	}

	@Column(name = "COD_ANDAMENTO")
	public Long getCodigoAndamento() {
		return codigoAndamento;
	}

	public void setCodigoAndamento(Long codigoAndamento) {
		this.codigoAndamento = codigoAndamento;
	}

	@Transient
	public Long getIdAndamentoProcesso() {
		return getId();
	}

	@Column(name = "flg_ultimo_andamento", insertable = false, updatable = false)
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getUltimoAndamento() {
		return ultimoAndamento;
	}

	public void setUltimoAndamento(Boolean ultimoAndamento) {
		this.ultimoAndamento = ultimoAndamento;
	}

	@Column(name = "FLG_LANCAMENTO_INDEVIDO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getLancamentoIndevido() {
		return lancamentoIndevido;
	}

	public void setLancamentoIndevido(Boolean lancamentoIndevido) {
		this.lancamentoIndevido = lancamentoIndevido;
	}

	public void setSigClasseProces(String sigClasseProces) {
		this.sigClasseProces = sigClasseProces;
	}

	@Column(name = "SIG_CLASSE_PROCES")
	public String getSigClasseProces() {
		return sigClasseProces;
	}

	public void setNumProcesso(Long numProcesso) {
		this.numProcesso = numProcesso;
	}

	@Column(name = "NUM_PROCESSO")
	public Long getNumProcesso() {
		return numProcesso;
	}

	public void setTipoDevolucao(TipoDevolucao tipoDevolucao) {
		this.tipoDevolucao = tipoDevolucao;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_DEVOLUCAO")
	public TipoDevolucao getTipoDevolucao() {
		return tipoDevolucao;
	}
	
	@Transient
	public String getObservacao() {
		String observacao;
		
		observacao = descricaoObservacaoAndamento == null ? "" : descricaoObservacaoAndamento;
		observacao = descricaoObservacaoInterna == null || descricaoObservacaoInterna.trim().equals("") ? 
				observacao : (observacao.trim().equals("") ? descricaoObservacaoInterna : observacao + "\n\n" + descricaoObservacaoInterna);
		
		return observacao;		
	}
	
	@Transient
	public boolean isAndamentoProcessoFindo() {
		return codigoAndamento.equals(Andamentos.PROCESSO_FINDO.getId()) || codigoAndamento.equals(Andamentos.PROCESSO_FINDO_2.getId());
	}

	public void setRecurso(Long recurso) {
		this.recurso = recurso;
	}

	@Column(name="COD_RECURSO")
	public Long getRecurso() {
		return recurso;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ANDAMENTO_PROCESSO", referencedColumnName = "SEQ_ANDAMENTO_PROCESSO", insertable = false, updatable = false)
	public List<TextoAndamentoProcesso> getListaTextoAndamentoProcessos() {
		return listaTextoAndamentoProcessos;
	}

	public void setListaTextoAndamentoProcessos(
			List<TextoAndamentoProcesso> listaTextoAndamentoProcessos) {
		this.listaTextoAndamentoProcessos = listaTextoAndamentoProcessos;
	}

	@Column(name="DAT_INCLUSAO", insertable=false, updatable=false)
	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	@Column(name="DAT_ALTERACAO", insertable=false, updatable=false)
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	@Column(name="USU_INCLUSAO", insertable=false, updatable=false)
	public String getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(String usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	@Column(name="USU_ALTERACAO", insertable=false, updatable=false)
	public String getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(String usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}
	
	@Transient
	public Boolean getPossuiSeqDocumento(){
		if (getListaTextoAndamentoProcessos() != null && getListaTextoAndamentoProcessos().size() > 0){
			for (TextoAndamentoProcesso textAndProc : getListaTextoAndamentoProcessos()){
				if (textAndProc.getSeqDocumento() != null){
					return true;
				}
			}
		}
		return false;
	}
	
	@Transient
	public String getIdentificacao(){
		return sigClasseProces + " " + numProcesso;
	}
}

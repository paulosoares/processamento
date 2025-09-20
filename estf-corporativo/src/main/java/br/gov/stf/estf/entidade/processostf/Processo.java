package br.gov.stf.estf.entidade.processostf;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.documento.MotivoInaptidao;
import br.gov.stf.estf.entidade.julgamento.Tema;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;

/**
 * Representa os processos do STF.
 * 
 * @author Rodrigo Barreiros
 * @author Demétrius Jubé
 * 
 * @since 15.07.2009
 */
@Entity
@Table(name = "VW_PROCESSO_RELATOR", schema = "JUDICIARIO")
@DiscriminatorValue(value = "PR")
public class Processo extends ObjetoIncidente<Processo> {

	private static final long serialVersionUID = 660640234042861041L;

	private TipoConfidencialidade tipoConfidencialidade;

	private String numeroRegistro;

	private String descricaoAviso;

	private Integer quantidadeVolumes;

	private Integer quantidadeApensosFixo;

	private Integer quantidadeApensos;

	private Integer quantidadeFolhas;

	private Boolean baixa;

	private Short quantidadeJuntadasLinha;

	private Boolean reuPreso;

	private Classe classeProcessual;

	private String siglaClasseProcessual;

	private Long numeroProcessual;

	private String descricaoContraFe;

	private String descricaoIdentificaCorrespondencia;

	private String descricaoObservacao;

	private String numeroUnicoProcesso;

	private Date dataAutuacao;

	private Boolean repercussaoGeral;

	private SituacaoProcesso situacao;

	private Setor setorRecebimento;

	private TipoMeioProcesso tipoMeioProcesso;

	private TipoRecebimento tipoRecebimento;

	private Processo processoRepercussaoGeral;

	private List<LegislacaoProcesso> legislacaoProcesso;

	private List<Assunto> assuntos;

	private List<IncidentePreferencia> incidentePreferencia;

	private Ministro ministroRelatorAtual;

	private Ministro ministroRelator;
	
	private List<ReferenciaPrescricao> referenciaPrescricao;

	private List<Processo> apensos;

	private Long quantidadeRepresentativoControversia;

	private Long quantidadePreTema;

	private Boolean filtroEmTramitacao;
	
	private Boolean materiaConstitucional;
	
	private Boolean representativoControversiaIndicadoOrigem;
	
	private List<HistoricoProcessoOrigem> historicoProcessoOrigem;

	private List<Tema> tema;
	
	private List<MotivoInaptidao> motivoInaptidao;
	
	/**
	 * Classificação da petição quanto a permissão de acesso as informações por
	 * usuários específicos.
	 */
	@Column(name = "TIP_CONFIDENCIALIDADE")
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

	/**
	 * Número do registro do processo na origem (Tribunais que enviaram o
	 * processo).
	 */
	@Column(name = "NUM_REGISTRO")
	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	/**
	 * Informações que o usuário acha relevante.
	 */
	@Column(name = "DSC_AVISO")
	public String getDescricaoAviso() {
		return descricaoAviso;
	}

	public void setDescricaoAviso(String descricaoAviso) {
		this.descricaoAviso = descricaoAviso;
	}

	/**
	 * Quantidade de volumes do processo.
	 */
	@Column(name = "QTD_VOLUMES")
	public Integer getQuantidadeVolumes() {
		return quantidadeVolumes;
	}

	public void setQuantidadeVolumes(Integer quantidadeVolumes) {
		this.quantidadeVolumes = quantidadeVolumes;
	}

	/**
	 * Quantidade de apensos do processo.
	 */
	@Column(name = "QTD_APENSOS")
	public Integer getQuantidadeApensosFixo() {
		return quantidadeApensosFixo;
	}

	public void setQuantidadeApensosFixo(Integer quantidadeApensos) {
		this.quantidadeApensosFixo = quantidadeApensos;
	}

	/**
	 * O método que deve ser utilizado é o ProcessoDependenciaService. public
	 * Integer getQuantidadeVinculados(Processo processo)
	 * 
	 */
	@Deprecated
	@Formula("(SELECT count (*) " + "  FROM stf.processo_dependencia pd"
			+ " WHERE     pd.cod_tipo_dependencia_processo in (5,9) "
			+ "       AND pd.sig_classe_vinculador = SIG_CLASSE_PROCES "
			+ "       AND pd.num_processo_vinculador = NUM_PROCESSO "
			+ "       AND pd.dat_fim_dependencia IS NULL) ")
	public Integer getQuantidadeApensos() {
		return quantidadeApensos;
	}

	public void setQuantidadeApensos(Integer quantidadeApensos) {
		this.quantidadeApensos = quantidadeApensos;
	}

	/**
	 * Quantidade de folhas de todos os volumes e apensos do processo.
	 */
	@Column(name = "QTD_FOLHAS")
	public Integer getQuantidadeFolhas() {
		return quantidadeFolhas;
	}

	public void setQuantidadeFolhas(Integer quantidadeFolhas) {
		this.quantidadeFolhas = quantidadeFolhas;
	}

	/**
	 * Flag que indica se o processo pode ser baixado. Indica se existe petições
	 * pendentes de julgamento que bloqueiam a baixa.
	 */
	@Column(name = "FLG_BAIXA")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getBaixa() {
		return baixa;
	}

	public void setBaixa(Boolean baixa) {
		this.baixa = baixa;
	}

	/**
	 * Quantidade de documentos que são juntados ao processo.
	 */
	@Column(name = "QTD_JUNTADA_LINHA")
	public Short getQuantidadeJuntadasLinha() {
		return quantidadeJuntadasLinha;
	}

	public void setQuantidadeJuntadasLinha(Short quantidadeJuntadasLinha) {
		this.quantidadeJuntadasLinha = quantidadeJuntadasLinha;
	}

	/**
	 * Flag utilizada para indicar se o réu já está preso para extradição.
	 */
	@Column(name = "FLG_REU_PRESO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getReuPreso() {
		return reuPreso;
	}

	public void setReuPreso(Boolean reuPreso) {
		this.reuPreso = reuPreso;
	}

	/**
	 * Classe processual.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIG_CLASSE_PROCES", updatable = false, insertable = false)
	public Classe getClasseProcessual() {
		return classeProcessual;
	}

	public void setClasseProcessual(Classe classeProcessual) {
		this.classeProcessual = classeProcessual;
	}

	@Column(name = "SIG_CLASSE_PROCES")
	public String getSiglaClasseProcessual() {
		return siglaClasseProcessual;
	}

	public void setSiglaClasseProcessual(String siglaClasseProcessual) {
		this.siglaClasseProcessual = siglaClasseProcessual;
	}

	/**
	 * Numero do processo.
	 */
	@Column(name = "NUM_PROCESSO")
	public Long getNumeroProcessual() {
		return numeroProcessual;
	}

	public void setNumeroProcessual(Long numeroProcessual) {
		this.numeroProcessual = numeroProcessual;
	}

	/**
	 * Quantidade de cópias do processo. Campo alfanumérico, podendo receber
	 * valores não numéricos.
	 * 
	 * O conceito de contra-fé é a identificação da quantidade de cópias do
	 * processo.
	 */
	@Column(name = "DSC_CONTRA_FE")
	public String getDescricaoContraFe() {
		return descricaoContraFe;
	}

	public void setDescricaoContraFe(String descricaoContraFe) {
		this.descricaoContraFe = descricaoContraFe;
	}

	/**
	 * Identificação da correspondência entregue ao STF.
	 */
	@Column(name = "DSC_IDENTIFICA_CORRESPONDENCIA")
	public String getDescricaoIdentificaCorrespondencia() {
		return descricaoIdentificaCorrespondencia;
	}

	public void setDescricaoIdentificaCorrespondencia(
			String descricaoIdentificaCorrespondencia) {
		this.descricaoIdentificaCorrespondencia = descricaoIdentificaCorrespondencia;
	}

	/**
	 * Coluna alimentada por uma trigger de auditoria com a data que o registro
	 * foi incluido. Alimentada uma única vez na inclusão do registro.
	 */
	@Column(name = "DSC_OBSERVACAO")
	public String getDescricaoObservacao() {
		return descricaoObservacao;
	}

	public void setDescricaoObservacao(String descricaoObservacao) {
		this.descricaoObservacao = descricaoObservacao;
	}

	/**
	 * Numeração única de processos no âmbito do Poder Judiciário, observada a
	 * estrutura NNNNNNN-DD.AAAA.J.TR.OOOO, composta de 6 (seis) campos
	 * obrigatórios, nos termos da tabela padronizada do CNJ.
	 */
	@Column(name = "NUM_UNICO_PROCESSO")
	public String getNumeroUnicoProcesso() {
		return numeroUnicoProcesso;
	}

	public void setNumeroUnicoProcesso(String numeroUnicoProcesso) {
		this.numeroUnicoProcesso = numeroUnicoProcesso;
	}

	/**
	 * Data em que o processo foi autuado. A autuação consiste no registro do
	 * processo no sistema depois de verificações nas informações providas pelo
	 * Protocolo. Por essa razão, quando essa data é atribuída na tabela de
	 * Processos, a aplicação atualiza o atributo na tabela de
	 * Processo_Protocolados.
	 */
	@Column(name = "DAT_AUTUACAO")
	public Date getDataAutuacao() {
		return dataAutuacao;
	}

	public void setDataAutuacao(Date dataAutuacao) {
		this.dataAutuacao = dataAutuacao;
	}

	/**
	 * Armazena SIM para processos com repercussão geral NÃO para processos que
	 * não tem repercussão geral. Será alimentada no momento da autuação do
	 * processo.
	 */
	@Column(name = "FLG_REPERCUSSAO_GERAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getRepercussaoGeral() {
		return repercussaoGeral;
	}

	public void setRepercussaoGeral(Boolean repercussaoGeral) {
		this.repercussaoGeral = repercussaoGeral;
	}

	/**
	 * Situação do Processo.
	 */
	@Column(name = "COD_SITUACAO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.SituacaoProcesso"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public SituacaoProcesso getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoProcesso situacao) {
		this.situacao = situacao;
	}

	/**
	 * Setor de Recebimento.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR_RECEBIMENTO")
	public Setor getSetorRecebimento() {
		return setorRecebimento;
	}

	public void setSetorRecebimento(Setor setorRecebimento) {
		this.setorRecebimento = setorRecebimento;
	}

	/**
	 * Tipo de meio do processo. Pode ser: F = Físico; E = Eletrônico.
	 */
	@Column(name = "TIP_MEIO_PROCESSO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoMeioProcesso"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public TipoMeioProcesso getTipoMeioProcesso() {
		return tipoMeioProcesso;
	}

	public void setTipoMeioProcesso(TipoMeioProcesso tipoMeioProcesso) {
		this.tipoMeioProcesso = tipoMeioProcesso;
	}

	/**
	 * Informa o tipo (modo) de recebimento do Processo.
	 */
	@Column(name = "TIP_RECEBIMENTO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoRecebimento"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public TipoRecebimento getTipoRecebimento() {
		return tipoRecebimento;
	}

	public void setTipoRecebimento(TipoRecebimento tipoRecebimento) {
		this.tipoRecebimento = tipoRecebimento;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "objetoIncidente")
	// @Transient
	public List<IncidentePreferencia> getIncidentePreferencia() {
		return incidentePreferencia;
	}

	public void setIncidentePreferencia(
			List<IncidentePreferencia> incidentePreferencia) {
		this.incidentePreferencia = incidentePreferencia;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_RELATOR_ATUAL", updatable = false, insertable = false)
	public Ministro getMinistroRelatorAtual() {
		return ministroRelatorAtual;
	}

	public void setMinistroRelatorAtual(Ministro ministroRelatorAtual) {
		this.ministroRelatorAtual = ministroRelatorAtual;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_RELATOR", updatable = false, insertable = false)
	public Ministro getMinistroRelator() {
			return ministroRelator;
	}

	public void setMinistroRelator(Ministro ministroRelator) {
		this.ministroRelator = ministroRelator;
	}
	
	/**
	 * Processo de Repercussão Geral.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "SIG_CLASSE_REPERCUSSAO_GERAL", referencedColumnName = "SIG_CLASSE_PROCES"),
			@JoinColumn(name = "NUM_PROCESSO_REPERCUSSAO_GERAL", referencedColumnName = "NUM_PROCESSO") })
	public Processo getProcessoRepercussaoGeral() {
		return processoRepercussaoGeral;
	}

	public void setProcessoRepercussaoGeral(Processo processoRepercussaoGeral) {
		this.processoRepercussaoGeral = processoRepercussaoGeral;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "objetoIncidente")
	public List<LegislacaoProcesso> getLegislacaoProcesso() {
		return legislacaoProcesso;
	}

	public void setLegislacaoProcesso(
			List<LegislacaoProcesso> legislacaoProcesso) {
		this.legislacaoProcesso = legislacaoProcesso;
	}

	@ManyToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(schema = "STF", name = "ASSUNTO_PROCESSO", joinColumns = @JoinColumn(name = "SEQ_OBJETO_INCIDENTE"), inverseJoinColumns = @JoinColumn(name = "COD_ASSUNTO"))
	@OrderBy(clause = "NUM_ORDEM")
	public List<Assunto> getAssuntos() {
		return assuntos;
	}

	public void setAssuntos(List<Assunto> assuntos) {
		this.assuntos = assuntos;
	}

	@Transient
	public List<Processo> getApensos(Processo processo) {
		return apensos;
	}

	/**
	 * Identificação do processo no STF.
	 */
	@Transient
	public String getIdentificacao() {
		return ObjetoIncidenteUtil.getIdentificacao(siglaClasseProcessual,
				numeroProcessual);
	}

	@Transient
	@Override
	public String getIdentificacaoCompleta() {
		return ObjetoIncidenteUtil.getIdentificacao(getClasseProcessual()
				.getDescricao(), numeroProcessual);
	}

	@Transient
	private String getNumeroProcessualFormatado() {
		return NumberFormat.getInstance().format(getNumeroProcessual());
	}

	@Transient
	public Boolean getPossuiLiminar() {
		if (incidentePreferencia != null && incidentePreferencia.size() > 0) {
			for (IncidentePreferencia tipo : incidentePreferencia) {
				if (tipo.getTipoPreferencia()
						.getId()
						.equals(TipoIncidentePreferencia.TipoPreferenciaContante.MEDIDA_LIMINAR
								.getCodigo())) {
					return Boolean.TRUE;
				}
			}
		}

		return Boolean.FALSE;
	}

	@Transient
	public Boolean getPossuiTutelaProvisoria() {
		if (incidentePreferencia != null && incidentePreferencia.size() > 0) {
			for (IncidentePreferencia tipo : incidentePreferencia) {
				if (tipo.getTipoPreferencia().getId()==41) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	@Transient
	public Boolean getPossuiCovid() {
		if (incidentePreferencia != null && incidentePreferencia.size() > 0) {
			for (IncidentePreferencia tipo : incidentePreferencia) {
				if (tipo.getTipoPreferencia().getId()==16) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	
	
	@Transient
	public Boolean getIsEletronico() {
		return TipoMeioProcesso.ELETRONICO.equals(getTipoMeioProcesso());
	}

	@Transient
	public String getDescricao() {
		return "Mérito";
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "objetoIncidente")
	public List<ReferenciaPrescricao> getReferenciaPrescricao() {
		return referenciaPrescricao;
	}

	public void setReferenciaPrescricao(
			List<ReferenciaPrescricao> referenciaPrescricao) {
		this.referenciaPrescricao = referenciaPrescricao;
	}

	@Formula("(SELECT COUNT(PT.SEQ_VINCULO_PROCESSO_TEMA) FROM JUDICIARIO.VINCULO_PROCESSO_TEMA PT, JUDICIARIO.TEMA T  WHERE "
			+ " T.cod_tipo_tema = 2 AND PT.seq_tema = T.seq_tema AND PT.seq_objeto_incidente = SEQ_OBJETO_INCIDENTE)")
	public Long getQuantidadeRepresentativoControversia() {
		return quantidadeRepresentativoControversia;
	}

	public void setQuantidadeRepresentativoControversia(
			Long quantidadeRepresentativoControversia) {
		this.quantidadeRepresentativoControversia = quantidadeRepresentativoControversia;
	}

	@Formula("(SELECT COUNT(PT.SEQ_VINCULO_PROCESSO_TEMA) FROM JUDICIARIO.VINCULO_PROCESSO_TEMA PT, JUDICIARIO.TEMA T  WHERE "
			+ " T.cod_tipo_tema = 3 AND PT.seq_tema = T.seq_tema AND PT.seq_objeto_incidente = SEQ_OBJETO_INCIDENTE)")
	public Long getQuantidadePreTema() {
		return quantidadePreTema;
	}

	public void setQuantidadePreTema(Long quantidadePreTema) {
		this.quantidadePreTema = quantidadePreTema;
	}
	
	@Column(name = "FLG_TRAMITACAO", updatable = false)
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getFiltroEmTramitacao() {		
		return filtroEmTramitacao;
	}

	public void setFiltroEmTramitacao(Boolean filtroEmTramitacao) {
		this.filtroEmTramitacao = filtroEmTramitacao;
	}

	@Column(name = "FLG_MATERIA_CONSTITUCIONAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getMateriaConstitucional() {	
		if(materiaConstitucional == null){
			return Boolean.FALSE;
		}
		return materiaConstitucional;
	}
	
	public void setMateriaConstitucional(Boolean materiaConstitucional) {
		this.materiaConstitucional = materiaConstitucional;
	}	

	@Column(name = "FLG_REPR_CONTROVERSIA")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getRepresentativoControversiaIndicadoOrigem() {
		return representativoControversiaIndicadoOrigem;
	}

	public void setRepresentativoControversiaIndicadoOrigem(Boolean representativoControversiaIndicadoOrigem) {
		this.representativoControversiaIndicadoOrigem = representativoControversiaIndicadoOrigem;
	}

	
	@Transient
	public boolean isEletronico() {
		return getTipoMeioProcesso().getCodigo().equals("E");
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "objetoIncidente")
	public List<HistoricoProcessoOrigem> getHistoricoProcessoOrigem() {
		return historicoProcessoOrigem;
	}

	public void setHistoricoProcessoOrigem(List<HistoricoProcessoOrigem> historicoProcessoOrigem) {
		this.historicoProcessoOrigem = historicoProcessoOrigem;
	}
	
	@Transient
	public Origem getOrigemPrincipal() {
		for (HistoricoProcessoOrigem hpo : getHistoricoProcessoOrigem()) {
			if (Boolean.TRUE.equals(hpo.getPrincipal()))
				return hpo.getOrigem();
		}
		
		return null;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(schema="JULGAMENTO", name="PROCESSO_TEMA", joinColumns=@JoinColumn(name="SEQ_OBJETO_INCIDENTE"), inverseJoinColumns=@JoinColumn(name="SEQ_TEMA"))
	public List<Tema> getTema() {
		return tema;
	}
	
	public void setTema(List<Tema> tema) {
		this.tema = tema;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(schema="JUDICIARIO", name="PROCESSO_MOTIVO_INAPTIDAO", joinColumns=@JoinColumn(name="SEQ_OBJETO_INCIDENTE"), inverseJoinColumns=@JoinColumn(name="COD_MOTIVO_INAPTIDAO"))
	public List<MotivoInaptidao> getMotivoInaptidao() {
		return motivoInaptidao;
	}

	public void setMotivoInaptidao(List<MotivoInaptidao> motivoInaptidao) {
		this.motivoInaptidao = motivoInaptidao;
	}
	
}

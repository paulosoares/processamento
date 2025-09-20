package br.gov.stf.estf.entidade.localizacao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.BooleanUtils;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "STF", name = "SETORES")
public class Setor extends ESTFBaseEntity<Long> {

	public static final Long CODIGO_SETOR_PRESIDENCIA = 600000179L;
	public static final Long CODIGO_SETOR_SECRETARIA_GERAL_PRESIDENCIA = 600000181L;
	public static final Long CODIGO_SETOR_PLANTAO_PRESIDENCIA = 600000916L;
	public static final Long CODIGO_SETOR_VICE_PRESIDENCIA = 600000716L;
	public static final Long CODIGO_SETOR_SECRETARIA_SESSOES = 600000645L;
	public static final Long CODIGO_SETOR_COORDENADORIA_ANALISE_JURISPRUDENCIA = 600000772L;
	public static final Long CODIGO_SETOR_COORDENADORIA_SESSOES_PRIMEIRA_TURMA = 600000648L;
	public static final Long CODIGO_SETOR_COORDENADORIA_SESSOES_SEGUNDA_TURMA = 600000649L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_AYRES_BRITTO = 600000154L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_CARMEN_LUCIA = 600000453L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_CELSO_MELLO = 600000003L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_EROS_GRAU = 600000060L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_ELLEN_GRACIE = 600000149L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_GILMAR_MENDES = 600000151L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_CEZAR_PELUSO = 600000153L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_CARLOS_BRITTO = 600000154L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_DIAS_TOFFOLI = 600000616L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_JOAQUIM_BARBOSA = 600000155L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_LUIZ_FUX = 600000681L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_MARCO_AURELIO = 600000156L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_MENEZES_DIREITO = 600000506L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_RICARDO_LEWANDOWSKI = 600000435L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_ROSA_WEBER = 600000683L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_TEORI = 600000793L;
	public static final Long CODIGO_SETOR_GABINETE_MINISTRO_ROBERTO_BARROSO = 600000795L;
	public static final Long CODIGO_SETOR_RECURSO_EXTRAORDINARIO = 600000898L;
	public static final Long CODIGO_SETOR_AGRAVO_DE_INSTRUMENTO = 600000642L;
	public static final Long CODIGO_SETOR_COORDENADORIA_APOIO_TECNICO = 600000633L;
	public static final Long CODIGO_SETOR_SECAO_COMUNICACOES = 600000901L;
	public static final Long CODIGO_SETOR_SECAO_PROCESSOS_CONTROLE_CONCENTRADO_RECLAMACOES = 600000897L;
	public static final Long CODIGO_SETOR_SECAO_PROCESSOS_ORIGINARIOS_CRIMINAIS = 600000907L;
	public static final Long CODIGO_SETOR_SECAO_PROCESSOS_DIVERSOS = 600000896L;
	public static final Long CODIGO_SETOR_SECAO_RECURSOS_CRIMINAIS = 600000908L;
	public static final Long CODIGO_SETOR_SECAO_RECURSOS_EXTRAORDINARIOS = 600000898L;
	public static final Long CODIGO_SETOR_SECAO_RE_PRIMEIRA_TURMA = 600000324L;
	public static final Long CODIGO_SETOR_SECAO_RE_SEGUNDA_TURMA = 600000328L;
	public static final Long CODIGO_GABINETE_SECRETARIA_JUDICIARIA = 600000627L;
	public static final Long CODIGO_NUCLEO_ANALISE_AGRAVOS_REGIMENTAIS = 600000568L;
	public static final Long CODIGO_SETOR_BAIXA_EXPEDICAO = 600000902L;
	public static final Long CODIGO_SETOR_TELECOMUNICACOES = 600000676L;
	public static final Long CODIGO_SETOR_PGR = 23L;
	public static final Long CODIGO_SETOR_AGU = 26L;
	public static final Long CODIGO_SECAO_DE_SISTEMAS_DE_GABINETES_E_JULGAMENTOS = 600000489L;
	public static final Long CODIGO_SETOR_GERENCIA_PROCESSOS_ORIGINARIOS_CRIMINAIS = 600000907L;
	
	public static final Long CODIGO_COORDENADORIA_DE_PROCESSOS_CRIMINAIS = 600000630L;
	public static final Long CODIGO_COORDENADORIA_DE_PROCESSOS_ORIGINARIOS = 600000631L;
	public static final Long CODIGO_COORDENADORIA_DE_RECURSOS = 600000632L;
	public static final Long CODIGO_ACERVO_ELETRONICO_INATIVO = 600000857L;

	/** @deprecated Setor inativado em 26.04.2012, devido a nova estruturação.
	 * @see #CODIGO_SECAO_DE_COMPOSIÇAO_E_CONTROLE_DE_ACORDAOS */
	public static final Long CODIGO_SETOR_COMPOSICAO_ACORDAOS = 600000652L;
	/** @deprecated Setor inativado em 26.04.2012, devido a nova estruturação. 
	 * @see #CODIGO_SECAO_DE_COMPOSIÇAO_E_CONTROLE_DE_ACORDAOS */
	public static final Long CODIGO_SETOR_CONTROLE_ACORDAOS = 600000651L;
	/** @deprecated Setor inativado em 26.04.2012, devido a nova estruturação. 
	 * @see #CODIGO_COORDENADORIA_DE_ACORDAOS */
	public static final Long CODIGO_SETOR_COORDENADORIA_ACORDAOS = 600000650L;

	/* Adicionados em 26.04.2012, devido a nova estruturação. */
	public static final Long CODIGO_COORDENADORIA_DE_ACORDAOS = 600000755L;
	public static final Long CODIGO_SECAO_DE_COMPOSIÇAO_E_CONTROLE_DE_ACORDAOS = 600000903L;
	public static final Long CODIGO_SECAO_DE_TRANSCRICAO_E_REVISAO_DE_JULGAMENTO = 600000900L;
	public static final Long CODIGO_SEÇAO_DE_AUDIO_E_VIDEO = 600000758L;
	public static final Long CODIGO_ASSESSORIA_DO_PLENARIO = 600000717L;
	public static final Long CODIGO_PRIM_TURMA_PRIMEIRA_TURMA = 600000714L;
	public static final Long CODIGO_SEG_TURMA_SEGUNDA_TURMA = 600000715L;
	public static final Long CODIGO_NUCLEO_REPERCUSSAO_GERAL = 600000860L;
	
	public static final Long CODIGO_SALA_DOS_OFICIAIS_DE_JUSTICA = 600000687L;
	public static final Long CODIGO_GERENCIA_EXPEDICAO_COMUNICACOES = 600000902L;
	public static final Long CODIGO_CERTIDOES_CARTORARIAS = 600000921L;

	private static final long serialVersionUID = 8599044001213458604L;
	
	private Long id;
	private String sigla;
	private String nome;
	private Boolean ativo;
	private Boolean flagSetor;
	private Boolean flagDeslocaProcesso;
	private Integer codigoCapitulo;
	private Long numeroProximaGuia;
	private Short numeroAnoGuia;
	private Setor pai;
	private boolean deslocaComunicacao;
	private Boolean flagDistribuicao;

	private List<TipoConfiguracaoSetor> tiposConfiguracao = new LinkedList<TipoConfiguracaoSetor>();

	@Id
	@Column(name = "COD_SETOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SIG_SETOR", unique = false, nullable = false, insertable = false, updatable = false)
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Column(name = "DSC_SETOR", unique = false, nullable = false, insertable = false, updatable = false)
	public String getNome() {
		if (BooleanUtils.isFalse(ativo)) {
			return nome + " (setor inativo)";
		} else {
			return nome;
		}
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "FLG_ATIVO")
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean flagAtivo) {
		this.ativo = flagAtivo;
	}

	@Column(name = "FLG_DESLOC_PROC")
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getDeslocaProcesso() {
		return flagDeslocaProcesso;
	}

	public void setDeslocaProcesso(Boolean flagDeslocaProcesso) {
		this.flagDeslocaProcesso = flagDeslocaProcesso;
	}

	@Column(name = "COD_CAPITULO", unique = false, nullable = true, insertable = true, updatable = true, precision = 9, scale = 0)
	public Integer getCodigoCapitulo() {
		return this.codigoCapitulo;
	}

	public void setCodigoCapitulo(Integer codigoCapitulo) {
		this.codigoCapitulo = codigoCapitulo;
	}

	@Column(name = "NUM_PROXIMA_GUIA")
	public Long getNumeroProximaGuia() {
		return numeroProximaGuia;
	}

	public void setNumeroProximaGuia(Long numeroProximaGuia) {
		this.numeroProximaGuia = numeroProximaGuia;
	}

	@Column(name = "NUM_ANO_GUIA")
	public Short getNumeroAnoGuia() {
		return numeroAnoGuia;
	}

	public void setNumeroAnoGuia(Short numeroAnoGuia) {
		this.numeroAnoGuia = numeroAnoGuia;
	}

	@Column(name = "FLG_SETOR")
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getFlagSetor() {
		return flagSetor;
	}

	public void setFlagSetor(Boolean flagSetor) {
		this.flagSetor = flagSetor;
	}

	@Override
	public boolean equals(Object setor) {
		if (setor instanceof Setor) {
			return getId().equals(((Setor) setor).getId());
		}
		return false;
	}

	@ManyToMany(targetEntity = br.gov.stf.estf.entidade.localizacao.TipoConfiguracaoSetor.class, cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(schema = "EGAB", name = "CONFIGURACAO_SETOR", joinColumns = { @JoinColumn(name = "COD_SETOR") }, inverseJoinColumns = { @JoinColumn(name = "SEQ_TIPO_CONFIGURACAO_SETOR") })
	public List<TipoConfiguracaoSetor> getTiposConfiguracao() {
		return tiposConfiguracao;
	}

	public void setTiposConfiguracao(List<TipoConfiguracaoSetor> tiposConfiguracao) {
		this.tiposConfiguracao = tiposConfiguracao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_PAI")
	public Setor getPai() {
		return pai;
	}

	public void setPai(Setor pai) {
		this.pai = pai;
	}
	
	@Column(name = "FLG_DESLOCA_COMUNICACAO")
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public boolean isDeslocaComunicacao() {
		return deslocaComunicacao;
	}

	public void setDeslocaComunicacao(boolean deslocaComunicacao) {
		this.deslocaComunicacao = deslocaComunicacao;
	}

	@Column(name = "FLG_DISTRIBUICAO_PROC")
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getFlagDistribuicao() {
		return flagDistribuicao;
	}
	
	public void setFlagDistribuicao(Boolean flagDistribuicao) {
		this.flagDistribuicao = flagDistribuicao;
	}
	
	@Transient
	public Boolean contemConfiguracao(String siglaTipoConfiguracao) {

		Boolean contem = Boolean.FALSE;

		if (siglaTipoConfiguracao != null && siglaTipoConfiguracao.trim().length() > 0)
			for (TipoConfiguracaoSetor tipoConfiguracaoTest : getTiposConfiguracao()) {
				if (siglaTipoConfiguracao.equalsIgnoreCase(tipoConfiguracaoTest.getSigla())) {
					contem = Boolean.TRUE;
					break;
				}
			}

		return contem;
	}

}

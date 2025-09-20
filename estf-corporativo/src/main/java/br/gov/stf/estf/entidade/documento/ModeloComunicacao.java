package br.gov.stf.estf.entidade.documento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento;

@Entity
@Table(name = "MODELO_COMUNICACAO", schema = "JUDICIARIO")
public class ModeloComunicacao extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 8410490011657182764L;
	private Long id;
	private ArquivoEletronico arquivoEletronico;
	private TipoComunicacao tipoComunicacao;
	private Andamento andamento;
	private Ministro ministro;
	private String dscModelo;
	private Setor setorDestino;
	private String flagAtivo;
	private TipoPermissaoModeloComunicacao tipoPermissao;
	private FlagGenericaModeloComunicacao flagPartes;
	private FlagGenericaModeloComunicacao flagAssinaturaMinistro;
	private FlagGenericaModeloComunicacao flagCampoLivre;
	private FlagGenericaModeloComunicacao flagVinculoPecaProcessoElet;
	private FlagGenericaModeloComunicacao flagDuasAssinaturas;
	private FlagGenericaModeloComunicacao flagProcessoLote;
	private FlagGenericaModeloComunicacao flagEncaminharParaDJe;
	private FlagGenericaModeloComunicacao flagAlterarObsAndamento;
	private FlagGenericaAcessoDocumento flagTipoAcessoDocumentoPeca; //flag para acesso as peças do documento
	private String textoModeloAviso;
	private FlagGenericaModeloComunicacao flagJuntadaPecaProc;
	private FlagGenericaModeloComunicacao flagSemAndamento;
	
	public static ModeloComunicacao INCLUIDO_NA_PAUTA = new ModeloComunicacao(9815L, "Processo incluído na Pauta", TipoComunicacao.NOTIFICACAO);
	public static ModeloComunicacao EXCLUIDO_DA_PAUTA = new ModeloComunicacao(9816L, "Processo excluído da Pauta", TipoComunicacao.NOTIFICACAO);
	public static ModeloComunicacao INCLUIDO_NO_CALENDARIO = new ModeloComunicacao(9817L, "Processo incluído no calendário de julgamento", TipoComunicacao.NOTIFICACAO);
	public static ModeloComunicacao INCLUIDO_NO_CALENDARIO_PELO_PRESIDENTE = new ModeloComunicacao(9818L, "Processo incluído no calendário de julgamento pelo Presidente", TipoComunicacao.NOTIFICACAO);

	public ModeloComunicacao() {
		super();
	}
	
	public ModeloComunicacao(Long id, String descricao, TipoComunicacao tipoComunicacao) {
		this.id = id;
		this.dscModelo = descricao;
		this.tipoComunicacao = tipoComunicacao;
	}

	@Id
	@Column(name = "SEQ_MODELO_COMUNICACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_MODELO_COMUNICACAO", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO", unique = false, nullable = true, insertable = true, updatable = true)
	public ArquivoEletronico getArquivoEletronico() {
		return arquivoEletronico;
	}

	public void setArquivoEletronico(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_COMUNICACAO", unique = false, nullable = false, insertable = true, updatable = true)
	public TipoComunicacao getTipoComunicacao() {
		return tipoComunicacao;
	}

	public void setTipoComunicacao(TipoComunicacao tipoComunicacao) {
		this.tipoComunicacao = tipoComunicacao;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO", unique = false, nullable = true, insertable = true, updatable = true)
	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	@Column(name = "DSC_MODELO", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 30)
	public String getDscModelo() {
		return dscModelo;
	}

	public void setDscModelo(String dscModelo) {
		this.dscModelo = dscModelo;
	}

	@Column(name = "FLG_MOSTRAR_PARTES")
	@Enumerated(EnumType.STRING)
	public FlagGenericaModeloComunicacao getFlagPartes() {
		return flagPartes;
	}

	public void setFlagPartes(FlagGenericaModeloComunicacao flagPartes) {
		this.flagPartes = flagPartes;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR_DESTINO", unique = false, nullable = true, insertable = true, updatable = true)
	public Setor getSetorDestino() {
		return setorDestino;
	}

	public void setSetorDestino(Setor setorDestino) {
		this.setorDestino = setorDestino;
	}

	@Column(name = "FLG_ASSINATURA_RELATOR")
	@Enumerated(EnumType.STRING)
	public FlagGenericaModeloComunicacao getFlagAssinaturaMinistro() {
		return flagAssinaturaMinistro;
	}

	public void setFlagAssinaturaMinistro(FlagGenericaModeloComunicacao flagAssinaturaMinistro) {
		this.flagAssinaturaMinistro = flagAssinaturaMinistro;
	}

	@Column(name = "FLG_ATIVO")
	@Enumerated(EnumType.STRING)
	public String getFlagAtivo() {
		return flagAtivo;
	}

	public void setFlagAtivo(String flagAtivo) {
		this.flagAtivo = flagAtivo;
	}

	@Column(name = "FLG_CAMPO_CUSTOMIZADO")
	@Enumerated(EnumType.STRING)
	public FlagGenericaModeloComunicacao getFlagCampoLivre() {
		return flagCampoLivre;
	}

	public void setFlagCampoLivre(FlagGenericaModeloComunicacao flagCampoLivre) {
		this.flagCampoLivre = flagCampoLivre;
	}

	@Column(name = "FLG_VINCULO_PECA_PROC")
	@Enumerated(EnumType.STRING)
	public FlagGenericaModeloComunicacao getFlagVinculoPecaProcessoElet() {
		return flagVinculoPecaProcessoElet;
	}

	public void setFlagVinculoPecaProcessoElet(FlagGenericaModeloComunicacao flagVinculoPecaProcessoElet) {
		this.flagVinculoPecaProcessoElet = flagVinculoPecaProcessoElet;
	}

	@Column(name = "FLG_ASSINATURA")
	@Enumerated(EnumType.STRING)
	public FlagGenericaModeloComunicacao getFlagDuasAssinaturas() {
		return flagDuasAssinaturas;
	}

	public void setFlagDuasAssinaturas(FlagGenericaModeloComunicacao flagDuasAssinaturas) {
		this.flagDuasAssinaturas = flagDuasAssinaturas;
	}

	@Column(name = "FLG_PROCESSO_LOTE")
	@Enumerated(EnumType.STRING)
	public FlagGenericaModeloComunicacao getFlagProcessoLote() {
		return flagProcessoLote;
	}

	public void setFlagProcessoLote(FlagGenericaModeloComunicacao flagProcessoLote) {
		this.flagProcessoLote = flagProcessoLote;
	}

	@Column(name = "FLG_UTILIZADO_DJ", length = 1)
	@Enumerated(EnumType.STRING)
	public FlagGenericaModeloComunicacao getFlagEncaminharParaDJe() {
		return flagEncaminharParaDJe;
	}

	public void setFlagEncaminharParaDJe(FlagGenericaModeloComunicacao flagEncaminharParaDJe) {
		this.flagEncaminharParaDJe = flagEncaminharParaDJe;
	}

	@Column(name = "TIP_ACESSO")
	@Enumerated(EnumType.STRING)
	public FlagGenericaAcessoDocumento getFlagTipoAcessoDocumentoPeca() {
		return flagTipoAcessoDocumentoPeca;
	}

	public void setFlagTipoAcessoDocumentoPeca(FlagGenericaAcessoDocumento flagTipoAcessoDocumentoPeca) {
		this.flagTipoAcessoDocumentoPeca = flagTipoAcessoDocumentoPeca;
	}
	
	@Column(name = "FLG_OBSERVACAO_ANDAMENTO")
	@Enumerated(EnumType.STRING)
	public FlagGenericaModeloComunicacao getFlagAlterarObsAndamento() {
		return flagAlterarObsAndamento;
	}

	public void setFlagAlterarObsAndamento(
			FlagGenericaModeloComunicacao flagAlterarObsAndamento) {
		this.flagAlterarObsAndamento = flagAlterarObsAndamento;
	}
	
	@Column(name = "TXT_MODELO_AVISO", length=4000)
	public String getTextoModeloAviso() {
		return textoModeloAviso;
	}
	
	public void setTextoModeloAviso(String textoModeloAviso) {
		this.textoModeloAviso = textoModeloAviso;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_PERMISSAO_MODELO", referencedColumnName = "SEQ_TIPO_PERMISSAO_MODELO", unique = false, nullable = false, insertable = true, updatable = true)
	public TipoPermissaoModeloComunicacao getTipoPermissao() {
		return tipoPermissao;
	}

	public void setTipoPermissao(TipoPermissaoModeloComunicacao tipoPermissao) {
		this.tipoPermissao = tipoPermissao;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_ANDAMENTO", referencedColumnName = "COD_ANDAMENTO", unique = false, insertable = true, updatable = true)
	public Andamento getAndamento() {
		return andamento;
	}

	public void setAndamento(Andamento andamento) {
		this.andamento = andamento;
	}

	@Column(name = "FLG_JUNTADA_PECA_PROC")
	@Enumerated(EnumType.STRING)
	public FlagGenericaModeloComunicacao getFlagJuntadaPecaProc() {
		return flagJuntadaPecaProc;
	}

	public void setFlagJuntadaPecaProc(FlagGenericaModeloComunicacao flagJuntadaPecaProc) {
		this.flagJuntadaPecaProc = flagJuntadaPecaProc;
	}

	@Column(name = "FLG_SEM_ANDAMENTO")
	@Enumerated(EnumType.STRING)
	public FlagGenericaModeloComunicacao getFlagSemAndamento() {
		return flagSemAndamento;
	}

	public void setFlagSemAndamento(FlagGenericaModeloComunicacao flagSemAndamento) {
		this.flagSemAndamento = flagSemAndamento;
	}
	
	@Transient
	public Boolean isProcessoLote() {
		return getFlagProcessoLote().equals(FlagGenericaModeloComunicacao.S);
	}

	@Transient
	public Boolean possuiPecasVinculadas() {
		return getFlagVinculoPecaProcessoElet().equals(FlagGenericaModeloComunicacao.S);
	}


	public enum FlagGenericaModeloComunicacao {
		N("Não"), S("Sim");

		private String descricao;

		FlagGenericaModeloComunicacao(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	};

	public enum FlagGenericaAcessoDocumento {
		P("Público"), I("Interno");

		private String descricao;

		FlagGenericaAcessoDocumento(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	};
}

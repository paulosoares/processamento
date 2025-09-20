package br.gov.stf.estf.assinatura.stfoffice.editor;

import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoPermissaoModeloComunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaModeloComunicacao;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.stfoffice.editor.web.requisicao.jnlp.RequisicaoJnlpDocumento;

public class RequisicaoAbrirModelo extends RequisicaoJnlpDocumento {

	private static final long serialVersionUID = 1L;

	
	private Andamento andamentoModelo;
	private TipoComunicacao tipoComunicacao;
	private TipoPermissaoModeloComunicacao tipoPermissaoModeloComunicacao;
	private Boolean modeloDocumento;
	private String nomeModelo;

	private ModeloComunicacao modeloComunicacao;
	private ArquivoEletronico arquivoEletronico;
	private String flagPartes;
	private String flagAssinaturaMinistro;
	private String flagPecaProcessoEletronico;
	private Long idSetorDestino;
	private String flgAtivo;
	private String flagCampoLivre;
	private String flagDuasAssinatura;
	private String flagProcessoLote;
	private String flagRestringePeca;
	private String flagEncaminharParaDJe;
	private String flagAlterarObsAndamento;
	private String flagJuntadaPecaProc;
	private String flagSemAndamento;

	public RequisicaoAbrirModelo() {
		super();
	}

	public RequisicaoAbrirModelo(ModeloComunicacao modeloComunicacao) {
		this.modeloComunicacao = modeloComunicacao;
	}

	public RequisicaoAbrirModelo(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}

	public RequisicaoAbrirModelo(Boolean modeloDocumento) {
		this.modeloDocumento = modeloDocumento;
	}

	public ArquivoEletronico getArquivoEletronico() {
		return arquivoEletronico;
	}

	public void setArquivoEletronico(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}

	public ModeloComunicacao getModeloComunicacao() {
		return modeloComunicacao;
	}

	public void setModeloComunicacao(ModeloComunicacao modeloComunicacao) {
		this.modeloComunicacao = modeloComunicacao;
	}

	public Boolean getModeloDocumento() {
		return modeloDocumento;
	}

	public void setModeloDocumento(Boolean modeloDocumento) {
		this.modeloDocumento = modeloDocumento;
	}

	public String getNomeModelo() {
		return nomeModelo;
	}

	public void setNomeModelo(String nomeModelo) {
		this.nomeModelo = nomeModelo;
	}

	public TipoPermissaoModeloComunicacao getTipoPermissaoModeloComunicacao() {
		return tipoPermissaoModeloComunicacao;
	}

	public void setTipoPermissaoModeloComunicacao(
			TipoPermissaoModeloComunicacao tipoPermissaoModeloComunicacao) {
		this.tipoPermissaoModeloComunicacao = tipoPermissaoModeloComunicacao;
	}

	public String getFlagPartes() {
		return flagPartes;
	}

	public void setFlagPartes(String flagPartes) {
		this.flagPartes = flagPartes;
	}

	public String getFlagAssinaturaMinistro() {
		return flagAssinaturaMinistro;
	}

	public void setFlagAssinaturaMinistro(String flagAssinaturaMinistro) {
		this.flagAssinaturaMinistro = flagAssinaturaMinistro;
	}

	public Long getIdSetorDestino() {
		return idSetorDestino;
	}

	public void setIdSetorDestino(Long idSetorDestino) {
		this.idSetorDestino = idSetorDestino;
	}

	public String getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(String flgAtivo) {
		this.flgAtivo = flgAtivo;
	}

	public String getFlagCampoLivre() {
		return flagCampoLivre;
	}

	public void setFlagCampoLivre(String flagCampoLivre) {
		this.flagCampoLivre = flagCampoLivre;
	}

	public String getFlagPecaProcessoEletronico() {
		return flagPecaProcessoEletronico;
	}

	public void setFlagPecaProcessoEletronico(String flagPecaProcessoEletronico) {
		this.flagPecaProcessoEletronico = flagPecaProcessoEletronico;
	}

	public String getFlagDuasAssinatura() {
		return flagDuasAssinatura;
	}

	public void setFlagDuasAssinatura(String flagDuasAssinatura) {
		this.flagDuasAssinatura = flagDuasAssinatura;
	}

	public String getFlagRestringePeca() {
		return flagRestringePeca;
	}

	public void setFlagRestringePeca(String flagRestringePeca) {
		this.flagRestringePeca = flagRestringePeca;
	}

	public String getFlagProcessoLote() {
		return flagProcessoLote;
	}

	public void setFlagProcessoLote(String flagProcessoLote) {
		this.flagProcessoLote = flagProcessoLote;
	}

	public String getFlagEncaminharParaDJe() {
		return flagEncaminharParaDJe;
	}

	public void setFlagEncaminharParaDJe(String flagEncaminharParaDJe) {
		this.flagEncaminharParaDJe = flagEncaminharParaDJe;
	}

	public Andamento getAndamentoModelo() {
		return andamentoModelo;
	}

	public void setAndamentoModelo(Andamento andamentoModelo) {
		this.andamentoModelo = andamentoModelo;
	}

	public TipoComunicacao getTipoComunicacao() {
		return tipoComunicacao;
	}

	public void setTipoComunicacao(TipoComunicacao tipoComunicacao) {
		this.tipoComunicacao = tipoComunicacao;
	}

	public String getFlagAlterarObsAndamento() {
		return flagAlterarObsAndamento;
	}

	public void setFlagAlterarObsAndamento(String flagAlterarObsAndamento) {
		this.flagAlterarObsAndamento = flagAlterarObsAndamento;
	}

	@Override
	public String toString() {
		String toString = "RequisicaoNovoModelo";

		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomeModelo == null) ? 0 : nomeModelo.hashCode());
		result = prime * result + ((modeloComunicacao == null) ? 0 : modeloComunicacao.hashCode());

		return toString + result;
	}

	public String getFlagJuntadaPecaProc() {
		return flagJuntadaPecaProc;
	}

	public void setFlagJuntadaPecaProc(String flagJuntadaPecaProc) {
		this.flagJuntadaPecaProc = flagJuntadaPecaProc;
	}

	public String getFlagSemAndamento() {
		return flagSemAndamento;
	}

	public void setFlagSemAndamento(String flagSemAndamento) {
		this.flagSemAndamento = flagSemAndamento;
	}
}

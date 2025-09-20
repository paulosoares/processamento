package br.gov.stf.estf.julgamento.model.util;

import java.util.Date;

import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoJulgamentoVirtual;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoSessaoConstante;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoJulgamento;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.framework.util.SearchData;

public class ListaJulgamentoSearchData extends SearchData{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long codigoMinistroRelator;
	private String idSessao;
	private Long numeroSessao;
	private Short anoSessao;
	private Date dataInicioSessao;
	private Date dataFimSessao;
	private Date dataPrevistaInicioSessao;
	private Date dataPrevistaFimSessao;
	private Date dataSessao;
	private TipoSessaoConstante tipoSessao;	
	private Boolean primeiraTurma;
	private Boolean segundaTurma;
	private Boolean plenario;
	private TipoAmbienteConstante tipoAmbiente;
	private TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant tipoSituacaoJulgamento;
	private Long codigoMinistroParticipante;
	private Boolean votoPendente;
	private String siglaClasseProcessual;
	private Long numeroProcesso;
	private TipoJulgamentoVirtual tipoJulgamentoVirtual;
	private boolean pedidoVista;
	private boolean pedidoDestaque;
	private Ministro ministroLogado;
	private boolean sustentacaoOral;
	private boolean questaoFato;
	private Boolean julgamentoTese;
	private Boolean julgamentoModulacao;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCodigoMinistroRelator() {
		return codigoMinistroRelator;
	}
	public void setCodigoMinistroRelator(Long codigoMinistroRelator) {
		this.codigoMinistroRelator = codigoMinistroRelator;
	}
	public String getIdSessao() {
		return idSessao;
	}
	public void setIdSessao(String idSessao) {
		this.idSessao = idSessao;
	}
	public Long getNumeroSessao() {
		return numeroSessao;
	}
	public void setNumeroSessao(Long numeroSessao) {
		this.numeroSessao = numeroSessao;
	}
	public Short getAnoSessao() {
		return anoSessao;
	}
	public void setAnoSessao(Short anoSessao) {
		this.anoSessao = anoSessao;
	}
	public Date getDataInicioSessao() {
		return dataInicioSessao;
	}
	public void setDataInicioSessao(Date dataInicioSessao) {
		this.dataInicioSessao = dataInicioSessao;
	}
	public Date getDataFimSessao() {
		return dataFimSessao;
	}
	public void setDataFimSessao(Date dataFimSessao) {
		this.dataFimSessao = dataFimSessao;
	}
	public Date getDataPrevistaInicioSessao() {
		return dataPrevistaInicioSessao;
	}
	public void setDataPrevistaInicioSessao(Date dataPrevistaInicioSessao) {
		this.dataPrevistaInicioSessao = dataPrevistaInicioSessao;
	}
	public Date getDataPrevistaFimSessao() {
		return dataPrevistaFimSessao;
	}
	public void setDataPrevistaFimSessao(Date dataPrevistaFimSessao) {
		this.dataPrevistaFimSessao = dataPrevistaFimSessao;
	}
	public Date getDataSessao() {
		return dataSessao;
	}
	public void setDataSessao(Date dataSessao) {
		this.dataSessao = dataSessao;
	}
	public TipoSessaoConstante getTipoSessao() {
		return tipoSessao;
	}
	public void setTipoSessao(TipoSessaoConstante tipoSessao) {
		this.tipoSessao = tipoSessao;
	}
	public Boolean isPrimeiraTurma() {
		return primeiraTurma;
	}
	public void setPrimeiraTurma(Boolean primeiraTurma) {
		this.primeiraTurma = primeiraTurma;
	}
	public Boolean isSegundaTurma() {
		return segundaTurma;
	}
	public void setSegundaTurma(Boolean segundaTurma) {
		this.segundaTurma = segundaTurma;
	}
	public Boolean isPlenario() {
		return plenario;
	}
	public void setPlenario(Boolean plenario) {
		this.plenario = plenario;
	}
	public TipoAmbienteConstante getTipoAmbiente() {
		return tipoAmbiente;
	}
	public void setTipoAmbiente(TipoAmbienteConstante tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
	}
	public TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant getTipoSituacaoJulgamento() {
		return tipoSituacaoJulgamento;
	}
	public void setTipoSituacaoJulgamento(TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant tipoSituacaoJulgamento) {
		this.tipoSituacaoJulgamento = tipoSituacaoJulgamento;
	}
	public Long getCodigoMinistroParticipante() {
		return codigoMinistroParticipante;
	}
	public void setCodigoMinistroParticipante(Long codigoMinistroParticipante) {
		this.codigoMinistroParticipante = codigoMinistroParticipante;
	}
	public Boolean isVotoPendente() {
		return votoPendente;
	}
	public void setVotoPendente(Boolean votoPendente) {
		this.votoPendente = votoPendente;
	}
	public String getSiglaClasseProcessual() {
		return siglaClasseProcessual;
	}
	public void setSiglaClasseProcessual(String siglaClasseProcessual) {
		this.siglaClasseProcessual = siglaClasseProcessual;
	}
	public Long getNumeroProcesso() {
		return numeroProcesso;
	}
	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}
	public TipoJulgamentoVirtual getTipoJulgamentoVirtual() {
		return tipoJulgamentoVirtual;
	}
	public void setTipoJulgamentoVirtual(TipoJulgamentoVirtual tipoJulgamentoVirtual) {
		this.tipoJulgamentoVirtual = tipoJulgamentoVirtual;
	}
	public boolean isPedidoVista() {
		return pedidoVista;
	}
	public void setPedidoVista(boolean pedidoVista) {
		this.pedidoVista = pedidoVista;
	}
	public boolean isPedidoDestaque() {
		return pedidoDestaque;
	}
	public void setPedidoDestaque(boolean pedidoDestaque) {
		this.pedidoDestaque = pedidoDestaque;
	}
	public boolean isSustentacaoOral() {
		return sustentacaoOral;
	}
	public void setSustentacaoOral(boolean sustentacaoOral) {
		this.sustentacaoOral = sustentacaoOral;
	}
	public Ministro getMinistroLogado() {
		return ministroLogado;
	}
	public void setMinistroLogado(Ministro ministroLogado) {
		this.ministroLogado = ministroLogado;
	}
	public boolean isQuestaoFato() {
		return questaoFato;
	}
	public void setQuestaoFato(boolean questaoFato) {
		this.questaoFato = questaoFato;
	}
	public Boolean getJulgamentoTese() {
		return julgamentoTese;
	}
	public void setJulgamentoTese(Boolean julgamentoTese) {
		this.julgamentoTese = julgamentoTese;
	}
	public Boolean getJulgamentoModulacao() {
		return julgamentoModulacao;
	}
	public void setJulgamentoModulacao(Boolean julgamentoModulacao) {
		this.julgamentoModulacao = julgamentoModulacao;
	}			
}

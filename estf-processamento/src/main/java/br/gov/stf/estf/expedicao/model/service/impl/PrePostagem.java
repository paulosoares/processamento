package br.gov.stf.estf.expedicao.model.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class PrePostagem {
	private String id;
	private String idCorreios;
	private String numeroCartaoPostagem;
	private String codigoObjeto;
	private String codigoServico;
	private Set<ServicoAdicional> listaServicoAdicional;
	private Integer modalidadePagamento;
	private BigDecimal precoServico;
	private BigDecimal precoPrePostagem;
	private String numeroNotaFiscal;
	private String chaveNFe;
	private Set<ItemDeclaracaoConteudo> itensDeclaracaoConteudo;
	private String pesoInformado;
	private String codigoFormatoObjetoInformado;
	private String alturaInformada;
	private String larguraInformada;
	private String comprimentoInformado;
	private String diametroInformado;
	private String ncmObjeto;
	private String rfidObjeto;
	private Integer cienteObjetoNaoProibido;
	private String idAtendimento;
	private String solicitarColeta = "N";
	private String nuColeta;
	private Date dataPrevistaPostagem;
	private String eticket;
	private Date dataEticket;
	private String checkList;
	private String controleCliente;
	private String observacao;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdCorreios() {
		return idCorreios;
	}

	public void setIdCorreios(String idCorreios) {
		this.idCorreios = idCorreios;
	}

	public String getNumeroCartaoPostagem() {
		return numeroCartaoPostagem;
	}

	public void setNumeroCartaoPostagem(String numeroCartaoPostagem) {
		this.numeroCartaoPostagem = numeroCartaoPostagem;
	}

	public String getCodigoObjeto() {
		return codigoObjeto;
	}

	public void setCodigoObjeto(String codigoObjeto) {
		this.codigoObjeto = codigoObjeto;
	}

	public String getCodigoServico() {
		return codigoServico;
	}

	public void setCodigoServico(String codigoServico) {
		this.codigoServico = codigoServico;
	}

	public Set<ServicoAdicional> getListaServicoAdicional() {
		return listaServicoAdicional;
	}

	public void setListaServicoAdicional(Set<ServicoAdicional> listaServicoAdicional) {
		this.listaServicoAdicional = listaServicoAdicional;
	}

	public Integer getModalidadePagamento() {
		return modalidadePagamento;
	}

	public void setModalidadePagamento(Integer modalidadePagamento) {
		this.modalidadePagamento = modalidadePagamento;
	}

	public BigDecimal getPrecoServico() {
		return precoServico;
	}

	public void setPrecoServico(BigDecimal precoServico) {
		this.precoServico = precoServico;
	}

	public BigDecimal getPrecoPrePostagem() {
		return precoPrePostagem;
	}

	public void setPrecoPrePostagem(BigDecimal precoPrePostagem) {
		this.precoPrePostagem = precoPrePostagem;
	}

	public String getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(String numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	public String getChaveNFe() {
		return chaveNFe;
	}

	public void setChaveNFe(String chaveNFe) {
		this.chaveNFe = chaveNFe;
	}

	public Set<ItemDeclaracaoConteudo> getItensDeclaracaoConteudo() {
		return itensDeclaracaoConteudo;
	}

	public void setItensDeclaracaoConteudo(Set<ItemDeclaracaoConteudo> itensDeclaracaoConteudo) {
		this.itensDeclaracaoConteudo = itensDeclaracaoConteudo;
	}

	public String getPesoInformado() {
		return pesoInformado;
	}

	public void setPesoInformado(String pesoInformado) {
		this.pesoInformado = pesoInformado;
	}

	public String getCodigoFormatoObjetoInformado() {
		return codigoFormatoObjetoInformado;
	}

	public void setCodigoFormatoObjetoInformado(String codigoFormatoObjetoInformado) {
		this.codigoFormatoObjetoInformado = codigoFormatoObjetoInformado;
	}

	public String getAlturaInformada() {
		return alturaInformada;
	}

	public void setAlturaInformada(String alturaInformada) {
		this.alturaInformada = alturaInformada;
	}

	public String getLarguraInformada() {
		return larguraInformada;
	}

	public void setLarguraInformada(String larguraInformada) {
		this.larguraInformada = larguraInformada;
	}

	public String getComprimentoInformado() {
		return comprimentoInformado;
	}

	public void setComprimentoInformado(String comprimentoInformado) {
		this.comprimentoInformado = comprimentoInformado;
	}

	public String getDiametroInformado() {
		return diametroInformado;
	}

	public void setDiametroInformado(String diametroInformado) {
		this.diametroInformado = diametroInformado;
	}

	public String getNcmObjeto() {
		return ncmObjeto;
	}

	public void setNcmObjeto(String ncmObjeto) {
		this.ncmObjeto = ncmObjeto;
	}

	public String getRfidObjeto() {
		return rfidObjeto;
	}

	public void setRfidObjeto(String rfidObjeto) {
		this.rfidObjeto = rfidObjeto;
	}

	public Integer getCienteObjetoNaoProibido() {
		return cienteObjetoNaoProibido;
	}

	public void setCienteObjetoNaoProibido(Integer cienteObjetoNaoProibido) {
		this.cienteObjetoNaoProibido = cienteObjetoNaoProibido;
	}

	public String getIdAtendimento() {
		return idAtendimento;
	}

	public void setIdAtendimento(String idAtendimento) {
		this.idAtendimento = idAtendimento;
	}

	public String getSolicitarColeta() {
		return solicitarColeta;
	}

	public void setSolicitarColeta(String solicitarColeta) {
		this.solicitarColeta = solicitarColeta;
	}

	public String getNuColeta() {
		return nuColeta;
	}

	public void setNuColeta(String nuColeta) {
		this.nuColeta = nuColeta;
	}

	public Date getDataPrevistaPostagem() {
		return dataPrevistaPostagem;
	}

	public void setDataPrevistaPostagem(Date dataPrevistaPostagem) {
		this.dataPrevistaPostagem = dataPrevistaPostagem;
	}

	public String getEticket() {
		return eticket;
	}

	public void setEticket(String eticket) {
		this.eticket = eticket;
	}

	public Date getDataEticket() {
		return dataEticket;
	}

	public void setDataEticket(Date dataEticket) {
		this.dataEticket = dataEticket;
	}

	public String getCheckList() {
		return checkList;
	}

	public void setCheckList(String checkList) {
		this.checkList = checkList;
	}

	public String getControleCliente() {
		return controleCliente;
	}

	public void setControleCliente(String controleCliente) {
		this.controleCliente = controleCliente;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}

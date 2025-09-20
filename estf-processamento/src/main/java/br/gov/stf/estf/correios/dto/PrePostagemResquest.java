package br.gov.stf.estf.correios.dto;

import java.util.List;
import java.util.Set;

import br.gov.stf.estf.expedicao.model.service.impl.ItemDeclaracaoConteudo;

public class PrePostagemResquest {
	private String cienteObjetoNaoProibido = "1";
	private String codigoFormatoObjetoInformado;
	private String codigoServico;
	private String pesoInformado;
	private String dataPrevistaPostagem;
	private List<ServicoAdicionalDto> listaServicoAdicional;
	private RemetenteDto remetente;
	private DestinatarioDto destinatario;
	private String alturaInformada;
	private String larguraInformada;
	private String comprimentoInformado;
	private String diametroInformado;
	private Set<ItemDeclaracaoConteudo> itensDeclaracaoConteudo;

	public String getCienteObjetoNaoProibido() {
		return cienteObjetoNaoProibido;
	}
	public void setCienteObjetoNaoProibido(String cienteObjetoNaoProibido) {
		this.cienteObjetoNaoProibido = cienteObjetoNaoProibido;
	}
	public String getCodigoFormatoObjetoInformado() {
		return codigoFormatoObjetoInformado;
	}
	public void setCodigoFormatoObjetoInformado(String codigoFormatoObjetoInformado) {
		this.codigoFormatoObjetoInformado = codigoFormatoObjetoInformado;
	}
	public String getCodigoServico() {
		return codigoServico;
	}
	public void setCodigoServico(String codigoServico) {
		this.codigoServico = codigoServico;
	}
	public String getPesoInformado() {
		return pesoInformado;
	}
	public void setPesoInformado(String pesoInformado) {
		this.pesoInformado = pesoInformado;
	}
	public String getDataPrevistaPostagem() {
		return dataPrevistaPostagem;
	}
	public void setDataPrevistaPostagem(String dataPrevistaPostagem) {
		this.dataPrevistaPostagem = dataPrevistaPostagem;
	}
	public List<ServicoAdicionalDto> getListaServicoAdicional() {
		return listaServicoAdicional;
	}
	public void setListaServicoAdicional(List<ServicoAdicionalDto> listaServicoAdicional) {
		this.listaServicoAdicional = listaServicoAdicional;
	}
	public RemetenteDto getRemetente() {
		return remetente;
	}
	public void setRemetente(RemetenteDto remetente) {
		this.remetente = remetente;
	}
	public DestinatarioDto getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(DestinatarioDto destinatario) {
		this.destinatario = destinatario;
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
	public Set<ItemDeclaracaoConteudo> getItensDeclaracaoConteudo() {
		return itensDeclaracaoConteudo;
	}
	public void setItensDeclaracaoConteudo(Set<ItemDeclaracaoConteudo> itensDeclaracaoConteudo) {
		this.itensDeclaracaoConteudo = itensDeclaracaoConteudo;
	}
	
	
}

package br.jus.stf.estf.decisao.objetoincidente.support;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;


/**
 * Classe utilizada para facilitar o reuso de metodos
 * @author Reinaldo.Barreto
 */
public class PreListaJulgamentoReportSupport {
	private String cabecalho;
	private String cabecalhoVistor;
	private String ministro;
	private String ministroVistor;
	private Long idMinistroVistor;
	private String numeroLista;
	private Long idListaJulgamento;
	private String sessao;
	private List<ObjetoIncidente<?>> listaProcessos;
	private String descricaoTipoListaJulgamento;
	
	private Boolean mostrarEmenta;
	private Boolean mostrarObservacao;
	private Boolean mostrarPartes;
	private Boolean mostrarVotoVista;
	
	private String tipoLista;
	
	public String getCabecalho() {
		return cabecalho;
	}
	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}
	public String getMinistro() {
		return ministro;
	}
	public void setMinistro(String ministro) {
		this.ministro = ministro;
	}
	public String getNumeroLista() {
		return numeroLista;
	}
	public void setNumeroLista(String numeroLista) {
		this.numeroLista = numeroLista;
	}
	public String getSessao() {
		return sessao;
	}
	public void setSessao(String sessao) {
		this.sessao = sessao;
	}
	public List<ObjetoIncidente<?>> getListaProcessos() {
		return listaProcessos;
	}
	public void setListaProcessos(List<ObjetoIncidente<?>> listaProcessos2) {
		this.listaProcessos = listaProcessos2;
	}
	public Boolean getMostrarEmenta() {
		return mostrarEmenta;
	}
	public void setMostrarEmenta(Boolean mostrarEmenta) {
		this.mostrarEmenta = mostrarEmenta;
	}
	public Boolean getMostrarObservacao() {
		return mostrarObservacao;
	}
	public void setMostrarObservacao(Boolean mostrarObservacao) {
		this.mostrarObservacao = mostrarObservacao;
	}
	public Boolean getMostrarPartes() {
		return mostrarPartes;
	}
	public void setMostrarPartes(Boolean mostrarPartes) {
		this.mostrarPartes = mostrarPartes;
	}
	public String getTipoLista() {
		return tipoLista;
	}
	public void setTipoLista(String tipoLista) {
		this.tipoLista = tipoLista;
	}
	public Long getIdListaJulgamento() {
		return idListaJulgamento;
	}
	public void setIdListaJulgamento(Long idListaJulgamento) {
		this.idListaJulgamento = idListaJulgamento;
	}
	public String getDescricaoTipoListaJulgamento() {
		return descricaoTipoListaJulgamento;
	}
	public void setDescricaoTipoListaJulgamento(String descricaoTipoListaJulgamento) {
		this.descricaoTipoListaJulgamento = descricaoTipoListaJulgamento;
	}
	public String getMinistroVistor() {
		return ministroVistor;
	}
	public void setMinistroVistor(String ministroVistor) {
		this.ministroVistor = ministroVistor;
	}
	public Boolean getMostrarVotoVista() {
		return mostrarVotoVista;
	}
	public void setMostrarVotoVista(Boolean mostrarVotoVista) {
		this.mostrarVotoVista = mostrarVotoVista;
	}
	public Long getIdMinistroVistor() {
		return idMinistroVistor;
	}
	public void setIdMinistroVistor(Long idMinistroVistor) {
		this.idMinistroVistor = idMinistroVistor;
	}
	public String getCabecalhoVistor() {
		return cabecalhoVistor;
	}
	public void setCabecalhoVistor(String cabecalhoVistor) {
		this.cabecalhoVistor = cabecalhoVistor;
	}
	
}

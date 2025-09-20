package br.gov.stf.estf.assinatura.relatorio;


public class RelatorioGerirCargaAutos {
	
	private String nomeAutorizador;
	private String nomeAutorizado;
	private String oab;
	private String dataCarga;
	private String dataPrevista;
	private String dataDevolucao;
	private String siglaNumeroProcesso;
	private String situacao;
	private Integer numeroCobranca;
	private String contatosJurisdicionado;
	
	

	public String getNomeAutorizador() {
		return nomeAutorizador;
	}
	
	public void setNomeAutorizador(String nomeAutorizador) {
		this.nomeAutorizador = nomeAutorizador;
	}
	
	public String getNomeAutorizado() {
		return nomeAutorizado;
	}
	
	public void setNomeAutorizado(String nomeAutorizado) {
		this.nomeAutorizado = nomeAutorizado;
	}
	
	public String getOab() {
		return oab;
	}
	
	public void setOab(String oab) {
		this.oab = oab;
	}

	public String getDataCarga() {
		return dataCarga;
	}
	
	public void setDataCarga(String dataCarga) {
		this.dataCarga = dataCarga;
	}
	
	public String getDataPrevista() {
		return dataPrevista;
	}
	
	public void setDataPrevista(String dataPrevista) {
		this.dataPrevista = dataPrevista;
	}
	
	public String getDataDevolucao() {
		return dataDevolucao;
	}
	
	public void setDataDevolucao(String dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	
	public String getSiglaNumeroProcesso() {
		return siglaNumeroProcesso;
	}
	
	public void setSiglaNumeroProcesso(String siglaNumeroProcesso) {
		this.siglaNumeroProcesso = siglaNumeroProcesso;
	}
	
	public String getSituacao() {
		return situacao;
	}
	
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	public Integer getNumeroCobranca() {
		return numeroCobranca;
	}
	
	public void setNumeroCobranca(Integer numeroCobranca) {
		this.numeroCobranca = numeroCobranca;
	}
	public String getContatosJurisdicionado() {
		return contatosJurisdicionado;
	}
	public void setContatosJurisdicionado(String contatosJurisdicionado) {
		this.contatosJurisdicionado = contatosJurisdicionado;
	}
}

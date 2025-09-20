package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

public class ProcessoPublicadoMinistroQuery {
	private Integer capitulo;
	private Integer[] materia;
	private Short ano;
	private Integer numero;	
	private String siglaProcessual;
	private Long numeroProcessual;
	private Long tipoRecurso;
	private Long tipoJulgamento;
	private Date dataSessao;
	private Long codigoSetor;
	private Long codigoMinistroRelator;
	private Long codigoAndamento;
	private Boolean recuperarOcultos;
	private String tipoMeioProcesso;
	private String confidencialidade;
	
	public Long getCodigoAndamento() {
		return codigoAndamento;
	}
	public void setCodigoAndamento(Long codigoAndamento) {
		this.codigoAndamento = codigoAndamento;
	}
	public Integer getCapitulo() {
		return capitulo;
	}
	public void setCapitulo(Integer capitulo) {
		this.capitulo = capitulo;
	}
	public Integer[] getMateria() {
		return materia;
	}
	public void setMateria(Integer[] materia) {
		this.materia = materia;
	}
	public void setMateria(Integer materia) {
		this.materia = new Integer[]{materia};
	}
	public Short getAno() {
		return ano;
	}
	public void setAno(Short ano) {
		this.ano = ano;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public String getSiglaProcessual() {
		return siglaProcessual;
	}
	public void setSiglaProcessual(String siglaProcessual) {
		this.siglaProcessual = siglaProcessual;
	}
	public Long getNumeroProcessual() {
		return numeroProcessual;
	}
	public void setNumeroProcessual(Long numeroProcessual) {
		this.numeroProcessual = numeroProcessual;
	}
	public Long getTipoRecurso() {
		return tipoRecurso;
	}
	public void setTipoRecurso(Long tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}
	public Long getTipoJulgamento() {
		return tipoJulgamento;
	}
	public void setTipoJulgamento(Long tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
	}
	public Date getDataSessao() {
		return dataSessao;
	}
	public void setDataSessao(Date dataSessao) {
		this.dataSessao = dataSessao;
	}
	public Long getCodigoSetor() {
		return codigoSetor;
	}
	public void setCodigoSetor(Long codigoSetor) {
		this.codigoSetor = codigoSetor;
	}
	public Long getCodigoMinistroRelator() {
		return codigoMinistroRelator;
	}
	public void setCodigoMinistroRelator(Long codigoMinistroRelator) {
		this.codigoMinistroRelator = codigoMinistroRelator;
	}
	public Boolean getRecuperarOcultos() {
		return recuperarOcultos;
	}
	public void setRecuperarOcultos(Boolean recuperarOcultos) {
		this.recuperarOcultos = recuperarOcultos;
	}
	public String getTipoMeioProcesso() {
		return tipoMeioProcesso;
	}
	public void setTipoMeioProcesso(String tipoMeioProcesso) {
		this.tipoMeioProcesso = tipoMeioProcesso;
	}
	public String getConfidencialidade() {
		return confidencialidade;
	}
	public void setConfidencialidade(String confidencialidade) {
		this.confidencialidade = confidencialidade;
	}
}

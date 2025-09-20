package br.gov.stf.estf.entidade.util;

import java.util.Date;

public class ProcessoPublicadoRelatorioQuery {
	private Integer capitulo;
	private Integer materia;
	private Short ano;
	private Integer numero;	
	private Date dataSessao;
	private TipoOrdenacao tipoOrdenacao;
	private Boolean recuperarOcultos;
	
	public enum TipoOrdenacao {
		ALFANUMERICO, MINISTRO;
	}

	public Integer getCapitulo() {
		return capitulo;
	}

	public void setCapitulo(Integer capitulo) {
		this.capitulo = capitulo;
	}

	public Integer getMateria() {
		return materia;
	}

	public void setMateria(Integer materia) {
		this.materia = materia;
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

	public Date getDataSessao() {
		return dataSessao;
	}

	public void setDataSessao(Date dataSessao) {
		this.dataSessao = dataSessao;
	}

	public TipoOrdenacao getTipoOrdenacao() {
		return tipoOrdenacao;
	}

	public void setTipoOrdenacao(TipoOrdenacao tipoOrdenacao) {
		this.tipoOrdenacao = tipoOrdenacao;
	}

	public Boolean getRecuperarOcultos() {
		return recuperarOcultos;
	}

	public void setRecuperarOcultos(Boolean recuperarOcultos) {
		this.recuperarOcultos = recuperarOcultos;
	}
}

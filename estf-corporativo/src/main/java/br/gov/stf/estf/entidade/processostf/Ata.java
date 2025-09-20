package br.gov.stf.estf.entidade.processostf;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Ata {
	private Integer numero;
	private Short ano;
	private String situacao;
	private String descricao;
	private Date dataCriacao;
	private Integer codigoCapitulo;
	private Integer codigoMateria;
	
	public Integer getCodigoCapitulo() {
		return codigoCapitulo;
	}

	public void setCodigoCapitulo(Integer codigoCapitulo) {
		this.codigoCapitulo = codigoCapitulo;
	}

	public Integer getCodigoMateria() {
		return codigoMateria;
	}

	public void setCodigoMateria(Integer codigomateria) {
		this.codigoMateria = codigomateria;
	}

	public String getDescricaoCompleta () {
		return numero+"/"+ano+" da "+descricao+" de "+new SimpleDateFormat("dd/MM/yyyy").format(dataCriacao)+" - "+situacao;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Short getAno() {
		return ano;
	}

	public void setAno(Short ano) {
		this.ano = ano;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public boolean isNumeroAnoPreenchido() {
		return numero != null && ano != null;
	}
	
	@Override
	public String toString() {
		return String.format(numero + "/" + ano);
	}
}

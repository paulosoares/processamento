package br.gov.stf.estf.documento.model.util;

import java.util.Date;

public class ComunicacaoSearch {

	private Date dataInicio;
	private Date dataFim;
	private Long numeroComunicacao;
	
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Date getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	public Long getNumeroComunicacao() {
		return numeroComunicacao;
	}
	public void setNumeroComunicacao(Long numeroComunicacao) {
		this.numeroComunicacao = numeroComunicacao;
	}
	
	
}

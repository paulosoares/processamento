package br.gov.stf.estf.assinatura.relatorio;

import java.util.Date;

/**
 * JavaBean com os dados do relatório de Acórdãos publicados que estão na Seção de Publicação de Acórdãos. 
 * @author RicardoLe
 *
 */
public class RelatorioAcordaoPublicado {
	
	private String siglaProcessual;
	private Long numeroProcessual;
	private String recurso;
	private String tipoMeio;
	private Date dataPublicacao;
	
	public void setSiglaProcessual(String siglaProcessual) {
		this.siglaProcessual = siglaProcessual;
	}
	public String getSiglaProcessual() {
		return siglaProcessual;
	}
	public void setNumeroProcessual(Long numeroProcessual) {
		this.numeroProcessual = numeroProcessual;
	}
	public Long getNumeroProcessual() {
		return numeroProcessual;
	}
	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}
	public String getRecurso() {
		return recurso;
	}
	public void setTipoMeio(String tipoMeio) {
		this.tipoMeio = tipoMeio;
	}
	public String getTipoMeio() {
		return tipoMeio;
	}
	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}
	public Date getDataPublicacao() {
		return dataPublicacao;
	}

}

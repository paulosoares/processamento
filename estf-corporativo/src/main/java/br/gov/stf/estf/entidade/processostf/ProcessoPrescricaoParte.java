package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

public class ProcessoPrescricaoParte {

	private PrescricaoReu prescricaoReu;
	private String nomeUltimoSetorDeslocamento;
	private Long codigoUltimoSetorDeslocamento;
	private Boolean mesmoProcesso;
	private String tipoPena;
	private Boolean ehAbstrata;
	private Boolean ehConcreta;
	private String tempoRestante;
	private int tempoRestanteParaOrdenacao;
	private Date dataPrescricaoPenaOrdenacao;

	public ProcessoPrescricaoParte() {

	}

	public ProcessoPrescricaoParte(PrescricaoReu prescricaoReu) {
		this.prescricaoReu = prescricaoReu;
	}

	public PrescricaoReu getPrescricaoReu() {
		return prescricaoReu;
	}

	public void setPrescricaoReu(PrescricaoReu prescricaoReu) {
		this.prescricaoReu = prescricaoReu;
	}

	public String getNomeUltimoSetorDeslocamento() {
		return nomeUltimoSetorDeslocamento;
	}

	public void setNomeUltimoSetorDeslocamento(String nomeUltimoSetorDeslocamento) {
		this.nomeUltimoSetorDeslocamento = nomeUltimoSetorDeslocamento;
	}

	public Long getCodigoUltimoSetorDeslocamento() {
		return codigoUltimoSetorDeslocamento;
	}

	public void setCodigoUltimoSetorDeslocamento(Long codigoUltimoSetorDeslocamento) {
		this.codigoUltimoSetorDeslocamento = codigoUltimoSetorDeslocamento;
	}

	public Boolean getMesmoProcesso() {
		return mesmoProcesso;
	}

	public void setMesmoProcesso(Boolean mesmoProcesso) {
		this.mesmoProcesso = mesmoProcesso;
	}

	public String getTipoPena() {
		return tipoPena;
	}

	public void setTipoPena(String tipoPena) {
		this.tipoPena = tipoPena;
	}

	public String getTempoRestante() {
		return tempoRestante;
	}

	public void setTempoRestante(String tempoRestante) {
		this.tempoRestante = tempoRestante;
	}

	public Boolean getEhAbstrata() {
		return ehAbstrata;
	}

	public void setEhAbstrata(Boolean ehAbstrata) {
		this.ehAbstrata = ehAbstrata;
	}

	public Boolean getEhConcreta() {
		return ehConcreta;
	}

	public void setEhConcreta(Boolean ehConcreta) {
		this.ehConcreta = ehConcreta;
	}

	public Date getDataPrescricaoPenaOrdenacao() {
		return dataPrescricaoPenaOrdenacao;
	}

	public void setDataPrescricaoPenaOrdenacao(Date dataPrescricaoPenaOrdenacao) {
		this.dataPrescricaoPenaOrdenacao = dataPrescricaoPenaOrdenacao;
	}

	public int getTempoRestanteParaOrdenacao() {
		return tempoRestanteParaOrdenacao;
	}

	public void setTempoRestanteParaOrdenacao(int calculaTempoRestanteParaOrdenacao) {
		this.tempoRestanteParaOrdenacao = calculaTempoRestanteParaOrdenacao;
		
	}
	
}

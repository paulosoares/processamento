package br.gov.stf.estf.assinatura.relatorio;

import java.util.Date;

public class RelatorioProcessoInteresse {
	
	private String identificacaoProcesso;
	private String nomeAdvogado;
	private String oab;
	private Long idAdvogado;
	private Date dataAndamento;
	private String descricaoAndamento;
	private String observacaoAndamento;
	
	public void setIdentificacaoProcesso(String identificacaoProcesso) {
		this.identificacaoProcesso = identificacaoProcesso;
	}
	public String getIdentificacaoProcesso() {
		return identificacaoProcesso;
	}
	public void setNomeAdvogado(String nomeAdvogado) {
		this.nomeAdvogado = nomeAdvogado;
	}
	public String getNomeAdvogado() {
		return nomeAdvogado;
	}
	public void setOab(String oab) {
		this.oab = oab;
	}
	public String getOab() {
		return oab;
	}
	public void setIdAdvogado(Long idAdvogado) {
		this.idAdvogado = idAdvogado;
	}
	public Long getIdAdvogado() {
		return idAdvogado;
	}
	public void setDataAndamento(Date dataAndamento) {
		this.dataAndamento = dataAndamento;
	}
	public Date getDataAndamento() {
		return dataAndamento;
	}
	public void setDescricaoAndamento(String descricaoAndamento) {
		this.descricaoAndamento = descricaoAndamento;
	}
	public String getDescricaoAndamento() {
		return descricaoAndamento;
	}
	public void setObservacaoAndamento(String observacaoAndamento) {
		this.observacaoAndamento = observacaoAndamento;
	}
	public String getObservacaoAndamento() {
		return observacaoAndamento;
	}
	

}

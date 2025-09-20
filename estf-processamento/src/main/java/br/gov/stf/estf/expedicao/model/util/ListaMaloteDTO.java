package br.gov.stf.estf.expedicao.model.util;

import java.math.BigDecimal;


public class ListaMaloteDTO {	
	
	private String posicao;
	private String destino;
	private String malote;
	private String lacre;
	private String guia;
	private String observacao;
	private String listasRemessa;
	private boolean maoPropriaNacional;
	
	public ListaMaloteDTO(String posicao, String destino, String malote,
			String lacre, String guia, String observacao, String listasRemessa, 
			boolean maoPropriaNacional) {
		this.posicao = posicao;
		this.destino = destino;
		this.malote = malote;
		this.lacre = lacre;
		this.guia = guia;
		this.observacao = observacao;
		this.listasRemessa = listasRemessa;
		this.maoPropriaNacional = maoPropriaNacional;
	}
	
	public boolean isMaoPropriaNacional() {
		return maoPropriaNacional;
	}

	public void setMaoPropriaNacional(boolean maoPropriaNacional) {
		this.maoPropriaNacional = maoPropriaNacional;
	}

	public String getListasRemessa() {
		return listasRemessa;
	}

	public void setListasRemessa(String listasRemessa) {
		this.listasRemessa = listasRemessa;
	}

	public String getPosicao() {
		return posicao;
	}
	public void setPosicao(String posicao) {
		this.posicao = posicao;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getMalote() {
		return malote;
	}
	public void setMalote(String malote) {
		this.malote = malote;
	}
	public String getLacre() {
		return lacre;
	}
	public void setLacre(String lacre) {
		this.lacre = lacre;
	}
	public String getGuia() {
		return guia;
	}
	public void setGuia(String guia) {
		this.guia = guia;
	}
		
	public static String getMascaraIR(Integer value)  {
	       Extenso extenso = new Extenso(new BigDecimal(value));
	       return  extenso.toString();
	 }

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	
	
	
	

}

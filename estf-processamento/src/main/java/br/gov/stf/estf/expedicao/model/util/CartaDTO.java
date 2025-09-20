package br.gov.stf.estf.expedicao.model.util;

import java.math.BigDecimal;

public class CartaDTO {
	
	
	private String numVolume;
	
	private String dataPostagem;
	
	private String destinatario;
	
	private String endereco;

	private boolean registrado;
	
	private boolean ar;
	
	private String peso;
	
	private String codigoRastreio;
	
	private String observacao;
	
	private String cepSemFormatacao;
	
    private boolean exibe;
    
    private boolean maoPropriaNacional;
    
    private String dataMatrix;
    
    public CartaDTO(String numVolume, String dataPostagem, String destinatario, String endereco,
			boolean registrado, boolean ar, String peso, String codigoRastreio, String observacao,String cepSemFormatacao, boolean exibe,
			boolean maoPropriaNacional) {
		super();
		this.numVolume = numVolume;
		this.dataPostagem = dataPostagem;
		this.destinatario = destinatario;
		this.endereco = endereco;
		this.registrado = registrado;
		this.ar = ar;
		this.peso = peso;
		this.codigoRastreio = codigoRastreio;
		this.observacao=observacao;
		this.cepSemFormatacao=cepSemFormatacao;
		this.exibe=exibe;
		this.maoPropriaNacional = maoPropriaNacional;
	}
	
	

	public CartaDTO(String numVolume, String dataPostagem, String destinatario, String endereco,
			boolean registrado, boolean ar, String peso, String codigoRastreio, String observacao,String cepSemFormatacao, boolean exibe,
			boolean maoPropriaNacional, String dataMatrix) {
		super();
		this.numVolume = numVolume;
		this.dataPostagem = dataPostagem;
		this.destinatario = destinatario;
		this.endereco = endereco;
		this.registrado = registrado;
		this.ar = ar;
		this.peso = peso;
		this.codigoRastreio = codigoRastreio;
		this.observacao=observacao;
		this.cepSemFormatacao=cepSemFormatacao;
		this.exibe=exibe;
		this.maoPropriaNacional = maoPropriaNacional;
		this.dataMatrix = dataMatrix;
	}
	
	
	public CartaDTO(boolean exibe) {
		this.exibe=exibe;
	}

	public String getDataPostagem() {
		return dataPostagem;
	}

	public void setDataPostagem(String dataPostagem) {
		this.dataPostagem = dataPostagem;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public boolean isRegistrado() {
		return registrado;
	}

	public void setRegistrado(boolean registrado) {
		this.registrado = registrado;
	}

	public boolean isAr() {
		return ar;
	}

	public void setAr(boolean ar) {
		this.ar = ar;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getCodigoRastreio() {
		return codigoRastreio;
	}

	public void setCodigoRastreio(String codigoRastreio) {
		this.codigoRastreio = codigoRastreio;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getNumVolume() {
		return numVolume;
	}

	public void setNumVolume(String numVolume) {
		this.numVolume = numVolume;
	}

	public String getCepSemFormatacao() {
		return cepSemFormatacao;
	}

	public void setCepSemFormatacao(String cepSemFormatacao) {
		this.cepSemFormatacao = cepSemFormatacao;
	}

	
	
	public static String getMascaraIR(Integer value)  {  

       Extenso extenso = new Extenso(new BigDecimal(value));
       return  extenso.toString();
   }


	public boolean isExibe() {
		return exibe;
	}


	public void setExibe(boolean exibe) {
		this.exibe = exibe;
	}


	public boolean isMaoPropriaNacional() {
		return maoPropriaNacional;
	}


	public void setMaoPropriaNacional(boolean maoPropriaNacional) {
		this.maoPropriaNacional = maoPropriaNacional;
	}



	public String getDataMatrix() {
		return dataMatrix;
	}



	public void setDataMatrix(String dataMatrix) {
		this.dataMatrix = dataMatrix;
	}  
	
	

}

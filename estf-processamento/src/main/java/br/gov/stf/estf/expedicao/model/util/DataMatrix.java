package br.gov.stf.estf.expedicao.model.util;

import org.apache.commons.lang.StringUtils;

public class DataMatrix {

	private String cepDestino = "0";
	private String complementoCepDestino = "0";
	private String cepOrigem = "0";
	private String complementoCepOrigem = "0";
	private String validadorCepDestino = "0";
	private String IDV = "0";
	private String codigoRastreamento = "0";
	private String servicosAdicionais = "0";
	private String cartaoDePostagem = "0";
	private String codigoServico = "0";
	private String informacaoAgrupamento = "0";
	private String numeroLogradouro = "0";
	private String complementoLogradouro = " ";
	private String valorDeclarado = "0";
	private String telefoneCompletoDestinatario = "0";
	private String latitude = "-00.";
	private String longitude = "-00.";
	private String pipe = "|";
	private String campoReserva = " ";

	public String getCepDestino() {
		return cepDestino;
	}

	public void setCepDestino(String cepDestino) {
		if (cepDestino!=null)
			this.cepDestino = cepDestino;
		this.validadorCepDestino = calculaValidadorCep(cepDestino);
	}

	public String getComplementoCepDestino() {
		return complementoCepDestino;
	}

	public void setComplementoCepDestino(String complementoCepDestino) {
		if (complementoCepDestino!=null)
			this.complementoCepDestino = complementoCepDestino;
	}

	public String getCepOrigem() {
		return cepOrigem;
	}

	public void setCepOrigem(String cepOrigem) {
		if (cepOrigem!=null)
			this.cepOrigem = cepOrigem;
	}

	public String getComplementoCepOrigem() {
		return complementoCepOrigem;
	}

	public void setComplementoCepOrigem(String complementoCepOrigem) {
		if (complementoCepOrigem!=null)
			this.complementoCepOrigem = complementoCepOrigem;
	}

	public String getValidadorCepDestino() {
		return validadorCepDestino;
	}

	public void setValidadorCepDestino(String validadorCepDestino) {
		if (validadorCepDestino!=null)
			this.validadorCepDestino = validadorCepDestino;
	}

	public String getIDV() {
		return IDV;
	}

	public void setIDV(String iDV) {
		if (iDV!=null)
			this.IDV = iDV;
	}

	public String getCodigoRastreamento() {
		return codigoRastreamento;
	}

	public void setCodigoRastreamento(String codigoRastreamento) {
		if (codigoRastreamento!=null)
			this.codigoRastreamento = codigoRastreamento;
	}

	public String getServicosAdicionais() {
		return servicosAdicionais;
	}

	public void setServicosAdicionais(String servicosAdicionais) {
		if (servicosAdicionais!=null)
			this.servicosAdicionais = servicosAdicionais;
	}

	public String getCartaoDePostagem() {
		return cartaoDePostagem;
	}

	public void setCartaoDePostagem(String cartaoDePostagem) {
		if (cartaoDePostagem!=null)
			this.cartaoDePostagem = cartaoDePostagem;
	}

	public String getCodigoServico() {
		return codigoServico;
	}

	public void setCodigoServico(String codigoServico) {
		if (codigoServico!=null)
			this.codigoServico = codigoServico;
	}

	public String getInformacaoAgrupamento() {
		return informacaoAgrupamento;
	}

	public void setInformacaoAgrupamento(String informacaoAgrupamento) {
		if (informacaoAgrupamento!=null)
			this.informacaoAgrupamento = informacaoAgrupamento;
	}

	public String getNumeroLogradouro() {
		return numeroLogradouro;
	}

	public void setNumeroLogradouro(String numeroLogradouro) {
		if (numeroLogradouro!=null)
			this.numeroLogradouro = numeroLogradouro;
	}

	public String getComplementoLogradouro() {
		return complementoLogradouro;
	}

	public void setComplementoLogradouro(String complementoLogradouro) {
		if (complementoLogradouro!=null)			
			this.complementoLogradouro = complementoLogradouro;
	}

	public String getValorDeclarado() {
		return valorDeclarado;
	}

	public void setValorDeclarado(String valorDeclarado) {
		if (valorDeclarado!=null)
			this.valorDeclarado = valorDeclarado;
	}

	public String getTelefoneCompletoDestinatario() {
		return telefoneCompletoDestinatario;
	}

	public void setTelefoneCompletoDestinatario(
			String telefoneCompletoDestinatario) {
		if (telefoneCompletoDestinatario!=null)
			this.telefoneCompletoDestinatario = telefoneCompletoDestinatario;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		if (latitude!=null)
			this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		if (longitude!=null)
			this.longitude = longitude;
	}

	public String getPipe() {
		return pipe;
	}

	public void setPipe(String pipe) {
		if (pipe!=null)
			this.pipe = pipe;
	}

	public String getCampoReserva() {
		return campoReserva;
	}

	public void setCampoReserva(String campoReserva) {
		if (campoReserva!=null)
			this.campoReserva = campoReserva;
	}

	public String padRight(String str, int size, char padChar) {
		return StringUtils.rightPad(str, size, padChar);
	}

	public String padLeft(String str, int size, char padChar) {
		return StringUtils.leftPad(str, size, padChar);
	}

	/**
	 * Soma dos 8 dígitos do CEP de destino:
	 * Subtrai-se o resultado da soma dos algarismos do CEP do múltiplo de 10
	 * imediatamente superior ao resultado. Ex: 71010050 -> 7+1+0+1+0+0+5+0 = 14, logo Subtrai-se 14 de 20. Validador = 6 
	 */
	private String calculaValidadorCep(String cep) {
		int soma = 0;

		String[] split = cep.split("(?!^)");

		for (int i = 0; i < split.length; i++) {
			soma += Integer.parseInt(split[i]);
		}
		
		soma = (10 - soma % 10);
		
		return String.valueOf( soma );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(padLeft(cepDestino, 8, '0'));
		sb.append(padLeft(complementoCepDestino, 5, '0'));
		sb.append(padLeft(cepOrigem, 8, '0'));
		sb.append(padLeft(complementoCepOrigem, 5, '0'));
		sb.append(padLeft(validadorCepDestino, 1, '0'));
		sb.append(padLeft(IDV, 2, '0'));
		sb.append(padLeft(codigoRastreamento, 13, '0'));
		sb.append(padRight(servicosAdicionais, 8, '0'));
		sb.append(padLeft(cartaoDePostagem, 10, '0'));
		sb.append(padLeft(codigoServico, 5, '0'));
		sb.append(padLeft(informacaoAgrupamento, 2, '0'));
		sb.append(padLeft(numeroLogradouro, 5, ' '));
		sb.append(padLeft(complementoLogradouro, 20, ' '));
		sb.append(padLeft(valorDeclarado, 5, '0'));
		sb.append(padLeft(telefoneCompletoDestinatario, 12, ' '));
		sb.append(padRight(latitude, 10, '0'));
		sb.append(padRight(longitude, 10, '0'));
		sb.append(pipe);
		sb.append(padLeft(campoReserva, 30, ' '));

		return sb.toString();
	}

}

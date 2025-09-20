package br.gov.stf.estf.expedicao.model.util;

public class ListaCorreiosDTO {
	
	
	
	private String destinatario;
	private String cepDestino;
	private String observacao;
	private String numLocalizador;
	private String tipoServico;	
	private String servicoAdicional;	
	private String peso;
	private String volume;
	private String numPLP;
	private boolean maoPropriaNacional;
	
	public ListaCorreiosDTO(String destinatario, String cepDestino,
			String observacao, String numLocalizador, String tipoServico,
			String servicoAdicional, String peso, String volume, String numPLP,
			boolean maoPropriaNacional) {
		super();
		this.destinatario = destinatario;
		this.cepDestino = cepDestino;
		this.observacao = observacao;
		this.numLocalizador = numLocalizador;
		this.tipoServico = tipoServico;
		this.servicoAdicional = servicoAdicional;
		this.peso = peso;
		this.volume = volume;
		this.numPLP = numPLP;
		this.maoPropriaNacional = maoPropriaNacional;
	}	

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getCepDestino() {
		return cepDestino;
	}

	public void setCepDestino(String cepDestino) {
		this.cepDestino = cepDestino;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getNumLocalizador() {
		return numLocalizador;
	}

	public void setNumLocalizador(String numLocalizador) {
		this.numLocalizador = numLocalizador;
	}

	public String getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	public String getServicoAdicional() {
		return servicoAdicional;
	}

	public void setServicoAdicional(String servicoAdicional) {
		this.servicoAdicional = servicoAdicional;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getNumPLP() {
		return numPLP;
	}

	public void setNumPLP(String numPLP) {
		this.numPLP = numPLP;
	}

	public boolean isMaoPropriaNacional() {
		return maoPropriaNacional;
	}

	public void setMaoPropriaNacional(boolean maoPropriaNacional) {
		this.maoPropriaNacional = maoPropriaNacional;
	}	

}

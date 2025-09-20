package br.gov.stf.estf.expedicao.model.util;

public class ArDTO {

	private String destinatario;
	private String conteudo;
	private String codigo;
	private String volume;
	private boolean maoPropriaNacional;

	public ArDTO(String destinatario, String conteudo, String codigo, String volume, boolean maoPropriaNacional) {
		super();
		this.destinatario = destinatario;
		this.conteudo = conteudo;
		this.codigo = codigo;
		this.volume = volume;
		this.maoPropriaNacional = maoPropriaNacional;
	}
	
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public boolean isMaoPropriaNacional() {
		return maoPropriaNacional;
	}

	public void setMaoPropriaNacional(boolean maoPropriaNacional) {
		this.maoPropriaNacional = maoPropriaNacional;
	}


}

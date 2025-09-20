package br.gov.stf.estf.expedicao.model.util;

public class ListaPortariaDTO {

	private String destinatario;
	private String peso;
	private String observacao;
	private String listasRemessa;
	private boolean maoPropriaNacional;

	public ListaPortariaDTO(String destinatario,
			String peso,
			String observacacao,
			String listasRemessa,
			boolean maoPropriaNacional) {
		super();
		this.destinatario = destinatario;
		this.peso = peso;
		this.observacao = observacacao;
		this.listasRemessa = listasRemessa;
		this.maoPropriaNacional = maoPropriaNacional;
	}
	
	public String getListasRemessa() {
		return listasRemessa;
	}

	public void setListasRemessa(String listasRemessa) {
		this.listasRemessa = listasRemessa;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public boolean isMaoPropriaNacional() {
		return maoPropriaNacional;
	}

	public void setMaoPropriaNacional(boolean maoPropriaNacional) {
		this.maoPropriaNacional = maoPropriaNacional;
	}
	
}
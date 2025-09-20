package br.gov.stf.estf.documento.model.service.enums;

public enum TipoDeAcessoDoDocumento {

	PUBLICO("PUB"), INTERNO("INT"), RESTRITO("RES");

	private String chave;

	public String getChave() {
		return chave;
	}

	private TipoDeAcessoDoDocumento(String chave) {
		this.chave = chave;
	}

}

package br.jus.stf.estf.decisao.mobile.assinatura.support;

public class AssinaturaExternaDocumentoDto extends AssinaturaDocumentoDto {

	private static final long serialVersionUID = -4734424628261081802L;

	private String id;
	private String hash;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

}

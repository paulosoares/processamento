package br.gov.stf.estf.entidade.publicacao;


public enum TipoSessao {
	ORDINARIA("O"),
	EXTRAORDINARIA("E"),
	VAZIO("X");
	
	private String sigla;
	
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	private TipoSessao(String sigla) {
		this.sigla = sigla;
	}
	
	public static TipoSessao valueOfSigla(String sigla) {
		if (sigla != null) {
			for (TipoSessao tipo : values()) {
				if (sigla.equals(tipo.getSigla())) {
					return tipo;
				}
			}
		}
		throw new RuntimeException("Nao existe TipoSessao com sigla: " + sigla);
	}
}

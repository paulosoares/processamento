package br.gov.stf.estf.entidade.publicacao;

public enum TipoModoDistribuicao {
	MANUAL("MA"),
	AUTOMATICA("AU");
	
	private String sigla;
	
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	private TipoModoDistribuicao(String sigla) {
		this.sigla = sigla;
	}
	
	public static TipoModoDistribuicao valueOfSigla(String sigla) {
		if (sigla != null) {
			for (TipoModoDistribuicao tipo : values()) {
				if (sigla.equals(tipo.getSigla())) {
					return tipo;
				}
			}
		}
		throw new RuntimeException("Nao existe TipoDistribuicao com sigla: " + sigla);
	}
	
}

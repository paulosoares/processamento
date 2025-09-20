package br.gov.stf.estf.entidade.documento;

public enum TipoLiberacao {
	SESSAO_VIRTUAL_ORDINARIA("VO", "Sess�o Virtual Ordin�ria"), 
	SESSAO_VIRTUAL_EXTRAORDINARIA("VE", "Sess�o Virtual Extraordin�ria"), 
	SESSAO_PRESENCIAL_ORDINARIA("PO", "Sess�o Presencial Ordin�ria"), 
	SESSAO_PRESENCIAL_EXTRAORDINARIA("PE", "Sess�o Presencial Extraordin�ria"),
	NAO_SE_APLICA("NA", "N�o se aplica");

	private String sigla;
	private String descricao;

	private TipoLiberacao(String sigla, String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}

	public static TipoLiberacao valueOfSigla(String sigla) {
		for (TipoLiberacao tipo : values()) {
			if (tipo.getSigla().equals(sigla)) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Nenhum elemento encontrado com a sigla informada: " + sigla);
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getSigla() {
		return sigla;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public boolean isVirtual() {
		return SESSAO_VIRTUAL_ORDINARIA.equals(this) || SESSAO_VIRTUAL_EXTRAORDINARIA.equals(this);
	}
	
	public boolean isPresencial() {
		return SESSAO_PRESENCIAL_ORDINARIA.equals(this) || SESSAO_PRESENCIAL_EXTRAORDINARIA.equals(this);
	}
	
	public boolean isOrdinaria() {
		return SESSAO_PRESENCIAL_ORDINARIA.equals(this) || SESSAO_VIRTUAL_ORDINARIA.equals(this);
	}
	
	public boolean isExtraordinaria() {
		return SESSAO_PRESENCIAL_EXTRAORDINARIA.equals(this) || SESSAO_VIRTUAL_EXTRAORDINARIA.equals(this);
	}
}

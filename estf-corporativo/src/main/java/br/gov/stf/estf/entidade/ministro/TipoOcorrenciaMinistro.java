package br.gov.stf.estf.entidade.ministro;

public enum TipoOcorrenciaMinistro {

	MP("MP", "Ministro Presidente"), 
	PI("PI", "Presidente Interino"), 
	S1("S1", ""), 
	S2("S2", ""), 
	T1("T1", ""), 
	T2("T2", ""), 
	_1T("1T", ""), 
	_2T("2T", ""), 
	VP("VP", "Vice-Presidente"), 
	PJ("PJ", "Presidente da Comissão de Jurisprudência"), 
	PC("PC", "Presidente do CNJ"),
	EL("EL", "Eleitoral");

	private String descricao;
	private String sigla;

	private TipoOcorrenciaMinistro(String sigla, String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public static TipoOcorrenciaMinistro getTipoOcorrenciaMinistro(String sigla) {
		for (TipoOcorrenciaMinistro ocorrencia : values()) {
			if (ocorrencia.getSigla().equals(sigla)) {
				return ocorrencia;
			}
		}
		return null;

	}
}

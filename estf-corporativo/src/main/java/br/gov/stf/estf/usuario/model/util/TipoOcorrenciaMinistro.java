package br.gov.stf.estf.usuario.model.util;

public enum TipoOcorrenciaMinistro {
	
	MP("MP","Ministro Presidente"),
	PI("PI","Presisdente Interino"),
	S1("S1",""),
	S2("S2",""),
	T1("T1",""),
	T2("T2",""),
	VP("VP","Vice-Presidente");
	
	
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
	
	public TipoOcorrenciaMinistro getTipoOcorrenciaMinistro(String sigla) {

		if( TipoOcorrenciaMinistro.MP.getSigla().equals(sigla))
			return TipoOcorrenciaMinistro.MP;		
		else if( TipoOcorrenciaMinistro.PI.getSigla().equals(sigla))
			return TipoOcorrenciaMinistro.PI;
		else if( TipoOcorrenciaMinistro.S1.getSigla().equals(sigla))
			return TipoOcorrenciaMinistro.S1;		
		else if( TipoOcorrenciaMinistro.S2.getSigla().equals(sigla))
			return TipoOcorrenciaMinistro.S2;
		else if( TipoOcorrenciaMinistro.T1.getSigla().equals(sigla))
			return TipoOcorrenciaMinistro.T1;
		else if( TipoOcorrenciaMinistro.T2.getSigla().equals(sigla))
			return TipoOcorrenciaMinistro.T2;
		else if( TipoOcorrenciaMinistro.VP.getSigla().equals(sigla))
			return TipoOcorrenciaMinistro.VP;
		
		return null;
	}
}

package br.gov.stf.estf.entidade.processostf;

public enum TipoCusta {
	I("Isento", "I"),
	J("Justiça gratuita", "J"),
	N("Aguarda preparo", "N"),                                
	P("Preparado", "P"),
	S("Indeterminado", "S"),
	X("Não informado", "X");
	

	private String	codigo;
	private String	descricao;


	private TipoCusta(String descricao, String codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}


	public String getDescricao() {
		return this.descricao;
	}


	public String getCodigo() {
		return this.codigo;
	}


	public TipoCusta getTipoCusta(String codigo) {
		if (TipoCusta.I.getCodigo().equals(codigo)) {
			return TipoCusta.I;
		} else if (TipoCusta.J.getCodigo().equals(codigo)) {
			return TipoCusta.J;
		} else if (TipoCusta.N.getCodigo().equals(codigo)) {
			return TipoCusta.N;
		} else if (TipoCusta.P.getCodigo().equals(codigo)) {
			return TipoCusta.P;
		} else if (TipoCusta.S.getCodigo().equals(codigo)) {
			return TipoCusta.S;
		} else {
			return TipoCusta.X;
		}
	}
}

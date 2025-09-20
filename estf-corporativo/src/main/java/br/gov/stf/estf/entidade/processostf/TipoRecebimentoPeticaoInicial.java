package br.gov.stf.estf.entidade.processostf;

public enum TipoRecebimentoPeticaoInicial {
	F("Fax", "F"),
	P("Petição Eletrônica", "P"),
	C("Correio", "C"),
	E("Email", "E"),
	B("BALCÃO", "B"),
	D("Processo Eletrônico", "D"),
	X("Indefinido", "X");
	

	private String	codigo;
	private String	descricao;


	private TipoRecebimentoPeticaoInicial(String descricao, String codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}


	public String getDescricao() {
		return this.descricao;
	}


	public String getCodigo() {
		return this.codigo;
	}


	public TipoRecebimentoPeticaoInicial getTipoRecebimentoPeticaoInicial(String codigo) {
		if (TipoRecebimentoPeticaoInicial.F.getCodigo().equals(codigo)) {
			return TipoRecebimentoPeticaoInicial.F;
		} else if (TipoRecebimentoPeticaoInicial.P.getCodigo().equals(codigo)) {
			return TipoRecebimentoPeticaoInicial.P;
		} else if (TipoRecebimentoPeticaoInicial.C.getCodigo().equals(codigo)) {
			return TipoRecebimentoPeticaoInicial.C;
		} else if (TipoRecebimentoPeticaoInicial.E.getCodigo().equals(codigo)) {
			return TipoRecebimentoPeticaoInicial.E;
		} else if (TipoRecebimentoPeticaoInicial.B.getCodigo().equals(codigo)) {
			return TipoRecebimentoPeticaoInicial.B;
		} else if (TipoRecebimentoPeticaoInicial.D.getCodigo().equals(codigo)) {
			return TipoRecebimentoPeticaoInicial.D;
		} else {
			return TipoRecebimentoPeticaoInicial.X;
		}
	}
}

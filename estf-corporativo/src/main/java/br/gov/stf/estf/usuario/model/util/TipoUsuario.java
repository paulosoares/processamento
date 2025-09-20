package br.gov.stf.estf.usuario.model.util;

public enum TipoUsuario {
	
	IN("IN", "Indefinido"),
	AN("AN", "Analista"),
	TC("TC", "Técnico"),
	AS("AS", "Assessor"),
	OU("OU", "Outro");
	
	private String descricao;
	private String codigo;
	
	private TipoUsuario(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public TipoUsuario getTipoUsuario(String codigo) {

		if( TipoUsuario.AS.getCodigo().equals(codigo))
			return TipoUsuario.AS;		
		else if( TipoUsuario.AN.getCodigo().equals(codigo))
			return TipoUsuario.AN;
		else if( TipoUsuario.TC.getCodigo().equals(codigo))
			return TipoUsuario.TC;		
		else if( TipoUsuario.OU.getCodigo().equals(codigo))
			return TipoUsuario.OU;
		
		return TipoUsuario.IN;
	}
}

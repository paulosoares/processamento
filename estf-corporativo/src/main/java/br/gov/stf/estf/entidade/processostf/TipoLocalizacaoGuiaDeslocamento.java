package br.gov.stf.estf.entidade.processostf;


public enum TipoLocalizacaoGuiaDeslocamento {

	ADVOGADO(Short.valueOf("1"), "Advogado"), 
	INTERNO(Short.valueOf("2"), "Interno"), 
	EXTERNO(Short.valueOf("3"), "Externo");
	
	private Short codigo;
	private String descricao;
	
	private TipoLocalizacaoGuiaDeslocamento(Short codigo, String descricao) {
		
		this.codigo = codigo;
		this.descricao = descricao;
	}
	

	public Short getCodigo() {
		return codigo;
	}

	public void setCodigo(Short codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public TipoLocalizacaoGuiaDeslocamento getInstance(Short codigo) {
		
		if(codigo == null)
			throw new NullPointerException("Código do tipo de localização nulo");
		else if(codigo.equals(1))
			return TipoLocalizacaoGuiaDeslocamento.ADVOGADO;
		else if(codigo.equals(2))
			return TipoLocalizacaoGuiaDeslocamento.INTERNO;
		else if(codigo.equals(3))
			return TipoLocalizacaoGuiaDeslocamento.EXTERNO;
		else
			throw new IllegalArgumentException("codigo de tipo de localização inválido");
	}
}

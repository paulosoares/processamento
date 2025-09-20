package br.gov.stf.estf.entidade.documento;

import br.gov.stf.framework.util.GenericEnum;


public class EspecieTexto extends GenericEnum<Long,EspecieTexto> {
    public static EspecieTexto CERTIDAO = new EspecieTexto(1,"Certidão",true);
    public static EspecieTexto TERMO_DE_JUNTADA = new EspecieTexto(2,"Termo de Juntada",true);
    public static EspecieTexto VOTO = new EspecieTexto(3, "Voto", true);
    public static EspecieTexto OFICIO = new EspecieTexto(4, "Ofício", true);
    public static EspecieTexto INFORMACAO = new EspecieTexto(5, "Informação", true);
    
	private final String descricao;
	private final boolean ativo;

	private EspecieTexto(Long codigo) {
		this(codigo,"Especie Texto " + codigo,true);
	}
	private EspecieTexto(long codigo, String descricao, boolean ativo) {
		super(codigo);
    	this.descricao = descricao;
    	this.ativo = ativo;
    }

	public String getDescricao() {
		return descricao;
	}

	public boolean isAtivo() {
		return ativo;
	}
	
	public static EspecieTexto valueOf(Long codigo) {
		return valueOf(EspecieTexto.class,codigo);
	}
	
	public static EspecieTexto[] values() {
		return values(new EspecieTexto[0],EspecieTexto.class);
	}
}

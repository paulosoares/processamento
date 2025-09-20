package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;


/*
 * A peticao eletronica pode ser dos tipos:
 *  * Avulsa (PA), 
 *  * Incidental (PC) ou 
 *  * Inicial (PI).
 */
public class TipoPeticaoEletronica extends GenericEnum<String, TipoPeticaoEletronica> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7975920710223730784L;
	public static final TipoPeticaoEletronica PETICAO_AVULSA = new TipoPeticaoEletronica("PA", "Petição Avulsa");
	public static final TipoPeticaoEletronica PETICAO_INCIDENTAL = new TipoPeticaoEletronica("PC", "Petição Incidental");
	public static final TipoPeticaoEletronica PETICAO_INICIAL = new TipoPeticaoEletronica("PI", "Petição Inicial");

	
	private final String descricao;

	private TipoPeticaoEletronica(String sigla) {
		this(sigla, "Tipo de Petição Eletrônica:" + sigla);
	}

	private TipoPeticaoEletronica(String sigla, String descricao) {
		super(sigla);
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoPeticaoEletronica valueOf(String TipoPeticaoEletronica) {
		return valueOf(TipoPeticaoEletronica.class, TipoPeticaoEletronica);
	}

	public static TipoPeticaoEletronica[] values() {
		return values(new TipoPeticaoEletronica[0], TipoPeticaoEletronica.class);
	}
}

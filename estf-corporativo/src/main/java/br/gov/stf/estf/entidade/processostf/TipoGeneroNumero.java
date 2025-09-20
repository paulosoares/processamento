package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;

/**
 * MS - Masculino singular FS - Feminino Singular MP - Masculino Plural FP -
 * Feminino Plural
 */
public class TipoGeneroNumero extends GenericEnum<String, TipoGeneroNumero> {

	private static final long serialVersionUID = 7225506753603969753L;
	
	public static final TipoGeneroNumero MASCULINO_SINGULAR = new TipoGeneroNumero("MS", "Masculino singular");
	public static final TipoGeneroNumero FEMININO_SINGULAR = new TipoGeneroNumero("FS", "Feminino singular");
	public static final TipoGeneroNumero MASCULINO_PLURAL = new TipoGeneroNumero("MP", "Masculino plural");
	public static final TipoGeneroNumero FEMININO_PLURAL = new TipoGeneroNumero("FP", "Feminino plural");

	private final String descricao;

	private TipoGeneroNumero(String sigla) {
		this(sigla, "Tipo Gênero Número:" + sigla);
	}

	private TipoGeneroNumero(String sigla, String descricao) {
		super(sigla);
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoGeneroNumero valueOf(String TipoGeneroNumero) {
		return valueOf(TipoGeneroNumero.class, TipoGeneroNumero);
	}

	public static TipoGeneroNumero[] values() {
		return values(new TipoGeneroNumero[0], TipoGeneroNumero.class);
	}
}

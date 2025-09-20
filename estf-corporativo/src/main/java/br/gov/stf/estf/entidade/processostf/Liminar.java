package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;

/**
 *R - Urgente (não utilizado atualmente) S - é liminar N - não é liminar
 */
public class Liminar extends GenericEnum<String, Liminar> {
	public static final Liminar URGENTE = new Liminar("R");
	public static final Liminar SIM = new Liminar("S");
	public static final Liminar NAO = new Liminar("N");

	private Liminar(String sigla) {
		super(sigla);
	}

	public static Liminar valueOf(String Liminar) {
		return valueOf(Liminar.class, Liminar);
	}

	public static Liminar[] values() {
		return values(new Liminar[0], Liminar.class);
	}

}

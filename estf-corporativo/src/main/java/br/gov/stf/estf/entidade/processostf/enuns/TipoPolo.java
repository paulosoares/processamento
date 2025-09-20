package br.gov.stf.estf.entidade.processostf.enuns;

import br.gov.stf.framework.util.GenericEnum;

/**
 * Tipo do pólo default da categoria. Pode ter os valores:
 * <p> - 'AT' - ATIVO </br>
 *     - 'PA' - PASSIVO </br>
 *     - 'PO' - POSTULANTE ou nulo.
 * 
 * @author Rodrigo.Barreiros
 * @since 15.07.2009
 */
public class TipoPolo extends GenericEnum<String, TipoPolo> {
	
	private static final long serialVersionUID = 3854820929331503992L;
	
	public static final TipoPolo POSTULANTE = new TipoPolo("PO");
	public static final TipoPolo PASSIVO = new TipoPolo("PA");
	public static final TipoPolo ATIVO = new TipoPolo("AT");
	
	private TipoPolo(String sigla) {
		super(sigla);
	}

	public static TipoPolo valueOf(String liminar) {
		return valueOf(TipoPolo.class, liminar);
	}

	public static TipoPolo[] values() {
		return values(new TipoPolo[0], TipoPolo.class);
	}

}

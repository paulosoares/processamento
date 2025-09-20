package br.gov.stf.estf.model.util;

import br.gov.stf.framework.util.SearchData;

/**
 * Classe utilizada para encapsular dados necessários em consultas diversas que exijam muitos parâmetros.
 * 
 * @author thiago.miranda
 */
public class ESTFSearchData extends SearchData {

	private static final long serialVersionUID = 4176261200025069518L;

	public Boolean limitarPesquisa;
	public Boolean readOnlyQuery;
	public TipoOrdem tipoOrdem;

	public ESTFSearchData(Boolean limitarPesquisa, Boolean readOnlyQuery, TipoOrdem tipoOrdem) {
		this.limitarPesquisa = limitarPesquisa;
		this.readOnlyQuery = readOnlyQuery;
		this.tipoOrdem = tipoOrdem;
	}

	public ESTFSearchData() {
		super();
	}

	// Métodos utilitários
	// TODO devem ser movidos para SearchData

	public static boolean maiorQueZero(Byte numero) {
		return numero != null && numero.byteValue() > 0;
	}

	public static boolean maiorQueZero(Short numero) {
		return numero != null && numero.shortValue() > 0;
	}

	public static boolean maiorQueZero(Integer numero) {
		return numero != null && numero.intValue() > 0;
	}

	public static boolean maiorQueZero(Long numero) {
		return numero != null && numero.longValue() > 0;
	}

	public static boolean isTrue(Boolean b) {
		return b != null && b.booleanValue();
	}

	public static boolean isFalse(Boolean b) {
		return b != null && !b.booleanValue();
	}
}

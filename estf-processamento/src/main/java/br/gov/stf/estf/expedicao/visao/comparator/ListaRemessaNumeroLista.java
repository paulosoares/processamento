package br.gov.stf.estf.expedicao.visao.comparator;

import java.io.Serializable;
import java.util.Comparator;

import br.gov.stf.estf.expedicao.entidade.ListaRemessa;

/**
 *
 * @author roberio.fernandes
 */
public class ListaRemessaNumeroLista implements Serializable, Comparator<ListaRemessa> {

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(ListaRemessa listaRemessa1, ListaRemessa listaRemessa2) {
		int comparar = (int) (listaRemessa1.getNumeroListaRemessa() - listaRemessa2.getNumeroListaRemessa());
		return comparar;
	}
}
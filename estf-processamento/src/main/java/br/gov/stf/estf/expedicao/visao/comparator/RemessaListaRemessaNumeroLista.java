package br.gov.stf.estf.expedicao.visao.comparator;

import java.io.Serializable;
import java.util.Comparator;

import br.gov.stf.estf.expedicao.entidade.Remessa;

/**
 *
 * @author roberio.fernandes
 */
public class RemessaListaRemessaNumeroLista implements Serializable, Comparator<Remessa> {

	private static final long serialVersionUID = 1L;

	private final ListaRemessaNumeroLista listaRemessaNumeroLista;

	public RemessaListaRemessaNumeroLista(ListaRemessaNumeroLista listaRemessaNumeroLista) {
		super();
		this.listaRemessaNumeroLista = listaRemessaNumeroLista;
	}

	@Override
	public int compare(Remessa remessa1, Remessa remessa2) {
		return listaRemessaNumeroLista.compare(remessa1.getListaRemessa(), remessa2.getListaRemessa());
	}
}
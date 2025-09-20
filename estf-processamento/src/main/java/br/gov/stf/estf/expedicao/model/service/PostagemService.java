package br.gov.stf.estf.expedicao.model.service;

import br.gov.stf.estf.expedicao.entidade.ListaRemessa;
import br.gov.stf.framework.model.service.ServiceException;

public interface PostagemService {

	/**
	 * Gera uma etiqueta para cada volume de cada remessa da lista informada,
	 * em seguida fecha a lista de postagem informada.
	 * Ap�s esta opera��o a lista n�o poder� mais ser alterada.
	 *
	 * @param listaRemessa
     * @throws br.gov.stf.framework.model.service.ServiceException 
	 */
	void gerarEtiquetas(ListaRemessa listaRemessa) throws ServiceException;
}
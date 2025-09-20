package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.ListaTextosDao;
import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ListaTextosService extends GenericService<ListaTextos, Long, ListaTextosDao> {

	List<ListaTextos> pesquisarListaTextos(String nome, Boolean ativo, Long idSetor) throws ServiceException;

	ListaTextos recuperarPorNome(String nome) throws ServiceException;
	
	public List<ListaTextos> pesquisarListasDoTexto(Texto texto) throws ServiceException;

	/**
	 * Exclui o texto informado de qualquer lista de texto a que ele pertença.
	 * 
	 * @param texto
	 * @throws ServiceException
	 * @author Demetrius.Jube
	 * @since 08/01/2010
	 */
	void excluirTextoDasListas(Texto texto) throws ServiceException;

}
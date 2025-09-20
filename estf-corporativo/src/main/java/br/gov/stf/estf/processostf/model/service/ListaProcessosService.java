package br.gov.stf.estf.processostf.model.service;


import java.util.List;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.processostf.model.dataaccess.ListaProcessosDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ListaProcessosService extends GenericService<ListaProcessos, Long, ListaProcessosDao> {
	
	public ListaProcessos recuperarPorNome(String nome) throws ServiceException;
	public List<ListaProcessos> pesquisarListaProcessos(String nome, Boolean ativo,Long idSetor) throws ServiceException;
	
}
package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ListaProcessosDao extends GenericDao<ListaProcessos, Long> {
	
	public ListaProcessos recuperarPorNome(String nome) throws DaoException;
	public List<ListaProcessos> pesquisarListaProcessos(String nome, Boolean ativo,Long idSetor) throws DaoException;
		
}

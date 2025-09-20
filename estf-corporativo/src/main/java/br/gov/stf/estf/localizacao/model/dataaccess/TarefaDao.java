package br.gov.stf.estf.localizacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.Tarefa;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TarefaDao extends GenericDao<Tarefa, Long> {

	public List<Tarefa> verificarUnicidade(Long id,String descricao) throws DaoException;
	
	public Secao recuperarTarefa(Long id) throws DaoException;
        
	public List<Tarefa> pesquisarTarefa(Long id,String descricao, Long idSecao, Long idSetor,boolean localizacaoIgual) throws DaoException;
	
	public Boolean persistirTarefa(Tarefa tarefa) throws DaoException;
	
	public Boolean excluirTarefa(Tarefa tarefa) throws DaoException;	
}

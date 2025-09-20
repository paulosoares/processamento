package br.gov.stf.estf.tarefa.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.tarefa.TipoSituacaoTarefa;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoSituacaoTarefaDao extends GenericDao<TipoSituacaoTarefa, Long> {
	
	public Boolean persistirTipoSituacaoTarefa(TipoSituacaoTarefa tipoSituacaoTarefa) throws DaoException;
	
	public Boolean excluirTipoSituacaoTarefa(TipoSituacaoTarefa tipoSituacaoTarefa) throws DaoException;
	
	public List<TipoSituacaoTarefa> pesquisarTipoSituacaoTarefa(Long Id,String Descricao,Long idSetor,Boolean ativo,Boolean semSetor)
	throws DaoException;
	
	public TipoSituacaoTarefa recuperarTipoSituacaoTarefa(Long id)throws DaoException;


}

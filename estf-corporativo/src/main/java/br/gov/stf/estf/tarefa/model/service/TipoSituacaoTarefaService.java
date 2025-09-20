package br.gov.stf.estf.tarefa.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.tarefa.TipoSituacaoTarefa;
import br.gov.stf.estf.tarefa.model.dataaccess.TipoSituacaoTarefaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;


public interface TipoSituacaoTarefaService extends GenericService<TipoSituacaoTarefa, Long, TipoSituacaoTarefaDao> {

	public Boolean persistirTipoSituacaoTarefa(TipoSituacaoTarefa tipoSituacaoTarefa) throws ServiceException;
	
	public Boolean excluirTipoSituacaoTarefa(TipoSituacaoTarefa tipoSituacaoTarefa)throws ServiceException;
	
	public List<TipoSituacaoTarefa> pesquisarTipoSituacaoTarefa(Long Id,String Descricao,Long idSetor,Boolean ativo,Boolean semSetor)
	throws ServiceException;
	
	public TipoSituacaoTarefa recuperarTipoSituacaoTarefa(Long id)throws ServiceException;
	
}

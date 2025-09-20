package br.gov.stf.estf.localizacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Tarefa;
import br.gov.stf.estf.localizacao.model.dataaccess.TarefaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TarefaService extends GenericService<Tarefa, Long, TarefaDao> {
    /**
     * metodo resposanvel por pesquisar tarefas
     * @param id codigo da tarefa 
     * @param descricao  dastraição da tarefa
     * @param idSecao codigo da seção da tarefa
     * @param idSetor codigo da localizacao da tarefa
     * @param localizacaoIgual se for true pesquisa os localizacao igual ao localizacao informado se não os localizacao difirentes.
     * @return retorna um lista de tarefas
     * @throws ServiceException
     * @author GuilhermeA
     */
	public List<Tarefa> pesquisarTarefa(Long id,String descricao, Long idSecao, Long idSetor,boolean localizacaoIgual)throws ServiceException;
	
	public Boolean persistirTarefa(Tarefa tarefa) throws ServiceException;
	
	public Boolean excluirTarefa(Tarefa tarefa) throws ServiceException;
}

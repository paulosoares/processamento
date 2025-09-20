package br.gov.stf.estf.tarefa.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.tarefa.ConfiguracaoTipoTarefaSetor;
import br.gov.stf.estf.entidade.tarefa.TipoAtribuicaoTarefa;
import br.gov.stf.estf.entidade.tarefa.TipoCampoTarefa;
import br.gov.stf.estf.entidade.tarefa.TipoCampoTarefaValor;
import br.gov.stf.estf.entidade.tarefa.TipoTarefaSetor;
import br.gov.stf.estf.tarefa.model.dataaccess.TipoTarefaSetorDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;


public interface TipoTarefaSetorService extends GenericService<TipoTarefaSetor, Long, TipoTarefaSetorDao> {
	
	public TipoTarefaSetor recuperarTipoTarefaSetor(Long id) throws ServiceException;
    public List<TipoTarefaSetor> pesquisarTipoTarefaSetor(Long id,String descricao,Long idSetor,Boolean ativo) throws ServiceException;
    public List<TipoTarefaSetor> pesquisarTipoTarefaSetor(Long id,String descricao,Long idSetor,Boolean ativo, Boolean cargaProgramada) throws ServiceException;
    
	public Boolean persistirTipoTarefaSetor(TipoTarefaSetor tipoTarefaSetor) throws ServiceException;
	public Boolean excluirTipoTarefaSetor(TipoTarefaSetor tipoTarefaSetor)throws ServiceException;
	
	public TipoAtribuicaoTarefa recuperarTipoAtribuicaoTarefa(Long id,String sigla,String descricao) throws ServiceException;
    public List<TipoAtribuicaoTarefa> pesquisarTipoAtribuicaoTarefa(Long id,String sigla,String descricao) throws ServiceException;
    
    public ConfiguracaoTipoTarefaSetor recuperarConfiguracaoTipoTarefaSetor(Long id,String sigla,String descricao) throws ServiceException;
    public List<ConfiguracaoTipoTarefaSetor> pesquisarConfiguracaoTipoTarefaSetor(Long id,String sigla,String descricao) throws ServiceException;
    
    public List<TipoCampoTarefaValor> pesquisarTipoCampoTarefaValor(Long id, Long idTipoCampoTarefa)throws ServiceException;
    
    public TipoCampoTarefa recuperarTipoCampoTarefa(Long id) throws ServiceException;
}

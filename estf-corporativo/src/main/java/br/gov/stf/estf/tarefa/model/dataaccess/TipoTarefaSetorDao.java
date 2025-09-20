package br.gov.stf.estf.tarefa.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.tarefa.ConfiguracaoTipoTarefaSetor;
import br.gov.stf.estf.entidade.tarefa.TipoAtribuicaoTarefa;
import br.gov.stf.estf.entidade.tarefa.TipoCampoTarefa;
import br.gov.stf.estf.entidade.tarefa.TipoCampoTarefaValor;
import br.gov.stf.estf.entidade.tarefa.TipoTarefaSetor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoTarefaSetorDao extends GenericDao<TipoTarefaSetor, Long> {
	
	public TipoTarefaSetor recuperarTipoTarefaSetor(Long id) throws DaoException;
    public List<TipoTarefaSetor> pesquisarTipoTarefaSetor(Long id,String descricao ,Long idSetor, Boolean ativo) throws DaoException;
    public List<TipoTarefaSetor> pesquisarTipoTarefaSetor(Long id,String descricao ,Long idSetor, Boolean ativo, Boolean cargaProgramada) throws DaoException;
    
    public Boolean verificarUnicidade(String descricao,Long idSetor)throws DaoException;
	
    public Boolean persistirTipoTarefaSetor(TipoTarefaSetor tipoTarefaSetor) throws DaoException;
	public Boolean excluirTipoTarefaSetor(TipoTarefaSetor tipoTarefaSetor) throws DaoException;
	
	public TipoAtribuicaoTarefa recuperarTipoAtribuicaoTarefa(Long id,String sigla,String descricao) throws DaoException;
    public List<TipoAtribuicaoTarefa> pesquisarTipoAtribuicaoTarefa(Long id,String sigla,String descricao) throws DaoException;
    
    public ConfiguracaoTipoTarefaSetor recuperarConfiguracaoTipoTarefaSetor(Long id,String sigla,String descricao) throws DaoException;
    public List<ConfiguracaoTipoTarefaSetor> pesquisarConfiguracaoTipoTarefaSetor(Long id,String sigla,String descricao) throws DaoException;
    
    public List<TipoCampoTarefaValor> pesquisarTipoCampoTarefaValor(Long id, Long idTipoCampoTarefa)throws DaoException;
    public TipoCampoTarefa recuperarTipoCampoTarefa(Long id) throws DaoException;

}

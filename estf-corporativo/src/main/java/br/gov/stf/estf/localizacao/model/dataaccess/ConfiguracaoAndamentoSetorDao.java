package br.gov.stf.estf.localizacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.ConfiguracaoAndamentoSetor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ConfiguracaoAndamentoSetorDao extends GenericDao<ConfiguracaoAndamentoSetor, Long> {

	public ConfiguracaoAndamentoSetor recuperarConfiguracaoAndamentoSetor(Long id) throws DaoException;
	
    public List<ConfiguracaoAndamentoSetor> pesquisarConfiguracaoAndamentoSetor(Long id, String descricao, Long idSetor,Boolean ativo)
    throws DaoException;
    
    public Boolean persistirConfiguracaoAndamentoSetor(ConfiguracaoAndamentoSetor configuracaoAndamentoSetor) 
    throws DaoException;
    
    public Boolean excluirConfiguracaoAndamentoSetor(ConfiguracaoAndamentoSetor configuracaoAndamentoSetor)
    throws DaoException;
}

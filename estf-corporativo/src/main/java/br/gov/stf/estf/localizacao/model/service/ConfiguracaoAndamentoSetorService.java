package br.gov.stf.estf.localizacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.ConfiguracaoAndamentoSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.ConfiguracaoAndamentoSetorDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ConfiguracaoAndamentoSetorService extends GenericService<ConfiguracaoAndamentoSetor, Long, ConfiguracaoAndamentoSetorDao> {

	public ConfiguracaoAndamentoSetor recuperarConfiguracaoAndamentoSetor(Long id) throws ServiceException;
	
    public List<ConfiguracaoAndamentoSetor> pesquisarConfiguracaoAndamentoSetor(Long id, String descricao, Long idSetor,Boolean ativo)
    throws ServiceException;
    
    public Boolean persistirConfiguracaoAndamentoSetor(ConfiguracaoAndamentoSetor configuracaoAndamentoSetor) 
    throws ServiceException;
    
    public Boolean excluirConfiguracaoAndamentoSetor(ConfiguracaoAndamentoSetor configuracaoAndamentoSetor)
    throws ServiceException;
    
    
}

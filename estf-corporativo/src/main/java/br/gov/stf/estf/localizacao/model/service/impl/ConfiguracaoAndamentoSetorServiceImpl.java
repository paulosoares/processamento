package br.gov.stf.estf.localizacao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.ConfiguracaoAndamentoSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.ConfiguracaoAndamentoSetorDao;
import br.gov.stf.estf.localizacao.model.service.ConfiguracaoAndamentoSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("configuracaoAndamentoSetorService")
public class ConfiguracaoAndamentoSetorServiceImpl 
extends GenericServiceImpl<ConfiguracaoAndamentoSetor, Long, ConfiguracaoAndamentoSetorDao>  
implements ConfiguracaoAndamentoSetorService {
    
    public ConfiguracaoAndamentoSetorServiceImpl(ConfiguracaoAndamentoSetorDao daoConfiguracaoAndamentoSetor) {
        super(daoConfiguracaoAndamentoSetor);
    }
    
    public ConfiguracaoAndamentoSetor recuperarConfiguracaoAndamentoSetor(Long id) throws ServiceException{
    	
    	try{
    		return dao.recuperarConfiguracaoAndamentoSetor(id);
    	}catch (DaoException e) {
			throw new ServiceException(e);
		}
    }
	
    public List<ConfiguracaoAndamentoSetor> pesquisarConfiguracaoAndamentoSetor(Long id, String descricao, Long idSetor,Boolean ativo)
    throws ServiceException{
    	
    	try{
    		return dao.pesquisarConfiguracaoAndamentoSetor(id, descricao, idSetor, ativo);
    	}catch (DaoException e) {
			throw new ServiceException(e);
		}
    }
    
    public Boolean persistirConfiguracaoAndamentoSetor(ConfiguracaoAndamentoSetor configuracaoAndamentoSetor) 
    throws ServiceException{
    	
    	try{
    		return dao.persistirConfiguracaoAndamentoSetor(configuracaoAndamentoSetor);
    	}catch (DaoException e) {
			throw new ServiceException(e);
		}
    	
    }
    
    public Boolean excluirConfiguracaoAndamentoSetor(ConfiguracaoAndamentoSetor configuracaoAndamentoSetor)
    throws ServiceException{
    	
    	try{
    		return dao.excluirConfiguracaoAndamentoSetor(configuracaoAndamentoSetor);
    	}catch (DaoException e) {
			throw new ServiceException(e);
		}
    	
    }


}
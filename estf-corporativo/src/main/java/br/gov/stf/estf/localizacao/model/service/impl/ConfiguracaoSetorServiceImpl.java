package br.gov.stf.estf.localizacao.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.ConfiguracaoSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.ConfiguracaoSetorDao;
import br.gov.stf.estf.localizacao.model.service.ConfiguracaoSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("configuracaoSetorService")
public class ConfiguracaoSetorServiceImpl 
	extends GenericServiceImpl<ConfiguracaoSetor, Long, ConfiguracaoSetorDao>  
	implements ConfiguracaoSetorService {

	protected ConfiguracaoSetorServiceImpl(ConfiguracaoSetorDao dao) {
		super(dao);
	}
	
	public ConfiguracaoSetor pesquisarConfiguracaoSetor(Long codigoSetor, Long codigoTipoConfiguracaoSetor)
		throws ServiceException {
		
		ConfiguracaoSetor configuracaoSetor = null;
		
		try {
			
			configuracaoSetor = dao.pesquisarConfiguracaoSetor(codigoSetor, codigoTipoConfiguracaoSetor);
		
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return configuracaoSetor;
	}
	
	public ConfiguracaoSetor recuperarConfiguracaoSetor(Long codigoSetor, String siglaTipoConfiguracaoSetor) throws ServiceException{
		ConfiguracaoSetor configuracaoSetor = null;
		
		try {
			
			configuracaoSetor = dao.recuperarConfiguracaoSetor(codigoSetor, siglaTipoConfiguracaoSetor);
		
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return configuracaoSetor;
	}

}

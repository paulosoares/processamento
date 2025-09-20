package br.gov.stf.estf.localizacao.model.service;

import br.gov.stf.estf.entidade.localizacao.ConfiguracaoSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.ConfiguracaoSetorDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ConfiguracaoSetorService extends GenericService<ConfiguracaoSetor, Long, ConfiguracaoSetorDao> {
	
	public ConfiguracaoSetor pesquisarConfiguracaoSetor(Long codigoSetor, Long codigoTipoConfiguracaoSetor) throws ServiceException;
	public ConfiguracaoSetor recuperarConfiguracaoSetor(Long codigoSetor, String siglaTipoConfiguracaoSetor) throws ServiceException;

}

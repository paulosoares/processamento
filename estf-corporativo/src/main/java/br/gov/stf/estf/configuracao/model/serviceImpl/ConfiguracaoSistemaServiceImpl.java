package br.gov.stf.estf.configuracao.model.serviceImpl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.configuracao.model.dataaccess.ConfiguracaoSistemaDao;
import br.gov.stf.estf.configuracao.model.service.ConfiguracaoSistemaService;
import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("configuracaoSistemaService")
public class ConfiguracaoSistemaServiceImpl extends GenericServiceImpl<ConfiguracaoSistema, Long, ConfiguracaoSistemaDao> implements ConfiguracaoSistemaService {

	public ConfiguracaoSistemaServiceImpl(ConfiguracaoSistemaDao dao) {
		super(dao);
	}
	
	public String recuperarValor(String chave) throws ServiceException{
		try {
			return dao.recuperarValor(chave);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public ConfiguracaoSistema recuperarValor(String siglaSistema, String chave)  throws ServiceException{
		try {
			return dao.recuperarValor(siglaSistema, chave);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	

}

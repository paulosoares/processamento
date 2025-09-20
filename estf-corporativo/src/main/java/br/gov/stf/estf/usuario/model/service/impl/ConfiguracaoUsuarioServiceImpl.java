package br.gov.stf.estf.usuario.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.ConfiguracaoUsuario;
import br.gov.stf.estf.usuario.model.dataaccess.ConfiguracaoUsuarioDao;
import br.gov.stf.estf.usuario.model.service.ConfiguracaoUsuarioService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("configuracaoUsuarioService")
public class ConfiguracaoUsuarioServiceImpl extends GenericServiceImpl<ConfiguracaoUsuario, Long, ConfiguracaoUsuarioDao> implements ConfiguracaoUsuarioService {
	
    public ConfiguracaoUsuarioServiceImpl(ConfiguracaoUsuarioDao dao) { 
		super(dao); 
	}
    
    public List<ConfiguracaoUsuario> pesquisar(String siglaUsuario, Long idSetor, Long idTipoConfUsuario, String codChave) throws ServiceException {
    	try {
    		return dao.pesquisar(siglaUsuario, idSetor, idTipoConfUsuario, codChave);
    	} catch (DaoException e) {
    		throw new ServiceException(e);
    	}
    }

}

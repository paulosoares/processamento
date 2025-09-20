package br.gov.stf.estf.usuario.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.TipoConfiguracaoUsuario;
import br.gov.stf.estf.usuario.model.dataaccess.TipoConfiguracaoUsuarioDao;
import br.gov.stf.estf.usuario.model.service.TipoConfiguracaoUsuarioService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoConfiguracaoUsuarioService")
public class TipoConfiguracaoUsuarioServiceImpl  extends GenericServiceImpl<TipoConfiguracaoUsuario, Long, TipoConfiguracaoUsuarioDao> implements TipoConfiguracaoUsuarioService {

    public TipoConfiguracaoUsuarioServiceImpl(TipoConfiguracaoUsuarioDao dao) { 
		super(dao); 
	}
	
}

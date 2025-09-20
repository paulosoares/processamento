package br.gov.stf.estf.usuario.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.CustomizacaoUsuario;
import br.gov.stf.estf.entidade.usuario.TipoCustomizacao;
import br.gov.stf.estf.usuario.model.dataaccess.CustomizacaoUsuarioDao;
import br.gov.stf.estf.usuario.model.service.CustomizacaoUsuarioService;
import br.gov.stf.estf.usuario.model.service.TipoCustomizacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("CustomizacaoUsuarioService")
public class CustomizacaoUsuarioServiceImpl extends GenericServiceImpl<CustomizacaoUsuario, Long, CustomizacaoUsuarioDao> implements CustomizacaoUsuarioService {

	@Autowired
	private TipoCustomizacaoService tipoCustomizacaoService;

	public CustomizacaoUsuarioServiceImpl(CustomizacaoUsuarioDao dao) {
		super(dao);
	}

	public CustomizacaoUsuario retornaCustomizacaoSetor(TipoCustomizacao tipoCustomizacao, Long codSetor) throws ServiceException {
		try {
			return dao.retornaCustomizacaoSetor(tipoCustomizacao, codSetor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}

package br.gov.stf.estf.usuario.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.UsuarioExterno;
import br.gov.stf.estf.usuario.model.dataaccess.UsuarioExternoDao;
import br.gov.stf.estf.usuario.model.service.UsuarioExternoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("usuarioExternoService")
public class UsuarioExternoServiceImpl extends
		GenericServiceImpl<UsuarioExterno, Integer, UsuarioExternoDao>
		implements UsuarioExternoService {

	@Autowired
	public UsuarioExternoServiceImpl(UsuarioExternoDao dao) {
		super(dao);
	}

	public List<UsuarioExterno> pesquisarOrgaosIntegracao()
			throws ServiceException {
		try {
			return dao.usuariosIntegracaoESTF();
		} catch (DaoException e) {
			throw new ServiceException(	"Erro ao realizar pesquisar de Orgãos que realizam Integração.",e);
		}
	}
}
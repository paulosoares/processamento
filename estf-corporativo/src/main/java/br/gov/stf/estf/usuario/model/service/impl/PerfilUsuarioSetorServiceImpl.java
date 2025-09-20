package br.gov.stf.estf.usuario.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.PerfilUsuarioSetor;
import br.gov.stf.estf.entidade.usuario.PerfilUsuarioSetor.PerfilUsuarioSetorId;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.usuario.model.dataaccess.PerfilUsuarioSetorDao;
import br.gov.stf.estf.usuario.model.service.PerfilUsuarioSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("perfilUsuarioSetorService")
public class PerfilUsuarioSetorServiceImpl extends GenericServiceImpl<PerfilUsuarioSetor, PerfilUsuarioSetorId, PerfilUsuarioSetorDao> 
	implements PerfilUsuarioSetorService{
    public PerfilUsuarioSetorServiceImpl(PerfilUsuarioSetorDao dao) { super(dao); }

	public List<PerfilUsuarioSetor> pesquisarSetoresUsuario(Usuario usuario) throws ServiceException {
		try {
			return dao.pesquisarSetoresUsuario(usuario);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<PerfilUsuarioSetor> pesquisarUsuarioDoSetor(String nome, Long idSetor) throws ServiceException {
		try {
			return dao.pesquisarUsuarioDoSetor(nome, idSetor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<PerfilUsuarioSetor> pesquisarUsuarioDoSetorSistema(String nome, Long idSetor, String sistema) throws ServiceException {
		try {
			return dao.pesquisarUsuarioDoSetorSistema(nome, idSetor, sistema);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<PerfilUsuarioSetor> pesquisarSetoresUsuario(Usuario usuario,
			String siglaSistema) throws ServiceException {
		try {
			return dao.pesquisarSetoresUsuario(usuario, siglaSistema);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}


}

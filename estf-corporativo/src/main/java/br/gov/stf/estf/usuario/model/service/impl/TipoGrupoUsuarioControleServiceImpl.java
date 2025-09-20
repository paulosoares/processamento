package br.gov.stf.estf.usuario.model.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.TipoGrupoControle;
import br.gov.stf.estf.entidade.usuario.TipoGrupoUsuarioControle;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.usuario.model.dataaccess.TipoGrupoUsuarioControleDao;
import br.gov.stf.estf.usuario.model.service.TipoGrupoUsuarioControleService;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoGrupoUsuarioControleService")
public class TipoGrupoUsuarioControleServiceImpl extends GenericServiceImpl<TipoGrupoUsuarioControle, Long, TipoGrupoUsuarioControleDao> implements
		TipoGrupoUsuarioControleService {
	
	private final UsuarioService usuarioService;
	
	public TipoGrupoUsuarioControleServiceImpl(TipoGrupoUsuarioControleDao dao, UsuarioService usuarioService) {
		super(dao);
		this.usuarioService = usuarioService;
	}

	public List<TipoGrupoControle> buscaGruposDoUsuario(String sigUsuario)
			throws ServiceException {
		
		List<TipoGrupoControle> listaGrupos = null;
		try {
			listaGrupos = dao.buscaGruposDoUsuario(sigUsuario);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return listaGrupos;
	}

	public List<TipoGrupoUsuarioControle> pesquisarGrupoUsuarioControle(String siglaUsuario, Long idTipoGrupoControle)
		throws ServiceException {
		
		List<TipoGrupoUsuarioControle> listaGrupoUsuario = null;
		Usuario usuario = null;
		
		if (siglaUsuario != null){
			try {
				usuario = usuarioService.recuperarPorId(siglaUsuario);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		
		try {
			listaGrupoUsuario = dao.pesquisarGrupoUsuarioControle(usuario, idTipoGrupoControle);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return listaGrupoUsuario;
	}
	
	
	public List<Usuario> pesquisarUsuarioPeloGrupoControle(Long idTipoGrupoControle)
	throws ServiceException {
	
	List<String> listaNomeUsuario = null;
	List<Usuario> listaUsuario = new LinkedList<Usuario>();
		
	try {
		listaNomeUsuario = dao.pesquisarUsuarioPeloGrupoControle(idTipoGrupoControle);
	} catch (DaoException e) {
		throw new ServiceException(e);
	}
	
	if (listaNomeUsuario != null && listaNomeUsuario.size() > 0){
		for (String usu : listaNomeUsuario){
			listaUsuario.add(usuarioService.recuperarPorId(usu));
		}
	}
	
	return listaUsuario;
}
	
	
	public void apagarGrupoUsuarioControle(TipoGrupoUsuarioControle tguc) throws ServiceException{
		try {
			dao.apagarGrupoUsuarioControle(tguc);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

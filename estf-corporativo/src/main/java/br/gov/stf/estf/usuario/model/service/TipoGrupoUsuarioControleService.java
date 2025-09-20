package br.gov.stf.estf.usuario.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.TipoGrupoControle;
import br.gov.stf.estf.entidade.usuario.TipoGrupoUsuarioControle;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.usuario.model.dataaccess.TipoGrupoUsuarioControleDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoGrupoUsuarioControleService extends GenericService<TipoGrupoUsuarioControle, Long, TipoGrupoUsuarioControleDao>{
	
	public List<TipoGrupoUsuarioControle> pesquisarGrupoUsuarioControle (String siglaUsuario, Long idTipoGrupoControle) throws ServiceException;
	
	public List<Usuario> pesquisarUsuarioPeloGrupoControle(Long idTipoGrupoControle) throws ServiceException;
	
	public List<TipoGrupoControle> buscaGruposDoUsuario(String sigUsuario) throws ServiceException;
	
	public void apagarGrupoUsuarioControle(TipoGrupoUsuarioControle tguc) throws ServiceException;

}

package br.gov.stf.estf.usuario.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.TipoGrupoControle;
import br.gov.stf.estf.entidade.usuario.TipoGrupoUsuarioControle;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoGrupoUsuarioControleDao extends GenericDao<TipoGrupoUsuarioControle, Long>{
	
	public List<TipoGrupoControle>buscaGruposDoUsuario (String sigUsuario) throws DaoException;
	
	public List<TipoGrupoUsuarioControle> pesquisarGrupoUsuarioControle(Usuario usuario, Long idTipoGrupoControle) throws DaoException;
	
	public List<String> pesquisarUsuarioPeloGrupoControle(Long idTipoGrupoControle) throws DaoException;
	
	public void apagarGrupoUsuarioControle(TipoGrupoUsuarioControle tguc) throws DaoException;

}

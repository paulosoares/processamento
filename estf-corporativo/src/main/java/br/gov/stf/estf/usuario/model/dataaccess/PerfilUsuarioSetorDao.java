package br.gov.stf.estf.usuario.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.PerfilUsuarioSetor;
import br.gov.stf.estf.entidade.usuario.PerfilUsuarioSetor.PerfilUsuarioSetorId;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PerfilUsuarioSetorDao extends GenericDao<PerfilUsuarioSetor, PerfilUsuarioSetorId>{
	
	public List<PerfilUsuarioSetor> pesquisarSetoresUsuario(Usuario usuario) throws DaoException;

	public List<PerfilUsuarioSetor> pesquisarUsuarioDoSetor(String nome, Long idSetor) throws DaoException;

	public List<PerfilUsuarioSetor> pesquisarSetoresUsuario(Usuario usuario, String siglaSistema) throws DaoException;

	public List<PerfilUsuarioSetor> pesquisarUsuarioDoSetorSistema(String nome, Long idSetor, String sistema) throws DaoException;
	
}

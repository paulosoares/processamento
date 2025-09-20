package br.gov.stf.estf.usuario.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.ConfiguracaoUsuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ConfiguracaoUsuarioDao extends GenericDao<ConfiguracaoUsuario, Long>{

	public List<ConfiguracaoUsuario> pesquisar(String siglaUsuario, Long idSetor, Long idTipoConfUsuario, String codChave) throws DaoException;
	
}

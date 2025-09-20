package br.gov.stf.estf.usuario.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.UsuarioExterno;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface UsuarioExternoDao extends GenericDao<UsuarioExterno, Integer> {

	public List<UsuarioExterno> usuariosIntegracaoESTF() throws DaoException;
}

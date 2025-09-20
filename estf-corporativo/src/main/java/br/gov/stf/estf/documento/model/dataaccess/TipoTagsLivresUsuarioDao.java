package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.TipoTagsLivresUsuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoTagsLivresUsuarioDao extends GenericDao<TipoTagsLivresUsuario, Long>{
	
	public List<TipoTagsLivresUsuario> pesquisar(String nome) throws DaoException;

}

package br.gov.stf.estf.usuario.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.TipoGrupoControle;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoGrupoControleDao extends GenericDao<TipoGrupoControle, Long>{
	
	public List<TipoGrupoControle> pesquisarTipoGrupoControle(String nomeGrupo) throws DaoException;
	
}

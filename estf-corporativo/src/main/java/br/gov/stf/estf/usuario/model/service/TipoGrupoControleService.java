package br.gov.stf.estf.usuario.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.TipoGrupoControle;
import br.gov.stf.estf.usuario.model.dataaccess.TipoGrupoControleDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoGrupoControleService extends GenericService<TipoGrupoControle, Long, TipoGrupoControleDao>{
	
	public List<TipoGrupoControle> pesquisarTipoGrupoControle(String nomeGrupo) throws ServiceException;
	
}

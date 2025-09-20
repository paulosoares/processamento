package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.TipoRecurso;
import br.gov.stf.estf.processostf.model.dataaccess.TipoRecursoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoRecursoService extends GenericService<TipoRecurso, Long, TipoRecursoDao> {
	
	public TipoRecurso recuperarTipoRecurso(Long id) throws ServiceException;

	public TipoRecurso recuperarTipoRecurso(String sigla) throws ServiceException;
	
	public List<TipoRecurso> recuperarTiposRecurso(boolean ativos) throws ServiceException;

}

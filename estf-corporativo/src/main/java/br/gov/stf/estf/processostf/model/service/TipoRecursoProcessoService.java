package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.TipoRecursoProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.TipoRecursoProcessoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoRecursoProcessoService extends GenericService<TipoRecursoProcesso, Long, TipoRecursoProcessoDao> {
	
	public List<TipoRecursoProcesso> pesquisar(Boolean ativo) throws ServiceException;

}

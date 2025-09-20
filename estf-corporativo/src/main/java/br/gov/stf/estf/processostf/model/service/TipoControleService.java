/**
 * 
 */
package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.TipoControle;
import br.gov.stf.estf.processostf.model.dataaccess.TipoControleDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoControleService extends GenericService<TipoControle, Long, TipoControleDao> {

	public List<TipoControle> pesquisarTodosControles() throws ServiceException;
	
	TipoControle recuperarPorSigla(String sigla) throws ServiceException;

}

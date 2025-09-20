/**
 * 
 */
package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.processostf.TipoJulgamento;
import br.gov.stf.estf.processostf.model.dataaccess.TipoJulgamentoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 25.11.2010
 */
public interface TipoJulgamentoService extends GenericService<TipoJulgamento, String, TipoJulgamentoDao> {
	
	TipoJulgamento recuperarTipoJulgamento(Long seqTipoRecurso, Long sequenciaCadeia) throws ServiceException;
}

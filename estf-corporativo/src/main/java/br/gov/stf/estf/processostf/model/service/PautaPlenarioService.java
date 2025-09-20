/**
 * 
 */
package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.PautaPlenario;
import br.gov.stf.estf.processostf.model.dataaccess.PautaPlenarioDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 08.08.2011
 */
public interface PautaPlenarioService extends GenericService<PautaPlenario, Long, PautaPlenarioDao> {

	List<PautaPlenario> recuperarTodos() throws ServiceException;
}

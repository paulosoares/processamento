/**
 * 
 */
package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.PautaPlenario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 08.08.2011
 */
public interface PautaPlenarioDao extends GenericDao<PautaPlenario, Long> {

	List<PautaPlenario> recuperarTodos() throws DaoException;
}

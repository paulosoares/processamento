/**
 * 
 */
package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.processostf.TipoJulgamento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 25.11.2010
 */
public interface TipoJulgamentoDao extends GenericDao<TipoJulgamento, String> {

	public TipoJulgamento recuperarTipoJulgamento(Long seqTipoRecurso, Long sequenciaCadeia) throws DaoException;
}

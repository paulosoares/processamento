/**
 * 
 */
package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.processostf.TipoSituacaoControle;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoSituacaoControleDao extends GenericDao<TipoSituacaoControle, Long> {

	public TipoSituacaoControle pesquisarSituacaoControle(String descricao) throws DaoException;
	
	TipoSituacaoControle recuperarPorCodigo(String codigoTipoSituacaoControle) throws DaoException;
}

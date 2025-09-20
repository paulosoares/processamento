/**
 * 
 */
package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.processostf.TipoSituacaoControle;
import br.gov.stf.estf.processostf.model.dataaccess.TipoSituacaoControleDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoSituacaoControleService extends GenericService<TipoSituacaoControle, Long, TipoSituacaoControleDao> {

	public TipoSituacaoControle pesquisarSituacaoControle(String descricao) throws ServiceException;
	
	TipoSituacaoControle recuperarPorCodigo(String codigoTipoSituacaoControle) throws ServiceException;

}

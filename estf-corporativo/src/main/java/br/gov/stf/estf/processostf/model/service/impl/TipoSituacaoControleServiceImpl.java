/**
 * 
 */
package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.TipoSituacaoControle;
import br.gov.stf.estf.processostf.model.dataaccess.TipoSituacaoControleDao;
import br.gov.stf.estf.processostf.model.service.TipoSituacaoControleService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoSituacaoControleService")
public class TipoSituacaoControleServiceImpl extends GenericServiceImpl<TipoSituacaoControle, Long, TipoSituacaoControleDao> implements
		TipoSituacaoControleService {

	protected TipoSituacaoControleServiceImpl(TipoSituacaoControleDao dao) {
		super(dao);
	}
	
	public TipoSituacaoControle pesquisarSituacaoControle(String descricao) throws ServiceException {
		TipoSituacaoControle tipo = null;
		try {
			tipo = dao.pesquisarSituacaoControle(descricao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return tipo;
	}

	@Override
	public TipoSituacaoControle recuperarPorCodigo(String codigoTipoSituacaoControle) throws ServiceException {
		try {
			return dao.recuperarPorCodigo(codigoTipoSituacaoControle);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

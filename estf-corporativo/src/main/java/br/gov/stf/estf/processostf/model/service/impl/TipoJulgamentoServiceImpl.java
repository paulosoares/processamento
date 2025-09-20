/**
 * 
 */
package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.TipoJulgamento;
import br.gov.stf.estf.processostf.model.dataaccess.TipoJulgamentoDao;
import br.gov.stf.estf.processostf.model.service.TipoJulgamentoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 25.11.2010
 */
@Service("tipoJulgamentoService")
public class TipoJulgamentoServiceImpl extends
		GenericServiceImpl<TipoJulgamento, String, TipoJulgamentoDao> implements TipoJulgamentoService {

	protected TipoJulgamentoServiceImpl(TipoJulgamentoDao dao) {
		super(dao);
	}

	@Override
	public TipoJulgamento recuperarTipoJulgamento(Long seqTipoRecurso,
			Long sequenciaCadeia) throws ServiceException {
		try {
			return dao.recuperarTipoJulgamento(seqTipoRecurso, sequenciaCadeia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

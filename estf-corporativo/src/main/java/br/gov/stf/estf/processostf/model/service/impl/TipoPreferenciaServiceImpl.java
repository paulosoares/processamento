/**
 * 
 */
package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import br.gov.stf.estf.entidade.processostf.TipoPreferencia;
import br.gov.stf.estf.processostf.model.dataaccess.TipoPreferenciaDao;
import br.gov.stf.estf.processostf.model.service.TipoPreferenciaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 08.08.2011
 */
@Service("tipoPreferenciaService")
public class TipoPreferenciaServiceImpl extends
		GenericServiceImpl<TipoPreferencia, Long, TipoPreferenciaDao> implements TipoPreferenciaService {

	protected TipoPreferenciaServiceImpl(TipoPreferenciaDao dao) {
		super(dao);
	}

	@Override
	public  TipoPreferencia recuperarTipoPreferencia(Long idTipoPreferencia) throws ServiceException {
		try {
			return dao.recuperarTipoPreferencia(idTipoPreferencia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public  List<TipoPreferencia> recuperarTipoPreferenciaODS() throws ServiceException {
		try {
			return dao.recuperarTipoPreferenciaODS();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
}

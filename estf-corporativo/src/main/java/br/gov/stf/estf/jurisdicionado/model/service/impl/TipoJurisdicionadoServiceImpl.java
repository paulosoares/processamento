package br.gov.stf.estf.jurisdicionado.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisdicionado.TipoJurisdicionado;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.TipoJurisdicionadoDao;
import br.gov.stf.estf.jurisdicionado.model.service.TipoJurisdicionadoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoJurisdicionadoService")
public class TipoJurisdicionadoServiceImpl extends
	GenericServiceImpl<TipoJurisdicionado, Long, TipoJurisdicionadoDao> implements TipoJurisdicionadoService {

	public TipoJurisdicionadoServiceImpl(TipoJurisdicionadoDao dao) {
		super(dao);
	}
	
	
	@Override
	public TipoJurisdicionado buscaTipoPelaSigla(String sigla) throws ServiceException {
		try {
			return dao.buscaTipoPelaSigla(sigla);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

package br.gov.stf.estf.jurisdicionado.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisdicionado.PapelJurisdicionado;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.PapelJurisdicionadoDao;
import br.gov.stf.estf.jurisdicionado.model.service.PapelJurisdicionadoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("papelJurisdicionadoService")
public class PapelJurisdicionadoServiceImpl extends
		GenericServiceImpl<PapelJurisdicionado, Long, PapelJurisdicionadoDao> implements PapelJurisdicionadoService {

	public PapelJurisdicionadoServiceImpl(PapelJurisdicionadoDao dao) {
		super(dao);
	}
	
	@Override
	public PapelJurisdicionado recuperarPorId(Long id) throws ServiceException {
		try {
			return dao.recuperarPorId(id);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


}

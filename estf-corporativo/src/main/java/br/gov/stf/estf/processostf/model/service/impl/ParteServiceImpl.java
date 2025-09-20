package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.processostf.model.dataaccess.ParteDao;
import br.gov.stf.estf.processostf.model.service.ParteService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("parteService")
public class ParteServiceImpl extends GenericServiceImpl<Parte, Long, ParteDao> 
		implements ParteService {
	
	public ParteServiceImpl(ParteDao dao) { 
		super(dao); 
	}

	@Override
	public List<Parte> pesquisarPartes(Long idObjetoIncidente)
			throws ServiceException {
		try {
			return dao.pesquisarPartes(idObjetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<Parte> pesquisarPartes(Long idObjetoIncidente, List<Long> codigosCategoria)
		throws ServiceException{
		
		try {
			return dao.pesquisarPartes(idObjetoIncidente, codigosCategoria);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}

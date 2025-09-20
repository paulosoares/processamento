package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.PrevisaoImpedimentoMinistro;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.PrevisaoImpedimentoMinistroDao;
import br.gov.stf.estf.processostf.model.service.PrevisaoImpedimentoMinistroService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;


@Service("previsaoImpedimentoMinistroService")
public class PrevisaoImpedimentoMinistroServiceImpl extends GenericServiceImpl<PrevisaoImpedimentoMinistro, Long, PrevisaoImpedimentoMinistroDao> implements  PrevisaoImpedimentoMinistroService {

	protected PrevisaoImpedimentoMinistroServiceImpl(PrevisaoImpedimentoMinistroDao dao) {
		super(dao);
	}
	
	@Override
	public List<PrevisaoImpedimentoMinistro> recuperar(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			return dao.recuperar( objetoIncidente );
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public PrevisaoImpedimentoMinistro recuperar(Ministro ministro, ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			return dao.recuperar(ministro, objetoIncidente);
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	
}
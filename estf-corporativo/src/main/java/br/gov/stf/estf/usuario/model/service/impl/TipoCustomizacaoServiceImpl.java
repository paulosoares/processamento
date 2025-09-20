package br.gov.stf.estf.usuario.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.TipoCustomizacao;
import br.gov.stf.estf.usuario.model.dataaccess.TipoCustomizacaoDao;
import br.gov.stf.estf.usuario.model.service.TipoCustomizacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("TipoCustomizacaoService")
public class TipoCustomizacaoServiceImpl extends GenericServiceImpl<TipoCustomizacao, Long, TipoCustomizacaoDao> implements TipoCustomizacaoService {

	   public TipoCustomizacaoServiceImpl(TipoCustomizacaoDao dao) { 
			super(dao); 
		}

	@Override
	public TipoCustomizacao buscaPorDscParametro(String dscParametro) throws ServiceException {
    	try {
    		return dao.buscaPorDscParametro(dscParametro);
    	} catch (DaoException e) {
    		throw new ServiceException(e);
    	}
	}
}

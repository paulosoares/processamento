package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.TipoListaJulgamento;
import br.gov.stf.estf.julgamento.model.dataaccess.TipoListaJulgamentoDao;
import br.gov.stf.estf.julgamento.model.service.TipoListaJulgamentoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;


@Service("tipoListaJulgamentoService")
public class TipoListaJulgamentoServiceImpl extends GenericServiceImpl<TipoListaJulgamento, Long, TipoListaJulgamentoDao> implements TipoListaJulgamentoService {
    
    public TipoListaJulgamentoServiceImpl(TipoListaJulgamentoDao dao) { super(dao); }

	@Override
	public List<TipoListaJulgamento> recuperarTipoListaJulgamentoAtivas() throws ServiceException {
		try {
			return dao.recuperarTipoListaJulgamentoAtivas();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	} 

}

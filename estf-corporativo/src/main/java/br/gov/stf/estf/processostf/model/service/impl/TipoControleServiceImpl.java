package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.TipoControle;
import br.gov.stf.estf.processostf.model.dataaccess.TipoControleDao;
import br.gov.stf.estf.processostf.model.service.TipoControleService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoControleService")
public class TipoControleServiceImpl extends GenericServiceImpl<TipoControle, Long, TipoControleDao> implements
		TipoControleService {

	protected TipoControleServiceImpl(TipoControleDao dao) {
		super(dao);
	}
	
	public List<TipoControle> pesquisarTodosControles() throws ServiceException {
		List<TipoControle> listaControle = null;
		try {
			listaControle = dao.pesquisarTodosControles();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return listaControle;
	}

	@Override
	public TipoControle recuperarPorSigla(String sigla) throws ServiceException {
		try {
			return dao.recuperarPorSigla(sigla);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}

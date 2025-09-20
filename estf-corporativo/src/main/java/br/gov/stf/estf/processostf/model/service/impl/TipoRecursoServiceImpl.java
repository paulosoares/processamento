package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.TipoRecurso;
import br.gov.stf.estf.processostf.model.dataaccess.TipoRecursoDao;
import br.gov.stf.estf.processostf.model.service.TipoRecursoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoRecursoService")
public class TipoRecursoServiceImpl extends GenericServiceImpl<TipoRecurso, Long, TipoRecursoDao> implements TipoRecursoService {

	public static final String MERITO = "M";

	public TipoRecursoServiceImpl(TipoRecursoDao dao) {
		super(dao);
	}

	public TipoRecurso recuperarTipoRecurso(Long id) throws ServiceException {
		try {
			return dao.recuperarTipoRecurso(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public TipoRecurso recuperarTipoRecurso(String sigla) throws ServiceException {
		try {
			return dao.recuperarTipoRecurso(sigla);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<TipoRecurso> recuperarTiposRecurso(boolean ativos) throws ServiceException {
		try {
			return dao.recuperarTipoRecurso(ativos);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

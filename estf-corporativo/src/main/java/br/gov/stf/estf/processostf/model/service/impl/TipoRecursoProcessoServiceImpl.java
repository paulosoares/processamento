package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.TipoRecursoProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.TipoRecursoProcessoDao;
import br.gov.stf.estf.processostf.model.service.TipoRecursoProcessoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoRecursoProcessoService")
public class TipoRecursoProcessoServiceImpl extends GenericServiceImpl<TipoRecursoProcesso, Long, TipoRecursoProcessoDao>
		implements TipoRecursoProcessoService {

	protected TipoRecursoProcessoServiceImpl(TipoRecursoProcessoDao dao) {
		super(dao);
	}

	public List<TipoRecursoProcesso> pesquisar(Boolean ativo) throws ServiceException {
		try {
			return dao.pesquisar(ativo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

package br.gov.stf.estf.processostf.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.RecursoProcessoDao;
import br.gov.stf.estf.processostf.model.service.RecursoProcessoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("recursoProcessoService")
public class RecursoProcessoServiceImpl extends GenericServiceImpl<RecursoProcesso, Long, RecursoProcessoDao> implements
		RecursoProcessoService {
	public RecursoProcessoServiceImpl(RecursoProcessoDao dao) {
		super(dao);
	}

	public List<RecursoProcesso> pesquisar(String siglaProcessual, Long numeroProcessual) throws ServiceException {
		List<RecursoProcesso> recursos = null;
		try {
			recursos = dao.pesquisar(siglaProcessual, numeroProcessual);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return recursos;
	}

	@Override
	public Long pesquisar(Processo processo, Date data) throws ServiceException {
		try {
			return dao.pesquisar(processo, data);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

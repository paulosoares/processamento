package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ComplementoParte;
import br.gov.stf.estf.entidade.processostf.ComplementoParte.ComplementoParteId;
import br.gov.stf.estf.processostf.model.dataaccess.ComplementoParteDao;
import br.gov.stf.estf.processostf.model.service.ComplementoParteService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("complementoParteService")
public class ComplementoParteServiceImpl extends GenericServiceImpl<ComplementoParte, ComplementoParteId, ComplementoParteDao> 
	implements ComplementoParteService {
    public ComplementoParteServiceImpl(ComplementoParteDao dao) { super(dao); }

	public List<ComplementoParte> pesquisar(Long codigoParte)
			throws ServiceException {
		List<ComplementoParte> resp = null;
		try {
			resp = dao.pesquisar(codigoParte);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return resp;
	}

}

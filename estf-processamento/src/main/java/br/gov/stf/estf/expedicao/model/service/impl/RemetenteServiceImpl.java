package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.expedicao.entidade.Remetente;
import br.gov.stf.estf.expedicao.model.dataaccess.RemetenteDao;
import br.gov.stf.estf.expedicao.model.service.RemetenteService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("remetenteService")
public class RemetenteServiceImpl implements RemetenteService {

    public static final long serialVersionUID = 1L;

    private RemetenteDao dao;

    public RemetenteServiceImpl(RemetenteDao dao) {
        this.dao = dao;
    }

	@Override
	public List<Remetente> listarTodos() throws ServiceException {
		try {
			return dao.listarTodos();
		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
	}
}
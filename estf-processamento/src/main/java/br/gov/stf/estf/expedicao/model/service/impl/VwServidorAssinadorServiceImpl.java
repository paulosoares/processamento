package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.expedicao.entidade.VwServidorAssinador;
import br.gov.stf.estf.expedicao.model.dataaccess.VwServidorAssinadorDao;
import br.gov.stf.estf.expedicao.model.service.VwServidorAssinadorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("vwServidorAssinadorService")
public class VwServidorAssinadorServiceImpl implements VwServidorAssinadorService {

    public static final long serialVersionUID = 1L;

    protected VwServidorAssinadorDao dao;

    public VwServidorAssinadorServiceImpl(VwServidorAssinadorDao dao) {
        this.dao = dao;
    }

    @Override
    public List<VwServidorAssinador> listar() throws ServiceException {
        try {
            return dao.listar();
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }
}
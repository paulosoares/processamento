package br.gov.stf.estf.corp.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.corp.entidade.UnidadeFederacao;
import br.gov.stf.estf.corp.model.dataaccess.UnidadeFederacaoDao;
import br.gov.stf.estf.corp.model.service.UnidadeFederacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("unidadeFederacaoService")
public class UnidadeFederacaoServiceImpl implements UnidadeFederacaoService {

    public static final long serialVersionUID = 1L;

    protected UnidadeFederacaoDao dao;

    public UnidadeFederacaoServiceImpl(UnidadeFederacaoDao dao) {
        this.dao = dao;
    }

    @Override
    public List<UnidadeFederacao> listarAtivos() throws ServiceException {
        try {
            return dao.listarAtivos();
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }
}
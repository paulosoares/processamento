package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.expedicao.entidade.UnidadePostagem;
import br.gov.stf.estf.expedicao.model.dataaccess.UnidadePostagemDao;
import br.gov.stf.estf.expedicao.model.service.UnidadePostagemService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("unidadePostagemService")
public class UnidadePostagemServiceImpl implements UnidadePostagemService {

    public static final long serialVersionUID = 1L;

    protected UnidadePostagemDao dao;

    public UnidadePostagemServiceImpl(UnidadePostagemDao dao) {
        this.dao = dao;
    }

    @Override
    public List<UnidadePostagem> listarAtivos() throws ServiceException {
        try {
            return dao.listarAtivos();
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }
}
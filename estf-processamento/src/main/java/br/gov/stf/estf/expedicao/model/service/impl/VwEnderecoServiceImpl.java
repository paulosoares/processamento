package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.expedicao.entidade.VwEndereco;
import br.gov.stf.estf.expedicao.model.dataaccess.VwEnderecoDao;
import br.gov.stf.estf.expedicao.model.service.VwEnderecoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("vwEnderecoService")
public class VwEnderecoServiceImpl implements VwEnderecoService {

    public static final long serialVersionUID = 1L;

    protected VwEnderecoDao dao;

    public VwEnderecoServiceImpl(VwEnderecoDao dao) {
        this.dao = dao;
    }

    @Override
    public List<VwEndereco> pesquisar(String cep) throws ServiceException {
        try {
            return dao.pesquisar(cep);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    @Override
    public List<VwEndereco> pesquisar(VwEndereco vwEndereco) throws ServiceException {
        try {
            return dao.pesquisar(vwEndereco);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }
}
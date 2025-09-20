package br.gov.stf.estf.corp.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.corp.entidade.Logradouro;
import br.gov.stf.estf.corp.model.dataaccess.LogradouroDao;
import br.gov.stf.estf.corp.model.service.LogradouroService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("logradouroService")
public class LogradouroServiceImpl implements LogradouroService {

    public static final long serialVersionUID = 1L;

    protected LogradouroDao dao;

    public LogradouroServiceImpl(LogradouroDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Logradouro> pesquisarPeloCep(String cep) throws ServiceException {
        try {
            return dao.pesquisarPeloCep(cep);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }
}
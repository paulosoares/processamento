package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.expedicao.entidade.ConfiguracaoEncaminhamento;
import br.gov.stf.estf.expedicao.model.dataaccess.ConfiguracaoEncaminhamentoDao;
import br.gov.stf.estf.expedicao.model.service.ConfiguracaoEncaminhamentoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("configuracaoEncaminhamentoService")
public class ConfiguracaoEncaminhamentoServiceImpl implements ConfiguracaoEncaminhamentoService {

    public static final long serialVersionUID = 1L;

    protected ConfiguracaoEncaminhamentoDao dao;

    public ConfiguracaoEncaminhamentoServiceImpl(ConfiguracaoEncaminhamentoDao dao) {
        this.dao = dao;
    }

    @Override
    public List<ConfiguracaoEncaminhamento> listar() throws ServiceException {
        try {
        	return dao.listar();
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    @Override
    public ConfiguracaoEncaminhamento buscar(Long codigo) throws ServiceException {
        try {
            return dao.buscar(codigo);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }
}
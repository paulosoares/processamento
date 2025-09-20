package br.gov.stf.estf.intimacao.model.service.impl;

import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.estf.intimacao.model.dataaccess.ConfiguracaoSistemaLocalDao;
import br.gov.stf.estf.intimacao.model.service.ConfiguracaoSistemaLocalService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Roberio.Fernandes
 */
@Service("configuracaoSistemaLocalService")
public class ConfiguracaoSistemaLocalServiceImpl implements ConfiguracaoSistemaLocalService {

    public static final long serialVersionUID = 1L;

    @Autowired
    private ConfiguracaoSistemaLocalDao configuracaoSistemaDaoLocal;

    @Override
    public ConfiguracaoSistema salvarValor(String siglaSistema, String chave, String valor) throws ServiceException {
        try {
            return configuracaoSistemaDaoLocal.salvarValor(siglaSistema, chave, valor);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }
}
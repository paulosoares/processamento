package br.gov.stf.estf.intimacao.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.estf.intimacao.model.dataaccess.ModeloComunicacaoLocalDao;
import br.gov.stf.estf.intimacao.model.service.ModeloComunicacaoLocalService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

/**
 *
 * @author Roberio.Fernandes
 */
@Service("modeloComunicacaoLocalService")
public class ModeloComunicacaoLocalServiceImpl implements ModeloComunicacaoLocalService {

    public static final long serialVersionUID = 1L;

    @Autowired
    private ModeloComunicacaoLocalDao dao;

    @Override
    public ModeloComunicacao buscar(ModeloComunicacaoEnum modeloComunicacaoEnum) throws ServiceException {
        try {
        	if (ModeloComunicacaoEnum.NOTIFICACAO_DE_PAUTA.equals(modeloComunicacaoEnum))
    			return ModeloComunicacao.INCLUIDO_NA_PAUTA;
        	
        	String descricaoTipoComunicacao = modeloComunicacaoEnum.getDescricaoTipoComunicacao();
        	String descricaoModelo = modeloComunicacaoEnum.getDescricaoModelo();
            return dao.buscar(descricaoTipoComunicacao, descricaoModelo);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }
}
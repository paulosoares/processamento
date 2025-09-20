package br.gov.stf.estf.intimacao.model.service;

import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.framework.model.service.ServiceException;

/**
 *
 * @author Roberio.Fernandes
 */
public interface ModeloComunicacaoLocalService {

    /**
     * Busca no banco de dados um modelo pelo tipo de comunicação e descrição.
     *
     * @param descricaoTipoComunicacao
     * @param descricaoModelo
     * @return
     * @throws ServiceException 
     */
    ModeloComunicacao buscar(ModeloComunicacaoEnum modeloComunicacaoEnum) throws ServiceException;
}
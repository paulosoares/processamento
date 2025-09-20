package br.gov.stf.estf.intimacao.model.service;

import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.intimacao.model.service.exception.TipoComunicacaoComDescricaoDiferenteUmException;
import br.gov.stf.framework.model.service.ServiceException;

/**
 *
 * @author Roberio.Fernandes
 */
public interface TipoComunicacaoLocalService {

    /**
     * Retorna o tipo de comunicação com a descrição informada.
     *
     * @param descricao
     * @return 
     * @throws br.gov.stf.estf.intimacao.model.service.exception.TipoComunicacaoComDescricaoDiferenteUmException 
     * @throws br.gov.stf.framework.model.service.ServiceException 
     */
    TipoComunicacao buscar(String descricao) throws TipoComunicacaoComDescricaoDiferenteUmException, ServiceException;

    /**
     * Gera o próximo número de comunicação para o tipo de comunicação informado.
     *
     * @param idTipoComunicacao
     * @return
     * @throws ServiceException 
     */
    Long gerarProximoNumeroComunicacao(Long idTipoComunicacao) throws ServiceException;
}
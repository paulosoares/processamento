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
     * Retorna o tipo de comunica��o com a descri��o informada.
     *
     * @param descricao
     * @return 
     * @throws br.gov.stf.estf.intimacao.model.service.exception.TipoComunicacaoComDescricaoDiferenteUmException 
     * @throws br.gov.stf.framework.model.service.ServiceException 
     */
    TipoComunicacao buscar(String descricao) throws TipoComunicacaoComDescricaoDiferenteUmException, ServiceException;

    /**
     * Gera o pr�ximo n�mero de comunica��o para o tipo de comunica��o informado.
     *
     * @param idTipoComunicacao
     * @return
     * @throws ServiceException 
     */
    Long gerarProximoNumeroComunicacao(Long idTipoComunicacao) throws ServiceException;
}
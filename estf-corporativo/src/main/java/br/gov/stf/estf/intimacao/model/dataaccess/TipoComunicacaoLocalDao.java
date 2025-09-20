package br.gov.stf.estf.intimacao.model.dataaccess;

import br.gov.stf.framework.model.dataaccess.DaoException;

/**
 *
 * @author Roberio.Fernandes
 */
public interface TipoComunicacaoLocalDao {

    /**
     * Gera o próximo número de comunicação para o tipo de comunicação informado.
     *
     * @param idTipoComunicacao
     * @return
     * @throws DaoException 
     */
    Long gerarProximoNumeroComunicacao(Long idTipoComunicacao) throws DaoException;
}
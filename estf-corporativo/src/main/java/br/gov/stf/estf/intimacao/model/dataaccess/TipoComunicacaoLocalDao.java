package br.gov.stf.estf.intimacao.model.dataaccess;

import br.gov.stf.framework.model.dataaccess.DaoException;

/**
 *
 * @author Roberio.Fernandes
 */
public interface TipoComunicacaoLocalDao {

    /**
     * Gera o pr�ximo n�mero de comunica��o para o tipo de comunica��o informado.
     *
     * @param idTipoComunicacao
     * @return
     * @throws DaoException 
     */
    Long gerarProximoNumeroComunicacao(Long idTipoComunicacao) throws DaoException;
}
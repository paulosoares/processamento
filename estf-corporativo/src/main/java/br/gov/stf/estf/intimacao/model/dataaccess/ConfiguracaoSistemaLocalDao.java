package br.gov.stf.estf.intimacao.model.dataaccess;

import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.framework.model.dataaccess.DaoException;

/**
 *
 * @author Roberio.Fernandes
 */
public interface ConfiguracaoSistemaLocalDao {

    /**
     * Salva na tabela de DNA o valor passado, com a chave informada. Caso esta chave não exista
     * um novo registro será criado.
     *
     * @param siglaSistema
     * @param chave
     * @param valor
     * @return
     * @throws DaoException 
     */
    ConfiguracaoSistema salvarValor(String siglaSistema, String chave, String valor) throws DaoException;
}
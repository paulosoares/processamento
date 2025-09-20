package br.gov.stf.estf.intimacao.model.dataaccess;

import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.framework.model.dataaccess.DaoException;

/**
 *
 * @author Roberio.Fernandes
 */
public interface ModeloComunicacaoLocalDao {

    /**
     * Busca no banco de dados um modelo pelo tipo de comunicação e descrição.
     *
     * @param descricaoTipoComunicacao
     * @param descricaoModelo
     * @return
     * @throws DaoException 
     */
    ModeloComunicacao buscar(String descricaoTipoComunicacao, String descricaoModelo) throws DaoException;

    /**
     * Busca no banco de dados um modelo pelo tipo de comunicação e descrição definidos no enum.
     *
     * @param modeloComunicacaoEnum
     * @return
     * @throws DaoException 
     */
    ModeloComunicacao buscar(ModeloComunicacaoEnum modeloComunicacaoEnum) throws DaoException;
}
package br.gov.stf.estf.corp.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.corp.entidade.UnidadeFederacao;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface UnidadeFederacaoDao {

    /**
     * Lista todos os registros de unidade de federação ativos.
     *
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<UnidadeFederacao> listarAtivos() throws DaoException;
}
package br.gov.stf.estf.corp.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.corp.entidade.Logradouro;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface LogradouroDao {

    /**
     * Pesquisa todos os registros de logradouro que possuírem este número no CEP.
     *
     * @param cep
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<Logradouro> pesquisarPeloCep(String cep) throws DaoException;
}
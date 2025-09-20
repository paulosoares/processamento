package br.gov.stf.estf.expedicao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.Remetente;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface RemetenteDao {

    /**
     * Lista todos os remetentes cadastraados.
     *
     * @return 
     * @throws br.gov.stf.framework.model.dataaccess.DaoException 
     */
    List<Remetente> listarTodos() throws DaoException;
}
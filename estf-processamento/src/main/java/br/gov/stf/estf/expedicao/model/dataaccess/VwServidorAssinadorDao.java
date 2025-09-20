package br.gov.stf.estf.expedicao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.VwServidorAssinador;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface VwServidorAssinadorDao {

	/**
	 * Lista todos os usuários do setor informado.
	 * 
	 * @return
	 * @throws DaoException
	 */
	List<VwServidorAssinador> listar() throws DaoException;
}
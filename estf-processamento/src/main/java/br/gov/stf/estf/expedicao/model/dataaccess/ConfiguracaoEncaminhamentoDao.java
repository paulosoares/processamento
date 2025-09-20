package br.gov.stf.estf.expedicao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.ConfiguracaoEncaminhamento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

public interface ConfiguracaoEncaminhamentoDao {

	/**
	 * Retorna todos os registros cadastrados.
	 * 
	 * @param codigo
	 * @return
	 * @throws ServiceException
	 */
	List<ConfiguracaoEncaminhamento> listar() throws DaoException;

	/**
	 * Busca o andamento com o código informado.
	 * 
	 * @param codigo
	 * @return
	 * @throws DaoException
	 */
	ConfiguracaoEncaminhamento buscar(Long codigo) throws DaoException;
}
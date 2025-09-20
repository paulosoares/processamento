package br.gov.stf.estf.expedicao.model.service;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.ConfiguracaoEncaminhamento;
import br.gov.stf.framework.model.service.ServiceException;

public interface ConfiguracaoEncaminhamentoService {

	/**
	 * Retorna todos os registros cadastrados.
	 * 
	 * @param codigo
	 * @return
	 * @throws ServiceException
	 */
	List<ConfiguracaoEncaminhamento> listar() throws ServiceException;

	/**
	 * Busca o andamento com o código informado.
	 * 
	 * @param codigo
	 * @return
	 * @throws ServiceException
	 */
	ConfiguracaoEncaminhamento buscar(Long codigo) throws ServiceException;
}
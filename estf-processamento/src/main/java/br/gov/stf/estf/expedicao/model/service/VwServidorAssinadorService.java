package br.gov.stf.estf.expedicao.model.service;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.VwServidorAssinador;
import br.gov.stf.framework.model.service.ServiceException;

public interface VwServidorAssinadorService {

	/**
	 * Lista todos os usuários.
	 * 
	 * @return
	 * @throws ServiceException
	 */
	List<VwServidorAssinador> listar() throws ServiceException;
}
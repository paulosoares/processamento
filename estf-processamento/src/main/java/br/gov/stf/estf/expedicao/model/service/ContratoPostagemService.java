package br.gov.stf.estf.expedicao.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.expedicao.entidade.ContratoPostagem;
import br.gov.stf.estf.expedicao.model.dataaccess.ContratoPostagemDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ContratoPostagemService extends GenericService<ContratoPostagem, Long, ContratoPostagemDao> {

	/**
	 * Retorna o contrato de postagem vigente.
	 *
	 * @return
	 * @throws ServiceException
	 */
	ContratoPostagem buscarVigente() throws ServiceException;

	/**
	 * Retorna o contrato de postagem vigente na data informada.
	 * 
	 * @param data
	 * @return
	 * @throws ServiceException
	 */
	ContratoPostagem buscarVigente(Date data) throws ServiceException;

	/**
	 * Lista todos os contratos que já foram encerrados, ou seja, que possuirem uma
	 * data de fim de vigência.
	 * 
	 * @return
	 * @throws ServiceException
	 */
	List<ContratoPostagem> listarContratosEncerrados() throws ServiceException;

	/**
	 * Lista todos os contratos cadastraos.
	 * 
	 * @return
	 * @throws ServiceException
	 */
	List<ContratoPostagem> listar() throws ServiceException;
}
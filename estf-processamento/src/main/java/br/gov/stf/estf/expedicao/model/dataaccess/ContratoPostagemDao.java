package br.gov.stf.estf.expedicao.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.expedicao.entidade.ContratoPostagem;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ContratoPostagemDao extends GenericDao<ContratoPostagem, Long> {

	/**
	 * Retorna o contrato de postagem vigente.
	 *
	 * @return
	 * @throws DaoException
	 */
	List<ContratoPostagem> listarContratosSemDataVigencia() throws DaoException;

	/**
	 * Lista todos os contratos que já foram encerrados, ou seja, que possuirem uma
	 * data de fim de vigência.
	 * 
	 * @return
	 * @throws DaoException
	 */
	List<ContratoPostagem> listarContratosEncerrados() throws DaoException;

	/**
	 * Lista todos os contratos cadastrados.
	 * 
	 * @return
	 * @throws DaoException
	 */
	List<ContratoPostagem> listar() throws DaoException;

	/**
	 * Lista todos os contratos cadastrados iniciados após a data informada.
	 * 
	 * @return
	 * @throws DaoException
	 */
	List<ContratoPostagem> listarIniciadosApos(Date data) throws DaoException;

	/**
	 * Retorna o contrato vigente na data informada, ou seja, o contrato onde a data
	 * informada esteja entre a data de início e a data de fim do contrato (ou a data
	 * fim seja nula).
	 * 
	 * @param data
	 * @return
	 * @throws DaoException
	 */
	ContratoPostagem buscarContratoVigenteEm(Date data) throws DaoException;
}
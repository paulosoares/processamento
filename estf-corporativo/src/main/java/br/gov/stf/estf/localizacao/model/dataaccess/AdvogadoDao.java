package br.gov.stf.estf.localizacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Advogado;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AdvogadoDao extends GenericDao<Advogado, Long> {
	public Advogado recuperar (Parte parte) throws DaoException;
	
	/**
	 * Faz o relacionamento entre duas partes já armazenadas no banco de dados: Uma parte representada e uma parte que
	 * será futuramente o advogado representante quando o protocolo for autuado.
	 * 
	 * @param advogadoParte
	 * @throws DaoException
	 */
	public void incluirAdvogado(Advogado advogado) throws DaoException;


	/**
	 * Retorna uma lista de advogados de acordo com as propriedades inicializadas no advogado informado. Utiliza a
	 * funcionalidade do query by example do hibernate.
	 * 
	 * @param advogado
	 * @return
	 * @throws DaoException
	 */
	public List<Advogado> pesquisarAdvogado(Advogado advogado) throws DaoException;


	/**
	 * Método que faz uma chamada à função CORP.FNC_FORMATA_OAB com a String informada.
	 * 
	 * @param numeroOab
	 * @return
	 * @throws DaoException
	 */
	public String validarNumeroOab(String numeroOab) throws DaoException;

	public List<Advogado> recuperarAdvogadoPorIdOuDescricao(String id) throws DaoException;

	public List<Advogado> recuperarAdvogadoPorId(Long id) throws DaoException;
	
	public List<Advogado> recuperarAdvogadoPorDescricao(String id) throws DaoException;
}

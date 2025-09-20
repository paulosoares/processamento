package br.gov.stf.estf.localizacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Advogado;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.localizacao.model.dataaccess.AdvogadoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AdvogadoService extends GenericService<Advogado, Long, AdvogadoDao> {
	public Advogado recuperar (Parte parte) throws ServiceException;
	
	/**
	 * @param advogado
	 * @throws ServiceException
	 */
	public void incluirAdvogado(Advogado advogado) throws ServiceException;


	/**
	 * Retorna uma lista de advogados de acordo com as propriedades inicializadas no advogado informado. Utiliza a
	 * funcionalidade do query by example do hibernate. Caso não seja encontrado nenhuma ocorrência, retorna uma lista
	 * vazia.
	 * 
	 * @param advogado
	 * @return
	 * @throws DaoException
	 */
	public List<Advogado> pesquisarAdvogado(Advogado oab) throws ServiceException;


	/**
	 * Função que valida se o número de OAB informado segue o formato padronizado: uma string de 8 caracteres, onde os
	 * dois primeiros são a sigla da unidade da federação (ex. DF, SP, etc). Do terceiro ao oitavo caracteres, devem ser
	 * informados apenas caracteres numéricos. Espaços em branco são ignorados. Ao se informar números de OAB que não
	 * atendem a especificação, é gerada uma {@code ServiceException}.
	 * 
	 * @param numeroOab
	 * @return O número de OAB formatado em caixa alta, sem espaços em branco e com o formato explicitado.
	 * @throws ServiceException
	 */
	public String validarNumeroOab(String numeroOab) throws ServiceException;
	
	public List<Advogado> recuperarAdvogadoPorIdOuDescricao(String id) throws ServiceException;
	
	public List<Advogado> recuperarAdvogadoPorId(Long id) throws ServiceException;
	
	public List<Advogado> recuperarAdvogadoPorDescricao(String id) throws ServiceException;
}

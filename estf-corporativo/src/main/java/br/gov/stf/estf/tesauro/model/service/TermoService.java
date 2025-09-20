/**
 * 
 */
package br.gov.stf.estf.tesauro.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.tesauro.Termo;
import br.gov.stf.estf.tesauro.model.dataaccess.TermoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 19.07.2012
 */
public interface TermoService extends GenericService<Termo, Long, TermoDao> {

	List<Termo> pesquisarPorDescricao(String suggestion) throws ServiceException;

	List<Termo> pesquisarPorDescricao(String suggestion, boolean termoExato) throws ServiceException;
	
	List<Termo> pesquisarPorDescricaoExata(String suggestion, boolean termoExato) throws ServiceException;
}

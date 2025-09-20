package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.PeticaoInicial;
import br.gov.stf.estf.entidade.processostf.PeticaoInicial.PeticaoInicialId;
import br.gov.stf.estf.processostf.model.dataaccess.PeticaoInicialDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;


/**
 * @author Bruno da Silva Abreu
 * @since 03/06/2008
 */
public interface PeticaoInicialService extends GenericService<PeticaoInicial, Long, PeticaoInicialDao> {
	/**
	 * @param peticaoInicial
	 * @throws ServiceException
	 */
	public void alterarPeticaoInicial(PeticaoInicial peticaoInicial) throws ServiceException;


	/**
	 * Retorna uma lista de petições iniciais de acordo com as propriedades inicializadas na petição informada. Utiliza
	 * a funcionalidade do query by example do hibernate. Caso não seja encontrado nenhuma ocorrência, retorna uma lista
	 * vazia.
	 * 
	 * @param peticaoInicial
	 * @return
	 * @throws DaoException
	 */
	public List<PeticaoInicial> pesquisarPeticaoInicial(PeticaoInicial peticaoInicial) throws ServiceException;

	public PeticaoInicial recuperarPeticaoInicial(PeticaoInicialId peticaoInicialId) throws ServiceException;
}

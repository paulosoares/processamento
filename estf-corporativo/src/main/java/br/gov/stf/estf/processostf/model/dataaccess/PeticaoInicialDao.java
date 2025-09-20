package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.PeticaoInicial;
import br.gov.stf.estf.entidade.processostf.PeticaoInicial.PeticaoInicialId;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Bruno da Silva Abreu
 * @since 03/06/2008
 *
 */
public interface PeticaoInicialDao extends GenericDao<PeticaoInicial, Long> {
	/**
	 * @param peticaoInicial
	 * @throws DaoException
	 */
	public void alterarPeticaoInicial(PeticaoInicial peticaoInicial) throws DaoException;


	/**
	 * Retorna uma lista de petições de acordo com as propriedades inicializadas na petição informada. Utiliza a
	 * funcionalidade do query by example do hibernate.
	 * 
	 * @param peticaoInicial
	 * @return
	 * @throws DaoException
	 */
	public List<PeticaoInicial> pesquisarPeticaoInicial(PeticaoInicial peticaoInicial) throws DaoException;
	
	public PeticaoInicial recuperarPeticaoInicial(PeticaoInicialId peticaoInicialId) throws DaoException;
}

/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import br.gov.stf.estf.entidade.jurisprudencia.BaseJurisprudenciaRepercussaoGeral;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author henrique.bona
 *
 */
public interface BaseJurisprudenciaRepercussaoGeralDao extends GenericDao<BaseJurisprudenciaRepercussaoGeral, Long> {
	
	/**
	 * Retorna a Repercussão Geral com base no ObjetoIncidente
	 * 
	 * @param oi
	 * @return
	 * @throws DaoException 
	 */
	BaseJurisprudenciaRepercussaoGeral recuperarPorObjetoIncidente(ObjetoIncidente<?> oi) throws DaoException;
	
	
	
}

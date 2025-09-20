/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import br.gov.stf.estf.entidade.jurisprudencia.QuestaoOrdem;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 20.08.2012
 */
public interface QuestaoOrdemDao extends GenericDao<QuestaoOrdem, Long> {

	QuestaoOrdem recuperarPorObjetoIncidente(ObjetoIncidente<?> oi) throws DaoException;

	
}

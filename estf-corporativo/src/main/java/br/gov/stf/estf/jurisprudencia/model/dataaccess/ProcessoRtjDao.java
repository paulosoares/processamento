/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.ProcessoRtj;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Hertony.Morais
 * @since 02.05.2013
 */
public interface ProcessoRtjDao extends GenericDao<ProcessoRtj, Long> {
	List<ProcessoRtj> pesquisarRtjPorObjetoIncidente(ObjetoIncidente<ObjetoIncidente<?>> objetoIncidente) throws DaoException;
}

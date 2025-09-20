/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.ProcessoRtj;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.ProcessoRtjDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Hertony.Morais
 * @since 30.04.2013
 */
public interface ProcessoRtjService extends GenericService<ProcessoRtj, Long, ProcessoRtjDao> {
	
	public List<ProcessoRtj> pesquisarRtjPorObjetoIncidente(ObjetoIncidente<ObjetoIncidente<?>> objetoIncidente) throws ServiceException;
	
}

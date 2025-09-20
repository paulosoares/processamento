/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.ProcessoRtj;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.ProcessoRtjDao;
import br.gov.stf.estf.jurisprudencia.model.service.ProcessoRtjService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Hertony.Morais
 * @since 30.04.2013
 */
@Service("processoRtjService")
public class ProcessoRtjServiceImpl extends GenericServiceImpl<ProcessoRtj, Long, ProcessoRtjDao> implements ProcessoRtjService {

	protected ProcessoRtjServiceImpl(ProcessoRtjDao dao) {
		super(dao);
	}
	
	@Override
	public List<ProcessoRtj> pesquisarRtjPorObjetoIncidente(
			ObjetoIncidente<ObjetoIncidente<?>> objetoIncidente) throws ServiceException{
		try {
			return dao.pesquisarRtjPorObjetoIncidente(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

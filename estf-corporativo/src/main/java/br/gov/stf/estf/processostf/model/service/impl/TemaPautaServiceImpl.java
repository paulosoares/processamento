/**
 * 
 */
package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.PautaPlenario;
import br.gov.stf.estf.entidade.processostf.TemaPauta;
import br.gov.stf.estf.processostf.model.dataaccess.TemaPautaDao;
import br.gov.stf.estf.processostf.model.service.TemaPautaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 08.08.2011
 */
@Service("temaPautaService")
public class TemaPautaServiceImpl extends GenericServiceImpl<TemaPauta, Long, TemaPautaDao>
		implements TemaPautaService {

	protected TemaPautaServiceImpl(TemaPautaDao dao) {
		super(dao);
	}

	/* (non-Javadoc)
	 * @see br.gov.stf.estf.processostf.model.service.TemaPautaService#pesquisarTemaPauta(br.gov.stf.estf.entidade.processostf.PautaPlenario)
	 */
	@Override
	public List<TemaPauta> pesquisarTemaPauta(PautaPlenario pautaPlenario)
			throws ServiceException {
		try {
			return dao.pesquisarTemaPauta(pautaPlenario);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

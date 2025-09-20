/**
 * 
 */
package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.SubtemaPauta;
import br.gov.stf.estf.entidade.processostf.TemaPauta;
import br.gov.stf.estf.processostf.model.dataaccess.SubtemaPautaDao;
import br.gov.stf.estf.processostf.model.service.SubtemaPautaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 08.08.2011
 */
@Service("subtemaPautaService")
public class SubtemaPautaServiceImpl extends
		GenericServiceImpl<SubtemaPauta, Long, SubtemaPautaDao> implements SubtemaPautaService {

	protected SubtemaPautaServiceImpl(SubtemaPautaDao dao) {
		super(dao);
	}

	/* (non-Javadoc)
	 * @see br.gov.stf.estf.processostf.model.service.SubtemaPautaService#pesquisarSubtemaPauta(br.gov.stf.estf.entidade.processostf.TemaPauta)
	 */
	@Override
	public List<SubtemaPauta> pesquisar(TemaPauta temaPauta)
			throws ServiceException {
		try {
			return dao.pesquisar(temaPauta);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

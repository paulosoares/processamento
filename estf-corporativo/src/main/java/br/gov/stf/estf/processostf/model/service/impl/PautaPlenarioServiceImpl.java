/**
 * 
 */
package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.PautaPlenario;
import br.gov.stf.estf.processostf.model.dataaccess.PautaPlenarioDao;
import br.gov.stf.estf.processostf.model.service.PautaPlenarioService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 *
 */
@Service("pautaPlenarioService")
public class PautaPlenarioServiceImpl extends
		GenericServiceImpl<PautaPlenario, Long, PautaPlenarioDao> implements PautaPlenarioService {

	protected PautaPlenarioServiceImpl(PautaPlenarioDao dao) {
		super(dao);
	}

	/* (non-Javadoc)
	 * @see br.gov.stf.estf.processostf.model.service.PautaPlenarioService#pesquisarTodos()
	 */
	@Override
	public List<PautaPlenario> recuperarTodos() throws ServiceException {
		try {
			return dao.recuperarTodos();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

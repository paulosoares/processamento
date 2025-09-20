/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.BaseJurisprudenciaRepercussaoGeral;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.BaseJurisprudenciaRepercussaoGeralDao;
import br.gov.stf.estf.jurisprudencia.model.service.BaseJurisprudenciaRepercussaoGeralService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author henrique.bona
 *
 */
@Service("baseJurisprudenciaRepercussaoGeralService")
public class BaseJurisprudenciaRepercussaoGeralServiceImpl extends
GenericServiceImpl<BaseJurisprudenciaRepercussaoGeral, Long, BaseJurisprudenciaRepercussaoGeralDao> implements
BaseJurisprudenciaRepercussaoGeralService {

	/**
	 * Construtor padrão
	 * @param dao
	 */
	protected BaseJurisprudenciaRepercussaoGeralServiceImpl(
			BaseJurisprudenciaRepercussaoGeralDao dao) {
		super(dao);
	}

	@Override
	public BaseJurisprudenciaRepercussaoGeral recuperarPorObjetoIncidente(
			ObjetoIncidente<?> oi) throws ServiceException {

		try {
			return this.dao.recuperarPorObjetoIncidente(oi);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("Não foi possível recuperar a Repercussão Geral com base no Objeto Incidente Informado.", e);
		}
	}

}

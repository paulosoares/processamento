/**
 * 
 */
package br.gov.stf.estf.tesauro.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.tesauro.Termo;
import br.gov.stf.estf.tesauro.model.dataaccess.TermoDao;
import br.gov.stf.estf.tesauro.model.service.TermoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 19.07.2012
 */
@Service("termoService")
public class TermoServiceImpl extends GenericServiceImpl<Termo, Long, TermoDao> implements TermoService {

	protected TermoServiceImpl(TermoDao dao) {
		super(dao);
	}
	
	@Override
	public List<Termo> pesquisarPorDescricao(String suggestion) throws ServiceException {
		return pesquisarPorDescricao(suggestion, false);
	}

	@Override
	public List<Termo> pesquisarPorDescricao(String suggestion, boolean termoExato) throws ServiceException {
		try {
			return dao.pesquisarPorDescricao(suggestion, termoExato);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Termo> pesquisarPorDescricaoExata(String suggestion, boolean termoExato) throws ServiceException {
		try {
			return dao.pesquisarPorDescricaoExata(suggestion, termoExato);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.TipoIndexacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.TipoIndexacaoDao;
import br.gov.stf.estf.jurisprudencia.model.service.TipoIndexacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
@Service("tipoIndexacaoService")
public class TipoIndexacaoServiceImpl extends GenericServiceImpl<TipoIndexacao, Long, TipoIndexacaoDao> implements TipoIndexacaoService {

	protected TipoIndexacaoServiceImpl(TipoIndexacaoDao dao) {
		super(dao);
	}

	@Override
	public TipoIndexacao recuperarPorSigla(String sigla) throws ServiceException {
		try {
			return dao.recuperarPorSigla(sigla);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}

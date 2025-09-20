package br.gov.stf.estf.julgamento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.TipoVoto;
import br.gov.stf.estf.julgamento.model.dataaccess.TipoVotoDao;
import br.gov.stf.estf.julgamento.model.service.TipoVotoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoVotoService")
public class TipoVotoServiceImpl extends GenericServiceImpl<TipoVoto, String, TipoVotoDao> implements TipoVotoService {

	protected TipoVotoServiceImpl(TipoVotoDao dao) {
		super(dao);
	}

	@Override
	public TipoVoto recuperarTipoVoto(String descricao) throws ServiceException {
		try {
			return dao.recuperarTipoVoto(descricao);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}

package br.gov.stf.estf.jurisdicionado.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisdicionado.TipoIdentificacao;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.TipoIdentificacaoDao;
import br.gov.stf.estf.jurisdicionado.model.service.TipoIdentificacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoIdentificacaoService")
public class TipoIdentificacaoServiceImpl extends GenericServiceImpl<TipoIdentificacao, Long, TipoIdentificacaoDao> 
	implements TipoIdentificacaoService {
	
	public TipoIdentificacaoServiceImpl(TipoIdentificacaoDao dao) {
		super(dao);
	}

	@Override
	public TipoIdentificacao pesquisaPelaSigla(String sigla) throws ServiceException {
		try {
			return dao.pesquisaPelaSigla(sigla);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	

}

package br.gov.stf.estf.documento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.ConfiguracaoTextoSetorDao;
import br.gov.stf.estf.documento.model.service.ConfiguracaoTextoSetorService;
import br.gov.stf.estf.entidade.documento.ConfiguracaoTextoSetor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("configuracaoTextoSetorService")
public class ConfiguracaoTextoSetorServiceImpl extends GenericServiceImpl<ConfiguracaoTextoSetor, Long, ConfiguracaoTextoSetorDao> implements ConfiguracaoTextoSetorService{

	protected ConfiguracaoTextoSetorServiceImpl(ConfiguracaoTextoSetorDao dao) {
		super(dao);
	}

	public ConfiguracaoTextoSetor recuperar(Long codigoSetor)
			throws ServiceException {
		ConfiguracaoTextoSetor cts = null;
		try {
			cts = dao.recuperar(codigoSetor);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return cts;
	}

}

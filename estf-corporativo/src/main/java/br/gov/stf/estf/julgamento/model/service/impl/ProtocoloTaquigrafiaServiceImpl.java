package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.processostf.ProtocoloTaquigrafia;
import br.gov.stf.estf.julgamento.model.dataaccess.ProtocoloTaquigrafiaDao;
import br.gov.stf.estf.julgamento.model.service.ProtocoloTaquigrafiaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Almir.Oliveira
 * @since 14.12.2011 */
@Service("protocoloTaquigrafiaService")
public class ProtocoloTaquigrafiaServiceImpl extends GenericServiceImpl<ProtocoloTaquigrafia, Long, ProtocoloTaquigrafiaDao> implements ProtocoloTaquigrafiaService {

	protected ProtocoloTaquigrafiaServiceImpl(ProtocoloTaquigrafiaDao dao) {
		super(dao);
	}

	@Override
	public List<ProtocoloTaquigrafia> pesquisar(ListaJulgamento listaJulgamento) throws ServiceException {
		try {
			return dao.pesquisar( listaJulgamento );
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}

	
}

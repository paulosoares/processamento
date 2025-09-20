package br.gov.stf.estf.documento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.ArquivoEletronicoDao;
import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ArquivoEletronicoView;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("arquivoEletronicoService")
public class ArquivoEletronicoServiceImpl extends GenericServiceImpl<ArquivoEletronico, Long, ArquivoEletronicoDao> implements
		ArquivoEletronicoService {
	public ArquivoEletronicoServiceImpl(ArquivoEletronicoDao dao) {
		super(dao);
	}

	public ArquivoEletronico recuperarArquivoEletronico(Long id) throws ServiceException {
		ArquivoEletronico result = null;

		try {
			result = dao.recuperarArquivoEletronico(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return result;
	}

	public void desbloquearArquivoEletronico(Long id) throws ServiceException {
		try {
			dao.desbloquearArquivoEletronico(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public void desbloquearArquivoEletronicoAdmin(Long id) throws ServiceException {
		try {
			dao.desbloquearArquivoEletronicoAdmin(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public ArquivoEletronico recuperarBloquearArquivoEletronico(Long id) throws ServiceException {
		try {
			return dao.recuperarBloquearArquivoEletronico(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	public ArquivoEletronicoView recuperarArquivoEletronicoViewPeloId(Long id) throws ServiceException {
		try {
			return dao.recuperarArquivoEletronicoViewPeloId(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
}

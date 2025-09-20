package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.TipoComunicacaoESTFDao;
import br.gov.stf.estf.documento.model.service.TipoComunicacaoESTFService;
import br.gov.stf.estf.entidade.documento.TipoComunicacaoESTF;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoComunicacaoServiceESTF")
public class TipoComunicacaoESTFServiceImpl
		extends
		GenericServiceImpl<TipoComunicacaoESTF, Integer, TipoComunicacaoESTFDao>
		implements TipoComunicacaoESTFService {

	@Autowired
	public TipoComunicacaoESTFServiceImpl(TipoComunicacaoESTFDao dao) {
		super(dao);
	}

	@Override
	public List<TipoComunicacaoESTF> pesquisarTodos() throws ServiceException {
		try {
			return dao.pesquisarTodos();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<TipoComunicacaoESTF> pesquisarPorTipo(Integer[] tipos)
			throws ServiceException {
		try {
			return dao.pesquisarPorTipo(tipos);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}

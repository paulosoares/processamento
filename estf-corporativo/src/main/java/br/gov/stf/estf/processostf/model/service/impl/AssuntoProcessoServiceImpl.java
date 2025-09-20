package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.entidade.processostf.AssuntoProcesso;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.dataaccess.AssuntoProcessoDao;
import br.gov.stf.estf.processostf.model.service.AssuntoProcessoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("assuntoProcessoService")
public class AssuntoProcessoServiceImpl extends
		GenericServiceImpl<AssuntoProcesso, Long, AssuntoProcessoDao> implements
		AssuntoProcessoService {
	public AssuntoProcessoServiceImpl(AssuntoProcessoDao dao) {
		super(dao);
	}

	public List<AssuntoProcesso> pesquisar(String siglaClasseProcessual,
			Long numeroProcesso) throws ServiceException {

		List<AssuntoProcesso> resp = null;

		try {
			resp = dao.pesquisar(siglaClasseProcessual, numeroProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return resp;
	}

	public void persistirAssuntoProcesso(AssuntoProcesso assuntoProcesso)
			throws ServiceException {

		try {
			dao.persistirAssuntoProcesso(assuntoProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Assunto recuperarAssuntoProcesso(Processo processo)
			throws ServiceException {
		Assunto assunto = null;
		try {
			assunto = dao.recuperarAssuntoPrincipal(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return assunto;
	}

	public List<Assunto> recuperarListaAssuntosDoProcesso(Processo processo)
			throws ServiceException {
		List<Assunto> listaAssunto = null;
		try {
			listaAssunto = dao.recuperarListaAssuntosDoProcesso(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return listaAssunto;
	}
}

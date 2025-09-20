package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.processostf.model.dataaccess.OrigemAndamentoDecisaoDao;
import br.gov.stf.estf.processostf.model.service.OrigemAndamentoDecisaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("origemAndamentoDecisaoService")
public class OrigemAndamentoDecisaoServiceImpl extends GenericServiceImpl<OrigemAndamentoDecisao, Long, OrigemAndamentoDecisaoDao> 
	implements OrigemAndamentoDecisaoService {
    public OrigemAndamentoDecisaoServiceImpl(OrigemAndamentoDecisaoDao dao) { super(dao); }

	@Override
	public OrigemAndamentoDecisao pesquisarOrigemDecisao(Setor setor) throws ServiceException {

		try {
			return dao.pesquisarOrigemDecisao(setor);
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<OrigemAndamentoDecisao> pesquisarOrigensComMinistroAtivo() throws ServiceException {

		try {
			return dao.pesquisarOrigensComMinistroAtivo();
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<OrigemAndamentoDecisao> pesquisarOrigensDecisao(List<Long> idsOrigem) throws ServiceException {
	
		try {
			return dao.pesquisarOrigensDecisao(idsOrigem);
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}
}

package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoPeticao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoPeticaoDao;
import br.gov.stf.estf.processostf.model.service.AndamentoPeticaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("andamentoPeticaoService")
public class AndamentoPeticaoServiceImpl extends GenericServiceImpl<AndamentoPeticao, Long, AndamentoPeticaoDao> implements AndamentoPeticaoService {
	
	public AndamentoPeticaoServiceImpl(AndamentoPeticaoDao dao) {
		super(dao);
	}

	public Long recuperarUltimaSequencia(ObjetoIncidente processo) throws ServiceException {
		try {
			Long seq = dao.recuperarUltimaSequencia(processo);
			return (seq == null) ? 0L : seq;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public AndamentoPeticao pesquisar(Andamento andamento, Peticao peticao) throws ServiceException {
		try {
			return dao.pesquisar(andamento, peticao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}

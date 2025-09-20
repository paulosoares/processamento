package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoProcessoComunicacaoDao;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoComunicacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("andamentoProcessoComunicacaoService")
public class AndamentoProcessoComunicacaoServiceImpl extends
		GenericServiceImpl<AndamentoProcessoComunicacao, Long, AndamentoProcessoComunicacaoDao> implements
		AndamentoProcessoComunicacaoService {
	public AndamentoProcessoComunicacaoServiceImpl(AndamentoProcessoComunicacaoDao dao) {
		super(dao);
	}

	@Override 
	public AndamentoProcessoComunicacao recuperarPorAndamento(Long idAndamento) throws ServiceException{
		try {
			return this.dao.recuperarPorAndamento(idAndamento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	

}

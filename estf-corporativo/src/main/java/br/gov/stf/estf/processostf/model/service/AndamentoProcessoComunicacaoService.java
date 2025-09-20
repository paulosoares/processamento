package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoProcessoComunicacaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AndamentoProcessoComunicacaoService extends GenericService<AndamentoProcessoComunicacao, Long, AndamentoProcessoComunicacaoDao> {

	AndamentoProcessoComunicacao recuperarPorAndamento(Long idAndamento) throws ServiceException;
	
}

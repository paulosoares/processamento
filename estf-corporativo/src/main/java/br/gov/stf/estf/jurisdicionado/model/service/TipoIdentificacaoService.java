package br.gov.stf.estf.jurisdicionado.model.service;

import br.gov.stf.estf.entidade.jurisdicionado.TipoIdentificacao;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.TipoIdentificacaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;


public interface TipoIdentificacaoService extends GenericService<TipoIdentificacao, Long, TipoIdentificacaoDao> {
	
	TipoIdentificacao pesquisaPelaSigla(String sigla) throws ServiceException;

}

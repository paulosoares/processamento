package br.gov.stf.estf.configuracao.model.service;

import br.gov.stf.estf.configuracao.model.dataaccess.ConfiguracaoSistemaDao;
import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ConfiguracaoSistemaService extends GenericService<ConfiguracaoSistema, Long, ConfiguracaoSistemaDao> {

	public String recuperarValor(String chave) throws ServiceException;

	public ConfiguracaoSistema recuperarValor(String siglaSistema, String chave) throws ServiceException;

}

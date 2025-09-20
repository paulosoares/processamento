package br.gov.stf.estf.configuracao.model.dataaccess;

import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ConfiguracaoSistemaDao extends GenericDao<ConfiguracaoSistema, Long> {
	
	public String recuperarValor(String chave) throws DaoException;

	public ConfiguracaoSistema recuperarValor(String siglaSistema, String chave) throws DaoException;

}

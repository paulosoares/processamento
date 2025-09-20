package br.gov.stf.estf.localizacao.model.dataaccess;

import br.gov.stf.estf.entidade.localizacao.ConfiguracaoSetor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ConfiguracaoSetorDao extends GenericDao<ConfiguracaoSetor, Long> {
	
	public ConfiguracaoSetor pesquisarConfiguracaoSetor(Long codigoSetor, Long codigoTipoConfiguracaoSetor) throws DaoException;
	public ConfiguracaoSetor recuperarConfiguracaoSetor(Long codigoSetor, String siglaTipoConfiguracaoSetor) throws DaoException;

}

package br.gov.stf.estf.documento.model.dataaccess;

import br.gov.stf.estf.entidade.documento.ConfiguracaoTextoSetor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ConfiguracaoTextoSetorDao extends GenericDao<ConfiguracaoTextoSetor, Long> {
	public ConfiguracaoTextoSetor recuperar (Long codigoSetor) throws DaoException;
}

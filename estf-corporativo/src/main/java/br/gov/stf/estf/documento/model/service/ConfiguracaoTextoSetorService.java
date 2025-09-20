package br.gov.stf.estf.documento.model.service;

import br.gov.stf.estf.documento.model.dataaccess.ConfiguracaoTextoSetorDao;
import br.gov.stf.estf.entidade.documento.ConfiguracaoTextoSetor;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ConfiguracaoTextoSetorService extends GenericService<ConfiguracaoTextoSetor, Long, ConfiguracaoTextoSetorDao> {
	public ConfiguracaoTextoSetor recuperar (Long codigoSetor) throws ServiceException;
}

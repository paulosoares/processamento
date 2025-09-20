package br.gov.stf.estf.julgamento.model.service;

import br.gov.stf.estf.entidade.julgamento.TipoVoto;
import br.gov.stf.estf.julgamento.model.dataaccess.TipoVotoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoVotoService extends GenericService<TipoVoto, String, TipoVotoDao> {

	public TipoVoto recuperarTipoVoto(String descricao) throws ServiceException;

}

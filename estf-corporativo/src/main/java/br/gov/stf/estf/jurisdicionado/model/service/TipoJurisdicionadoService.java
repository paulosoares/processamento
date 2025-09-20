package br.gov.stf.estf.jurisdicionado.model.service;

import br.gov.stf.estf.entidade.jurisdicionado.TipoJurisdicionado;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.TipoJurisdicionadoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoJurisdicionadoService extends GenericService<TipoJurisdicionado, Long, TipoJurisdicionadoDao> {
	
	TipoJurisdicionado buscaTipoPelaSigla(String sigla) throws ServiceException;

}

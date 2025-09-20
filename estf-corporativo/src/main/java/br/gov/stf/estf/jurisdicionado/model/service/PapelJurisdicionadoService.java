package br.gov.stf.estf.jurisdicionado.model.service;

import br.gov.stf.estf.entidade.jurisdicionado.PapelJurisdicionado;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.PapelJurisdicionadoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PapelJurisdicionadoService extends GenericService<PapelJurisdicionado, Long, PapelJurisdicionadoDao> {
	
	public PapelJurisdicionado recuperarPorId(Long id) throws ServiceException;

}

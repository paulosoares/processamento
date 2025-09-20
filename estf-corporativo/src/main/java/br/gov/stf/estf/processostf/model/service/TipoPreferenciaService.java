package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.TipoPreferencia;
import br.gov.stf.estf.processostf.model.dataaccess.TipoPreferenciaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoPreferenciaService extends GenericService<TipoPreferencia, Long, TipoPreferenciaDao> {

	public TipoPreferencia recuperarTipoPreferencia(Long idTipoPreferencia) throws ServiceException;
	
	public List<TipoPreferencia> recuperarTipoPreferenciaODS() throws ServiceException;
}

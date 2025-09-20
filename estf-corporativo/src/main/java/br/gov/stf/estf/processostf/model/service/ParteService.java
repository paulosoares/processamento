package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.processostf.model.dataaccess.ParteDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ParteService extends GenericService<Parte, Long, ParteDao>{
	
	public List<Parte> pesquisarPartes(Long idObjetoIncidente) throws ServiceException;
	
	public List<Parte> pesquisarPartes(Long idObjetoIncidente, List<Long> codigosCategoria) throws ServiceException;
}

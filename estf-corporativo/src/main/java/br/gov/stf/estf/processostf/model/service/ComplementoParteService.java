package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ComplementoParte;
import br.gov.stf.estf.entidade.processostf.ComplementoParte.ComplementoParteId;
import br.gov.stf.estf.processostf.model.dataaccess.ComplementoParteDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ComplementoParteService extends GenericService<ComplementoParte, ComplementoParteId, ComplementoParteDao>{
	public List<ComplementoParte> pesquisar (Long codigoParte) throws ServiceException;
}

package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.VinculoProcessoTema;
import br.gov.stf.estf.julgamento.model.dataaccess.VinculoProcessoTemaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface VinculoProcessoTemaService extends GenericService<VinculoProcessoTema, Long, VinculoProcessoTemaDao> {

	public List<VinculoProcessoTema> pesquisarVinculoProcessoTema(Long idTema,
			Long idObjetoIncidentePrincipal, Long idTipoTema) throws ServiceException;

	public boolean existeRepresentativoControversiaProcesso(Long idObjetoIncidentePrincipal, Long idTipoTema) throws ServiceException;
}

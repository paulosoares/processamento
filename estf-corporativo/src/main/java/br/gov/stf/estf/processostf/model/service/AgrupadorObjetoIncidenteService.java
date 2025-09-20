package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.AgrupadorObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.AgrupadorObjetoIncidenteDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;

public interface AgrupadorObjetoIncidenteService extends GenericService<AgrupadorObjetoIncidente, Long, AgrupadorObjetoIncidenteDao> {

	List<AgrupadorObjetoIncidente> pesquisarAgrupadorObjetoIncidente(Agrupador categoria, ObjetoIncidente<?> objetoIncidente) throws DaoException;

}

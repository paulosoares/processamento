package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.AgrupadorObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AgrupadorObjetoIncidenteDao extends GenericDao<AgrupadorObjetoIncidente, Long> {

	List<AgrupadorObjetoIncidente> pesquisarAgrupadorObjetoIncidente(Agrupador categoria, ObjetoIncidente<?> objetoIncidente) throws DaoException;
}

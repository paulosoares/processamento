package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.AgrupadorObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.AgrupadorObjetoIncidenteDao;
import br.gov.stf.estf.processostf.model.service.AgrupadorObjetoIncidenteService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("agrupadorObjetoIncidenteService")
public class AgrupadorObjetoIncidenteServiceImpl extends GenericServiceImpl<AgrupadorObjetoIncidente, Long, AgrupadorObjetoIncidenteDao>
		implements AgrupadorObjetoIncidenteService {
	
	public AgrupadorObjetoIncidenteServiceImpl(AgrupadorObjetoIncidenteDao dao) {
		super(dao);
	}

	@Override
	public List<AgrupadorObjetoIncidente> pesquisarAgrupadorObjetoIncidente(Agrupador categoria, ObjetoIncidente<?> objetoIncidente) throws DaoException {
		return dao.pesquisarAgrupadorObjetoIncidente(categoria, objetoIncidente);
	}

}

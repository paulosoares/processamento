package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.processostf.SumulaIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface SumulaIncidenteDao extends GenericDao<SumulaIncidente, Long> {
	
	public SumulaIncidente pesquisaSumulaIncidente(Long idSumula, Long idProcessoPrecedente) throws DaoException;

}

package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.IncidentePreferencia;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface IncidentePreferenciaDao extends GenericDao<IncidentePreferencia, Long> {

	public abstract List<IncidentePreferencia> recuperarPreferenciasDoIncidente(Long seqObjetoIncidente) throws DaoException;

	public abstract void inserirPreferenciaConvertidoEletronico(IncidentePreferencia incidentePreferencia) throws DaoException;

	public abstract void removerPreferenciaConvertidoEletronico(IncidentePreferencia incidentePreferencia) throws DaoException;
	
}

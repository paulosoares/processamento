package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.TipoPreferencia;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoPreferenciaDao extends GenericDao<TipoPreferencia, Long> {

	public TipoPreferencia recuperarTipoPreferencia(Long idTipoPreferencia) throws DaoException;
	
	public List<TipoPreferencia> recuperarTipoPreferenciaODS() throws DaoException; 
}

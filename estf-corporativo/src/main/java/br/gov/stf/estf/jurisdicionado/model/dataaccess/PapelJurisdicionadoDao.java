package br.gov.stf.estf.jurisdicionado.model.dataaccess;

import br.gov.stf.estf.entidade.jurisdicionado.PapelJurisdicionado;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PapelJurisdicionadoDao extends GenericDao<PapelJurisdicionado, Long> {

	public PapelJurisdicionado recuperarPorId(Long id) throws DaoException;

}

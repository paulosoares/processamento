package br.gov.stf.estf.jurisdicionado.model.dataaccess;

import br.gov.stf.estf.entidade.jurisdicionado.TipoJurisdicionado;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoJurisdicionadoDao extends GenericDao<TipoJurisdicionado, Long> {
	
	TipoJurisdicionado buscaTipoPelaSigla(String sigla) throws DaoException;

}

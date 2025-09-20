package br.gov.stf.estf.julgamento.model.dataaccess;

import br.gov.stf.estf.entidade.julgamento.TipoVoto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoVotoDao extends GenericDao<TipoVoto, String> {

	public TipoVoto recuperarTipoVoto(String descricao) throws DaoException;

}

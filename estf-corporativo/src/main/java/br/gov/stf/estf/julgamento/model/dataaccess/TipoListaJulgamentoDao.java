package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.TipoListaJulgamento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoListaJulgamentoDao extends GenericDao<TipoListaJulgamento, Long>{

	List<TipoListaJulgamento> recuperarTipoListaJulgamentoAtivas() throws DaoException;

}

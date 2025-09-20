package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.HistoricoProcessoOrigem;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface HistoricoProcessoOrigemDao extends GenericDao<HistoricoProcessoOrigem, Long> {

public List<HistoricoProcessoOrigem> recuperarPorObjetoIncidente (Long objetoIncidente) throws DaoException;

public List<HistoricoProcessoOrigem> recuperarTodosPorObjetoIncidente(Long objetoIncidente) throws DaoException;

public HistoricoProcessoOrigem recuperarOrigemInicialSTJ(Long idObjetoIncidente) throws DaoException;

}

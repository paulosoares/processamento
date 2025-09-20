package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.VinculoProcessoTema;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface VinculoProcessoTemaDao extends GenericDao<VinculoProcessoTema, Long> {

	public List<VinculoProcessoTema> pesquisarVinculoProcessoTema(Long idTema, Long idObjetoIncidentePrincipal, Long idTipoTema) throws DaoException;

}

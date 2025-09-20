package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.processostf.ControlarDeslocaIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ControlarDeslocaIncidenteDao extends GenericDao<ControlarDeslocaIncidente, Long> {
	// grava um objeto incidente na tabela temporária que servirá como insumo para a pkg_desloca_incidente realizar o deslocamento.
	public void insereObjetoIncidente(ControlarDeslocaIncidente controlarDeslocaIncidente) throws DaoException;
}

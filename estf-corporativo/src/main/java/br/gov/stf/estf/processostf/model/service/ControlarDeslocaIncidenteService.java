package br.gov.stf.estf.processostf.model.service;

import java.sql.SQLException;

import br.gov.stf.estf.entidade.processostf.ControlarDeslocaIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.ControlarDeslocaIncidenteDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ControlarDeslocaIncidenteService extends GenericService<ControlarDeslocaIncidente, Long, ControlarDeslocaIncidenteDao> {

	// grava um objeto incidente na tabela temporária que servirá como insumo para a pkg_desloca_incidente realizar o deslocamento.
	public void insereObjetoIncidente(ControlarDeslocaIncidente controlarDeslocaIncidente) throws ServiceException, SQLException;
}

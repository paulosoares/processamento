package br.gov.stf.estf.log.model.dataaccess;

import java.util.Date;

import br.gov.stf.estf.entidade.log.LogControleProcess;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface LogControleProcessDao extends	GenericDao<LogControleProcess, Long> {
	
	public Date findMaxDataByUsuarioExterno(Long seqUsuarioExterno) throws DaoException;

}
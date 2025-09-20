package br.gov.stf.estf.log.model.service;

import java.util.Date;

import br.gov.stf.estf.entidade.log.LogControleProcess;
import br.gov.stf.estf.log.model.dataaccess.LogControleProcessDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface LogControleProcessService extends GenericService<LogControleProcess, Long, LogControleProcessDao> {
	
	public Date findMaxDataByUsuarioExterno(Long seqUsuarioExterno) throws ServiceException;
	
}

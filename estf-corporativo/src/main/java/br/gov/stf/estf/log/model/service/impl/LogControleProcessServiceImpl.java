package br.gov.stf.estf.log.model.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.log.LogControleProcess;
import br.gov.stf.estf.log.model.dataaccess.LogControleProcessDao;
import br.gov.stf.estf.log.model.service.LogControleProcessService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;


@Service("logControleProcessService")
public class LogControleProcessServiceImpl extends GenericServiceImpl<LogControleProcess, Long, LogControleProcessDao> implements LogControleProcessService {

	public LogControleProcessServiceImpl(LogControleProcessDao dao) {
		super(dao);
	}

	@Override
	public Date findMaxDataByUsuarioExterno(Long seqUsuarioExterno) throws ServiceException{

		Date retorno = null;
		
		try {
			retorno = dao.findMaxDataByUsuarioExterno(seqUsuarioExterno);
		} catch(DaoException e) {
			throw new ServiceException(e);
		}     
		return retorno;
	}

}

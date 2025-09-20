package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.judiciario.NumeroProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.NumeroProcessoDao;
import br.gov.stf.estf.processostf.model.service.NumeroProcessoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("numeroProcessoService")
public class NumeroProcessoServiceImpl extends GenericServiceImpl<NumeroProcesso, Long, NumeroProcessoDao> implements NumeroProcessoService {

	protected NumeroProcessoServiceImpl(NumeroProcessoDao dao) {
		super(dao);
	}

	@Override
	public List<NumeroProcesso> getAll() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NumeroProcesso getNumeroProcesso() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}

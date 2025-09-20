package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.judiciario.NumeroProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.NumeroProcessoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface NumeroProcessoService extends GenericService<NumeroProcesso, Long, NumeroProcessoDao> {

	public List<NumeroProcesso> getAll() throws ServiceException;
	public NumeroProcesso getNumeroProcesso() throws ServiceException;
}

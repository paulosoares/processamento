package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.PrevisaoImpedimentoMinistro;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.PrevisaoImpedimentoMinistroDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PrevisaoImpedimentoMinistroService extends GenericService<PrevisaoImpedimentoMinistro, Long, PrevisaoImpedimentoMinistroDao> {
	
	public List<PrevisaoImpedimentoMinistro> recuperar(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
	public PrevisaoImpedimentoMinistro recuperar(Ministro ministro, ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
}

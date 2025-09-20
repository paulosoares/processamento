package br.gov.stf.estf.processostf.model.service;



import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.AcordaoAgendado;
import br.gov.stf.estf.entidade.processostf.AcordaoAgendado.AcordaoAgendadoId;
import br.gov.stf.estf.processostf.model.dataaccess.AcordaoAgendadoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AcordaoAgendadoService extends GenericService<AcordaoAgendado, AcordaoAgendadoId, AcordaoAgendadoDao> {
	public List<AcordaoAgendado> pesquisarSessaoEspecial (
			Boolean recuperarOcultos, String ...siglaClasseProcessual) 
	throws ServiceException;
	
	public List<AcordaoAgendado> pesquisarComposto (Date dataComposicaoDj) throws ServiceException;
}

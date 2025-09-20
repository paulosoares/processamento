package br.gov.stf.estf.processostf.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.AcordaoAgendado;
import br.gov.stf.estf.entidade.processostf.AcordaoAgendado.AcordaoAgendadoId;
import br.gov.stf.estf.processostf.model.dataaccess.AcordaoAgendadoDao;
import br.gov.stf.estf.processostf.model.service.AcordaoAgendadoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("acordaoAgendadoService")
public class AcordaoAgendadoServiceImpl extends GenericServiceImpl<AcordaoAgendado, AcordaoAgendadoId, AcordaoAgendadoDao> 
	implements AcordaoAgendadoService {
	
    public AcordaoAgendadoServiceImpl(AcordaoAgendadoDao dao) { super(dao); }

	public List<AcordaoAgendado> pesquisarSessaoEspecial(
			Boolean recuperarOcultos, String... siglaClasseProcessual) throws ServiceException {
		List<AcordaoAgendado> acordaos = null;
		try {
			acordaos = dao.pesquisarSessaoEspecial(recuperarOcultos, siglaClasseProcessual);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return acordaos;
	}

	public List<AcordaoAgendado> pesquisarComposto(Date dataComposicaoDj)
			throws ServiceException {
		List<AcordaoAgendado> acordaos = null;
		try {
			acordaos = dao.pesquisarComposto(dataComposicaoDj);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return acordaos;
	}

}

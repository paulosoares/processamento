package br.gov.stf.estf.intimacao.model.service.impl;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.intimacao.model.dataaccess.ProcessoLocalDao;
import br.gov.stf.estf.intimacao.model.service.ProcessoLocalService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Roberio.Fernandes
 */
@Service("processoLocalServiceIntimacao")
public class ProcessoLocalServiceImpl implements ProcessoLocalService {

    public static final long serialVersionUID = 1L;

    @Autowired
    private ProcessoLocalDao dao;

    @Override
    public Processo recuperarProcessoEletronico(String classe, Long numero, Long idSetor) throws ServiceException {
        try {
            return dao.recuperarProcessoEletronico(classe, numero, idSetor);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }
    
    @Override
	public List<ObjetoIncidente<?>> recuperarIncidentesDoProcessoEletronico(Long idObjetoIncidentePrincipal) throws ServiceException {
		try {
            return dao.recuperarIncidentesDoProcessoEletronico(idObjetoIncidentePrincipal);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
	}

	@Override
	public Processo recuperarProcessoEletronicoPorIdProcessoPrincipal(Long idObjetoIncidentePrincipal) throws ServiceException {
		try {
			return dao.recuperarProcessoEletronicoPorIdProcessoPrincipal(idObjetoIncidentePrincipal);
		 } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
	}
}

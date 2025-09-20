package br.gov.stf.estf.intimacao.model.service.impl;

import br.gov.stf.estf.intimacao.model.dataaccess.ParteLocalDao;
import br.gov.stf.estf.intimacao.model.service.ParteLocalService;
import br.gov.stf.estf.intimacao.visao.dto.ParteDTO;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Roberio.Fernandes
 */
@Service("parteLocalServiceIntimacao")
public class ParteLocalServiceImpl implements ParteLocalService {

    public static final long serialVersionUID = 1L;

    @Autowired
    private ParteLocalDao dao;

    @Override
    public List<ParteDTO> listarPartes(Boolean intimavel, Long objetoIncidente) throws ServiceException {
        try {
            return dao.listarPartes(intimavel, objetoIncidente);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

	@Override
	public List<ParteDTO> listarPartesIntimacaoEletronica() throws DaoException {		
		return dao.listarPartesIntimacaoEletronica();
	}
}

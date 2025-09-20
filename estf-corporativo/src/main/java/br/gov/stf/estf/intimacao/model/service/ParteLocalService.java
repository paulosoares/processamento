package br.gov.stf.estf.intimacao.model.service;

import br.gov.stf.estf.intimacao.visao.dto.ParteDTO;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

import java.util.List;

/**
 *
 * @author Roberio.Fernandes
 */
public interface ParteLocalService {

    List<ParteDTO> listarPartes(Boolean intimavel, Long objetoIncidente) throws ServiceException;
    
	List<ParteDTO> listarPartesIntimacaoEletronica() throws DaoException;

}
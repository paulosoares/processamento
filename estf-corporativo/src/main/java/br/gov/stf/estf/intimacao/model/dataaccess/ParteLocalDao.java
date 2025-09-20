package br.gov.stf.estf.intimacao.model.dataaccess;

import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.intimacao.visao.dto.ParteDTO;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import java.util.List;

/**
 *
 * @author Roberio.Fernandes
 */
public interface ParteLocalDao extends GenericDao<Parte, Long> {

    List<ParteDTO> listarPartes(Boolean intimavel, Long objetoIncidente) throws DaoException;

	List<ParteDTO> listarPartesIntimacaoEletronica() throws DaoException;
}
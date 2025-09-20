package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.VinculoProcessoTema;
import br.gov.stf.estf.julgamento.model.dataaccess.VinculoProcessoTemaDao;
import br.gov.stf.estf.julgamento.model.service.VinculoProcessoTemaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("vinculoProcessoTemaService")
public class VinculoProcessoTemaServiceImpl
		extends GenericServiceImpl<VinculoProcessoTema, Long, VinculoProcessoTemaDao>
		implements VinculoProcessoTemaService {

	public VinculoProcessoTemaServiceImpl(VinculoProcessoTemaDao dao) {
		super(dao);
	}

	public List<VinculoProcessoTema> pesquisarVinculoProcessoTema(Long idTema, Long idObjetoIncidentePrincipal, Long idTipoTema)
			throws ServiceException {

		try {
			return dao.pesquisarVinculoProcessoTema(idTema, idObjetoIncidentePrincipal, idTipoTema);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public boolean existeRepresentativoControversiaProcesso(Long idObjetoIncidentePrincipal, Long idTipoTema) throws ServiceException {
		boolean existeRC = false;

		try {
			if (dao.pesquisarVinculoProcessoTema(null, idObjetoIncidentePrincipal, idTipoTema).size() > 0)
				existeRC = true;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return existeRC;
	}

}
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.Auditoria;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.AuditoriaDao;
import br.gov.stf.estf.jurisprudencia.model.service.AuditoriaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Edvaldoo
 * @since 07.12.2020
 */
@Service("auditoriaService")
public class AuditoriaServiceImpl extends GenericServiceImpl<Auditoria, Long, AuditoriaDao>
		implements AuditoriaService {

	protected AuditoriaServiceImpl(AuditoriaDao dao) {
		super(dao);
	}

	@Override
	public List<Auditoria> pesquisarPelaReferenciaDoProcesso(Auditoria auditoria) throws ServiceException {
		try {
			return dao.pesquisarPelaReferenciaDoProcesso(auditoria);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
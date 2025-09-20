package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.estf.processostf.model.dataaccess.ProcedenciaDao;
import br.gov.stf.estf.processostf.model.service.ProcedenciaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("procedenciaService")
public class ProcedenciaServiceImpl extends GenericServiceImpl<Procedencia, Long, ProcedenciaDao> implements ProcedenciaService {
	public ProcedenciaServiceImpl(ProcedenciaDao dao) {
		super(dao);
	}

	public List pesquisarProcedencia() throws ServiceException {

		List<Procedencia> procedencia = null;

		try {
			procedencia = dao.pesquisarProcedencia();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return procedencia;
	}

	public String pesquisarProcedencia(Long objetoIncidenteId) throws ServiceException {
		String procedencia = "";
		try {
			procedencia = dao.pesquisarProcedencia(objetoIncidenteId);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return procedencia;
	}

	@Override
	public List<Procedencia> pesquisarProcedenciasAtivas() throws ServiceException {

		try {
			return dao.pesquisarProcedenciasAtivas();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Procedencia> pesquisarProcedenciasComOrigemAtiva(Orgao orgao) throws ServiceException {
		
		try {
			return dao.pesquisarProcedenciasComOrigemAtiva(orgao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Procedencia> pesquisarProcedenciasDescricaoAtivas(
			String descricao) throws ServiceException {
		try {
			return dao.pesquisarProcedenciasDescricaoAtivas(descricao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}

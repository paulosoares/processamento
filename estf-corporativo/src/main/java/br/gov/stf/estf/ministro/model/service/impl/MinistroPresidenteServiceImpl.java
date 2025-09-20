package br.gov.stf.estf.ministro.model.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.ministro.MinistroPresidente;
import br.gov.stf.estf.entidade.ministro.MinistroPresidente.MinistroPresidenteId;
import br.gov.stf.estf.entidade.ministro.TipoOcorrenciaMinistro;
import br.gov.stf.estf.ministro.model.dataaccess.MinistroPresidenteDao;
import br.gov.stf.estf.ministro.model.service.MinistroPresidenteService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("ministroPresidenteService")
public class MinistroPresidenteServiceImpl
		extends
		GenericServiceImpl<MinistroPresidente, MinistroPresidenteId, MinistroPresidenteDao>
		implements MinistroPresidenteService {
    public MinistroPresidenteServiceImpl(MinistroPresidenteDao dao) { super(dao); }

	private Map<TipoOcorrenciaMinistro, MinistroPresidente> mapaDeMinistrosPresidentes;

	public Map<TipoOcorrenciaMinistro, MinistroPresidente> getMapaDeMinistrosPresidentes() throws ServiceException {
		if (mapaDeMinistrosPresidentes == null) {
			mapaDeMinistrosPresidentes = recuperarMinistrosPresidentesAtuais();
		}
		return mapaDeMinistrosPresidentes;
	}


	public MinistroPresidente recuperarMinistroPresidenteAtual()
			throws ServiceException {
		MinistroPresidente ministro = null;

		try {
			ministro = dao.recuperarMinistroPresidenteAtual();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return ministro;
	}

	public Map<TipoOcorrenciaMinistro, MinistroPresidente> recuperarMinistrosPresidentesAtuais()
			throws ServiceException {
		try {
			List<MinistroPresidente> ministrosPresidentes = dao
					.recuperarMinistrosPresidentesAtuais();
			Map<TipoOcorrenciaMinistro, MinistroPresidente> mapaDeMinistros = new HashMap<TipoOcorrenciaMinistro, MinistroPresidente>();
			for (MinistroPresidente ministroPresidente : ministrosPresidentes) {
				mapaDeMinistros.put(ministroPresidente.getTipoOcorrencia(),
						ministroPresidente);
			}
			return mapaDeMinistros;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public MinistroPresidente recuperarMinistro(
			TipoOcorrenciaMinistro tipoOcorrenciaMinistro)
			throws ServiceException {
		try {
			return dao.recuperarMinistro(tipoOcorrenciaMinistro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public MinistroPresidente getMinistroPresidente() throws ServiceException {
		return getMapaDeMinistrosPresidentes().get(TipoOcorrenciaMinistro.MP);
	}

	public MinistroPresidente getMinistroVicePresidente() throws ServiceException {
		return getMapaDeMinistrosPresidentes().get(TipoOcorrenciaMinistro.VP);
	}


	@Override
	public MinistroPresidente recuperarMinistro(TipoOcorrenciaMinistro tipoOcorrenciaMinistro, Date dataPosse) throws ServiceException {
		try {
			return dao.recuperarMinistro(tipoOcorrenciaMinistro, dataPosse);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public MinistroPresidente recuperarMinistroPresidenteNoPeriodo(TipoOcorrenciaMinistro tipoOcorrenciaMinistro, Date dataInicio, Date dataFim) throws ServiceException {
		return dao.recuperarMinistroPresidenteNoPeriodo(tipoOcorrenciaMinistro, dataInicio, dataFim);
	}
	

}

/**
 * 
 */
package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.TipoVinculoObjeto;
import br.gov.stf.estf.entidade.processostf.VinculoObjeto;
import br.gov.stf.estf.processostf.model.dataaccess.VinculoObjetoDao;
import br.gov.stf.estf.processostf.model.service.VinculoObjetoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 27.07.2011
 */
@Service("vinculoObjetoService")
public class VinculoObjetoServiceImpl extends
		GenericServiceImpl<VinculoObjeto, Long, VinculoObjetoDao>  implements VinculoObjetoService {

	protected VinculoObjetoServiceImpl(VinculoObjetoDao dao) {
		super(dao);
	}

	@Override
	public List<VinculoObjeto> pesquisarPorVinculador(
			ObjetoIncidente<?> vinculador, TipoVinculoObjeto tipoVinculoObjeto) throws ServiceException {
		try {
			return dao.pesquisarPorVinculador(vinculador, tipoVinculoObjeto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<VinculoObjeto> pesquisarPorVinculado(
			ObjetoIncidente<?> vinculado, TipoVinculoObjeto tipoVinculoObjeto) throws ServiceException {
		try {
			return dao.pesquisarPorVinculado(vinculado, tipoVinculoObjeto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ObjetoIncidente<?>> recuperarObjetosIncidenteVinculados(
			ObjetoIncidente<?> vinculador, TipoVinculoObjeto tipoVinculoObjeto)
			throws ServiceException {
		try {
			return dao.recuperarObjetosIncidenteVinculados(vinculador, tipoVinculoObjeto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

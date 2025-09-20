package br.gov.stf.estf.usuario.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.TipoGrupoControle;
import br.gov.stf.estf.usuario.model.dataaccess.TipoGrupoControleDao;
import br.gov.stf.estf.usuario.model.service.TipoGrupoControleService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoGrupoControleService")
public class TipoGrupoControleServiceImpl extends GenericServiceImpl<TipoGrupoControle, Long, TipoGrupoControleDao>
	implements TipoGrupoControleService {
	
	public TipoGrupoControleServiceImpl(TipoGrupoControleDao dao) {
		super(dao);
	}

	public List<TipoGrupoControle> pesquisarTipoGrupoControle(String nomeGrupo)
			throws ServiceException {
		
		List<TipoGrupoControle> lista = null;
		
		try {
			lista  = dao.pesquisarTipoGrupoControle(nomeGrupo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return lista;
	}
}
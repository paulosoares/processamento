package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.TipoTagsLivresUsuarioDao;
import br.gov.stf.estf.documento.model.service.TipoTagsLivresUsuarioService;
import br.gov.stf.estf.entidade.documento.TipoTagsLivresUsuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoTagsLivresUsuarioService")
public class TipoTagsLivresUsuarioServiceImpl extends GenericServiceImpl<TipoTagsLivresUsuario, Long, TipoTagsLivresUsuarioDao> 
	implements TipoTagsLivresUsuarioService{

	public TipoTagsLivresUsuarioServiceImpl(TipoTagsLivresUsuarioDao dao){
		super(dao);
	}
	
	public List<TipoTagsLivresUsuario> pesquisar(String nome) throws ServiceException {
		
		List<TipoTagsLivresUsuario> lista= null;
		
		try {
			lista = dao.pesquisar(nome);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
		return lista;
	}
}


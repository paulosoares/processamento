package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.TagsLivresUsuarioDao;
import br.gov.stf.estf.documento.model.service.TagsLivresUsuarioService;
import br.gov.stf.estf.entidade.documento.TagsLivresUsuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;


@Service("tagsLivresUsuarioService")
public class TagsLivresUsuarioServiceImpl extends GenericServiceImpl<TagsLivresUsuario, Long, TagsLivresUsuarioDao> implements TagsLivresUsuarioService{
	
	public TagsLivresUsuarioServiceImpl(TagsLivresUsuarioDao dao){
		super(dao);
	}
	
	public List<TagsLivresUsuario> pesquisarNomeRotuloOuDescricao(String nomeRotulo, Long codTipoTag, String dscTag) throws ServiceException {
		
		List<TagsLivresUsuario> lista = null;
		
		try {
			lista  = dao.pesquisarNomeRotuloOuDescricao(nomeRotulo, codTipoTag, dscTag);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return lista;
	}

}

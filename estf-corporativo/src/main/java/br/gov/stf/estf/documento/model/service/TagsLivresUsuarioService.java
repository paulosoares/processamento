package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.TagsLivresUsuarioDao;
import br.gov.stf.estf.entidade.documento.TagsLivresUsuario;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TagsLivresUsuarioService extends GenericService<TagsLivresUsuario, Long, TagsLivresUsuarioDao>{
	
	public List<TagsLivresUsuario> pesquisarNomeRotuloOuDescricao(String nomeRotulo, Long codTipoTag, String dscTag) throws ServiceException;
	

}

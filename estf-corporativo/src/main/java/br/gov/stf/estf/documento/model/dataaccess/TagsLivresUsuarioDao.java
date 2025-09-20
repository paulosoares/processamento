package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.TagsLivresUsuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TagsLivresUsuarioDao extends GenericDao<TagsLivresUsuario, Long>{
	
	public List<TagsLivresUsuario> pesquisarNomeRotuloOuDescricao(String nomeRotulo, Long codTipoTag, String dscTag) throws DaoException;

}

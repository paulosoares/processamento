package br.gov.stf.estf.assinatura.service;

import java.util.List;

import br.gov.stf.estf.entidade.documento.TagsLivresUsuario;
import br.gov.stf.estf.entidade.documento.TipoTagsLivresUsuario;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.framework.exception.RegraDeNegocioException;

/**
 * Serviço local utilizado para realizar diversas operações sobre tags livres de usuário.
 * 
 * @author thiago.miranda
 * @since 3.16.1
 */
public interface TagsLivresServiceLocal {

	void alterarTagsAutoridades(TagsLivresUsuario tagsAutoridades) throws RegraDeNegocioException, ServiceLocalException;

	void excluirTiposTagsLivresUsuario(List<TipoTagsLivresUsuario> tiposTagsLivresUsuarios) throws RegraDeNegocioException, ServiceLocalException;

	List<TagsLivresUsuario> pesquisarTodasTagsAutoridades() throws RegraDeNegocioException, ServiceLocalException;
	
	List<TagsLivresUsuario> pesquisarTagsAutoridades(TagsLivresUsuario exemplo) throws RegraDeNegocioException, ServiceLocalException;

	List<TagsLivresUsuario> pesquisarTodasTagsLivres() throws RegraDeNegocioException, ServiceLocalException;
	
	List<TipoTagsLivresUsuario> pesquisarTodosTiposTagsLivresUsuario() throws RegraDeNegocioException, ServiceLocalException;

	List<TipoTagsLivresUsuario> pesquisarTiposTagsLivresUsuario(TipoTagsLivresUsuario exemplo) throws RegraDeNegocioException, ServiceLocalException;

	void salvarTagsAutoridades(TagsLivresUsuario tagsAutoridades) throws RegraDeNegocioException, ServiceLocalException;

	void salvarTiposTagsLivresUsuario(TipoTagsLivresUsuario tipoTagsLivresUsuario) throws RegraDeNegocioException, ServiceLocalException;
}

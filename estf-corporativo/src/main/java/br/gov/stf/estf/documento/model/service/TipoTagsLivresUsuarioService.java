package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.TipoTagsLivresUsuarioDao;
import br.gov.stf.estf.entidade.documento.TipoTagsLivresUsuario;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoTagsLivresUsuarioService extends GenericService<TipoTagsLivresUsuario, Long, TipoTagsLivresUsuarioDao>{
	
	
	/**
	 * Pesquisa os tipos de campos livres de preenchimento
	 * @return
	 * @throws ServiceException
	 */
	public List<TipoTagsLivresUsuario> pesquisar(String nome) throws ServiceException;

}

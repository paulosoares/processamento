package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.TipoPermissaoModeloComunicacaoDao;
import br.gov.stf.estf.entidade.documento.TipoPermissaoModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoPermissaoModeloComunicacaoService
		extends
		GenericService<TipoPermissaoModeloComunicacao, Long, TipoPermissaoModeloComunicacaoDao> {

	/**
	 * Obtém todas as permissões que podem ser acessadas pelo setor informado.
	 * 
	 * @param setor
	 * @return
	 * @throws ServiceException
	 */
	List<TipoPermissaoModeloComunicacao> pesquisarPermissoes(Setor setor,
			boolean incluirInstitucional) throws ServiceException;

	List<TipoPermissaoModeloComunicacao> pesquisarPermissoes(
			String descricaoPermissao, Boolean exatamenteIgual)
			throws ServiceException;
	
	TipoPermissaoModeloComunicacao recuperarPorId(Long idPermissao) throws ServiceException;
}

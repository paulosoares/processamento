package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.ModeloComunicacaoDao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ModeloComunicacaoService extends
		GenericService<ModeloComunicacao, Long, ModeloComunicacaoDao> {

	/**
	 * Método responsável em pesquisar os modelos existentes com os atributos
	 * informados.
	 * 
	 * @throws ServiceException
	 */
	List<ModeloComunicacao> pesquisar(String nomeModelo, Long tipoModelo,
			Long tipoPermissao, String flagAtivo) throws ServiceException;

	/**
	 * Método responsável em pesquisar os modelos existentes com os atributos
	 * informados.
	 * 
	 * Funciona do mesmo modo que {@link #pesquisar(String, Long, Long, String)}
	 * , mas as permissões serão recuperadas com base no setor informado.
	 * 
	 * @throws ServiceException
	 */
	List<ModeloComunicacao> pesquisar(String nomeModelo, Long tipoModelo,
			Setor setor, String flagAtivo) throws ServiceException;
	
	
	/**
	 * Busca os tipos de comunicacao pelo setor na tabela TIPO_PERMISSAO_MODELO
	 */
	List<TipoComunicacao> pesquisarTipoComunicacaoPeloSetorPermissao(Setor setor) 
		throws ServiceException;

	/**
	 * Método responsável em salvar um novo modelo de Documento criado pela
	 * Secretaria Judiciaria.
	 * 
	 * @throws ServiceException
	 */
	ModeloComunicacao incluirNovoDocumento(Long codigoTipoModelo, byte[] odt,
			String tipoArquivo) throws ServiceException;

	/**
	 * Pesquisar o modelo exato selecionado pelo usuário
	 * 
	 * @param nomeModelo
	 * @param idTipoModelo
	 * @throws ServiceException
	 */
	ModeloComunicacao pesquisarModeloEscolhido(Long idModelo, Long idTipoModelo)
			throws ServiceException;

	List<TipoComunicacao> pesquisarTipoComunicacao() throws ServiceException;
}

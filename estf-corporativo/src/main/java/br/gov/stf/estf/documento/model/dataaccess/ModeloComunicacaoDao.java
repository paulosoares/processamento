package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ModeloComunicacaoDao extends
		GenericDao<ModeloComunicacao, Long> {

	List<ModeloComunicacao> pesquisar(String nomeModelo, Long tipoModelo,
			Long tipoPermissao, String flagAtivo) throws DaoException;

	List<ModeloComunicacao> pesquisar(String nomeModelo, Long tipoModelo,
			Setor setor, String flagAtivo) throws DaoException;

	/**
	 * Pesquisa modelo selecionado pelo usuário.
	 */
	ModeloComunicacao pesquisarModeloEscolhido(Long idModelo,
			TipoComunicacao tipo) throws DaoException;
	
	/**
	 * Busca os tipos de comunicacao pelo setor na tabela TIPO_PERMISSAO_MODELO
	 */
	 
	List<TipoComunicacao> pesquisarTipoComunicacaoPeloSetorPermissao(Setor setor) 
		throws DaoException;

	List<TipoComunicacao> pesquisarTipoComunicacao() throws DaoException;
}

package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.TipoComunicacaoDao;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoPermissaoModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoComunicacaoService extends GenericService<TipoComunicacao, Long, TipoComunicacaoDao> {

	TipoComunicacao pesquisarTipoModelo(Long idTipoModelo) throws ServiceException;

	List<TipoComunicacao> pesquisarListaTiposModelos(String nomeTipoModelo) throws ServiceException;

	/**
	 * Procura por um tipo de modelo com o nome e código de permissão informados.
	 * 
	 * @param nomeTipoModelo
	 * @param codigoTipoPermissao
	 * @return
	 * @throws ServiceException
	 */
	TipoComunicacao pesquisarUnicoTipoModelo(String nomeTipoModelo, Long codigoTipoPermissao) throws ServiceException;
	
	/**
	 * Procura tipos de modelos com base nos que podem ser acessados por
	 * usuários de um determinado setor.
	 * 
	 * @param codigoSetor
	 * @throws DaoException
	 * @see {@link TipoPermissaoModeloComunicacao}
	 */
	List<TipoComunicacao> pesquisarListaTiposModelos(String nomeTipoModelo, Setor setor) throws ServiceException;
	
	Long pesquisaNumeracaoUnicaModelo(Long idTipoComunicacao) throws ServiceException;
}

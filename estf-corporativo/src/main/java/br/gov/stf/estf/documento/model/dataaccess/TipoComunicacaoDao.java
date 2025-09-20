package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoPermissaoModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoComunicacaoDao extends GenericDao<TipoComunicacao, Long> {

	TipoComunicacao pesquisarTipoModelo(Long idTipoModelo) throws DaoException;

	List<TipoComunicacao> pesquisarListaTiposModelos(String nomeTipoModelo) throws DaoException;

	TipoComunicacao pesquisarUnicoTipoModelo(String nomeTipoModelo, Long codigoTipoPermissao) throws DaoException;

	/**
	 * Obtém todos os tipos de modelos que podem ser acessados por usuários de
	 * um determinado setor.
	 * 
	 * @param codigoSetor
	 * @throws DaoException
	 * @see {@link TipoPermissaoModeloComunicacao}
	 */
	public List<TipoComunicacao> pesquisarListaTiposModelos(String nomeTipoModelo, Setor setor) throws DaoException;
	
	Long pesquisaNumeracaoUnicaModelo(Long idTipoComunicacao) throws DaoException;
}

package br.gov.stf.estf.expedicao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.VwEndereco;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface VwEnderecoDao {

	/**
	 * Busca o registro pelo id informado.
	 * @param id
	 * @return
	 * @throws DaoException
	 */
    public VwEndereco recuperarPorId(Long id) throws DaoException;

    /**
     * Pesquisa todos os registros de endereço que possuírem este número no CEP.
     *
     * @param cep
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<VwEndereco> pesquisar(String cep) throws DaoException;

    /**
     * Pesquisa todos os registros de endereço que possuírem os dados existentes no
     * objeto informado como modelo.
     *
     * @param vwEndereco
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<VwEndereco> pesquisar(VwEndereco vwEndereco) throws DaoException;
}
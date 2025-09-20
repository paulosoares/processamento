package br.gov.stf.estf.corp.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.corp.entidade.Municipio;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface MunicipioDao {

	/**
	 * Busca o registro pelo id informado.
	 * @param id
	 * @return
	 * @throws DaoException
	 */
    public Municipio recuperarPorId(String id) throws DaoException;

    /**
     * Lista todos os registros de Municipio ativos.
     *
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<Municipio> listarAtivos() throws DaoException;

    /**
     * Lista todos os registros de Municipio ativos que estiverem
     * vinculados à sigla de UF informada.
     *
     * @param siglaUf
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<Municipio> listarAtivos(String siglaUf) throws DaoException;

    /**
     * Lista todos os registros de Municipio ativos que estiverem
     * vinculados à sigla de UF informada, e que forem do tipo Município.
     *
     * @param siglaUf
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<Municipio> listarAtivosTipoMunicipio(String siglaUf) throws DaoException;

    /**
     * Busca o registro de Municipio ativos que estiver
     * vinculado à sigla de UF informada e possuirem o nome informado.
     *
     * @param siglaUf
     * @param nome
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    Municipio listarAtivo(String siglaUf, String nome) throws DaoException;

    /**
     * Lista todos os registros de Municipio ativos que estiverem
     * vinculados à sigla de UF informada e possuirem o nome informado,
     * e que forem do tipo Município.
     *
     * @param siglaUf
     * @param nome
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    Municipio listarAtivoTipoMunicipio(String siglaUf, String nome) throws DaoException;
}
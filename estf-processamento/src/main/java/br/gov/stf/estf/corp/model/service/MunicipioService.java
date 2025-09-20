package br.gov.stf.estf.corp.model.service;

import java.util.List;

import br.gov.stf.estf.corp.entidade.Municipio;
import br.gov.stf.framework.model.service.ServiceException;

public interface MunicipioService {

	/**
	 * Busca o registro pelo id informado.
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
    public Municipio recuperarPorId(String id) throws ServiceException;

    /**
     * Lista todos os registros de Municipio ativos.
     *
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    List<Municipio> listarAtivos() throws ServiceException;

    /**
     * Lista todos os registros de Municipio ativos que estiverem
     * vinculados à sigla de UF informada.
     *
     * @param siglaUf
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    List<Municipio> listarAtivos(String siglaUf) throws ServiceException;

    /**
     * Lista todos os registros de Municipio ativos que estiverem
     * vinculados à sigla de UF informada, e que forem do tipo Município.
     *
     * @param siglaUf
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    List<Municipio> listarAtivosTipoMunicipio(String siglaUf) throws ServiceException;

    /**
     * Busca o registro de Municipio ativos que estiver
     * vinculado à sigla de UF informada e possuirem o nome informado.
     *
     * @param siglaUf
     * @param nome
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    Municipio listarAtivo(String siglaUf, String nome) throws ServiceException;

    /**
     * Busca o registro de Municipio ativos que estiver
     * vinculado à sigla de UF informada e possuirem o nome informado,
     * e que forem do tipo Município.
     *
     * @param siglaUf
     * @param nome
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    Municipio listarAtivoTipoMunicipio(String siglaUf, String nome) throws ServiceException;

    /**
     * Retorna o endereço correspondente, caso ele seja um Município. Caso não seja,
     * buscará o registro de tipo Município correspondente.
     *
     * @param id
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    Municipio buscarMunicipioCorrespondente(String id) throws ServiceException;
}
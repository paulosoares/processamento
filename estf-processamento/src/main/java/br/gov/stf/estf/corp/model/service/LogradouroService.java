package br.gov.stf.estf.corp.model.service;

import java.util.List;

import br.gov.stf.estf.corp.entidade.Logradouro;
import br.gov.stf.framework.model.service.ServiceException;

public interface LogradouroService {

    /**
     * Pesquisa todos os registros de logradouro que possu�rem este n�mero no CEP.
     *
     * @param cep
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    List<Logradouro> pesquisarPeloCep(String cep) throws ServiceException;
}
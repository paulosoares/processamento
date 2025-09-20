package br.gov.stf.estf.corp.model.service;

import java.util.List;

import br.gov.stf.estf.corp.entidade.UnidadeFederacao;
import br.gov.stf.framework.model.service.ServiceException;

public interface UnidadeFederacaoService {

    /**
     * Lista todos os registros de unidade de federação ativos.
     *
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    List<UnidadeFederacao> listarAtivos() throws ServiceException;
}
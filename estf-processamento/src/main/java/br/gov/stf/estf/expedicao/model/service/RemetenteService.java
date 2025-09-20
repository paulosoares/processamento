package br.gov.stf.estf.expedicao.model.service;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.Remetente;
import br.gov.stf.framework.model.service.ServiceException;

public interface RemetenteService {

    /**
     * Lista todos os remetentes cadastraados.
     *
     * @return 
     * @throws br.gov.stf.framework.model.service.ServiceException 
     */
    List<Remetente> listarTodos() throws ServiceException;
}
package br.gov.stf.estf.expedicao.model.service;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.UnidadePostagem;
import br.gov.stf.framework.model.service.ServiceException;

public interface UnidadePostagemService {

    /**
     * Lista todos os registros de unidade de postagem ativos, ordenado pelo
     * status de 'principal', iniciando pelo(s) que estiver(em) com esta
     * marcação.
     *
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    List<UnidadePostagem> listarAtivos() throws ServiceException;
}
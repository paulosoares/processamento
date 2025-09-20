package br.gov.stf.estf.expedicao.model.service;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.VwEndereco;
import br.gov.stf.framework.model.service.ServiceException;

public interface VwEnderecoService {

    /**
     * Pesquisa todos os registros de endere�o que possu�rem este n�mero no CEP.
     *
     * @param cep
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    List<VwEndereco> pesquisar(String cep) throws ServiceException;

    /**
     * Pesquisa todos os registros de endere�o que possu�rem os dados existentes no
     * objeto informado como modelo.
     *
     * @param vwEndereco
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    List<VwEndereco> pesquisar(VwEndereco vwEndereco) throws ServiceException;
}
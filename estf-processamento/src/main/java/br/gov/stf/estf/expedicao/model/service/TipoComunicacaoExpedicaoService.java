package br.gov.stf.estf.expedicao.model.service;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.TipoComunicacaoExpedicao;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoComunicacaoExpedicaoService {

    /**
     * Lista todos os tipos de comuncação cadastrados.
     *
     * @return 
     * @throws br.gov.stf.framework.model.service.ServiceException 
     */
    List<TipoComunicacaoExpedicao> listarTiposComunicacao() throws ServiceException;
}
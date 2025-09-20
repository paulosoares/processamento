package br.gov.stf.estf.expedicao.model.service;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.TipoServico;
import br.gov.stf.estf.expedicao.model.util.TipoEntregaEnum;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoServicoService {

    /**
     * Lista todos os tipos de serviço ativos cadastrados, filtrando pelo tipo
     * de entrega informado.
     *
     * @param tipoEntregaEnum {@link br.gov.stf.estf.expedicao.model.util.TipoEntregaEnum }
     * @return 
     * @throws br.gov.stf.framework.model.service.ServiceException 
     */
    List<TipoServico> listarTiposServicoPorTipoEntrega(TipoEntregaEnum tipoEntregaEnum) throws ServiceException;
}
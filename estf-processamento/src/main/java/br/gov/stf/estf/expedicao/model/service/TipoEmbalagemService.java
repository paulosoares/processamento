package br.gov.stf.estf.expedicao.model.service;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.TipoEmbalagem;
import br.gov.stf.estf.expedicao.model.util.TipoEmbalagemEnum;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoEmbalagemService {

	/**
     * Lista todos os tipos de embalagem cadastrados, do tipo informado.
     *
     * @param tipoServico {@link br.gov.stf.estf.expedicao.entidade.TipoEmbalagemEnum }
     * @return 
     * @throws br.gov.stf.framework.model.dataaccess.DaoException 
     */
    List<TipoEmbalagem> listarTiposServicoPorTipoEntrega(TipoEmbalagemEnum tipoEmbalagem) throws ServiceException;
}
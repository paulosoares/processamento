package br.gov.stf.estf.expedicao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.TipoEmbalagem;
import br.gov.stf.estf.expedicao.model.util.TipoEmbalagemEnum;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface TipoEmbalagemDao {

    /**
     * Lista todos os tipos de embalagem cadastrados, do tipo informado.
     *
     * @param tipoEmbalagem {@link br.gov.stf.estf.expedicao.entidade.TipoEmbalagemEnum }
     * @return 
     * @throws br.gov.stf.framework.model.dataaccess.DaoException 
     */
    List<TipoEmbalagem> listarTiposServicoPorTipoEntrega(TipoEmbalagemEnum tipoEmbalagem) throws DaoException;
}
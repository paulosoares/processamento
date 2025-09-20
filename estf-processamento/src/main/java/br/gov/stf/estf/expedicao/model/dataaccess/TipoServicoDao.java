package br.gov.stf.estf.expedicao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.TipoServico;
import br.gov.stf.estf.expedicao.model.util.TipoServicoEnum;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface TipoServicoDao {

    /**
     * Lista todos os tipos de serviço ativos cadastrados, filtrando pelo indicador
     * dos correios e pelo indicador de postagem, onde este ultimo poderá ser
     * desconsiderado na consulta, informando o valor 'null'.
     *
     * @param isEntregaCorreios {@link Boolean }
     * @param tipoServico {@link br.gov.stf.estf.expedicao.entidade.TipoServico }
     * @return 
     * @throws br.gov.stf.framework.model.dataaccess.DaoException 
     */
    List<TipoServico> listarTiposServicoPorTipoEntrega(boolean isEntregaCorreios, TipoServicoEnum tipoServico) throws DaoException;
}
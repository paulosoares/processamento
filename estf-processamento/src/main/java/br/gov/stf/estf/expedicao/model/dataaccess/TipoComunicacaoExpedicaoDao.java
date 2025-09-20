package br.gov.stf.estf.expedicao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.TipoComunicacaoExpedicao;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface TipoComunicacaoExpedicaoDao {

    /**
     * Lista todos os tipos de comuncação cadastrados.
     *
     * @return 
     * @throws br.gov.stf.framework.model.dataaccess.DaoException 
     */
    List<TipoComunicacaoExpedicao> listarTiposComunicacao() throws  DaoException;
}
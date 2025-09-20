package br.gov.stf.estf.expedicao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.Remessa;
import br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface RemessaDao extends GenericDao<Remessa, Long> {

    /**
     * Realiza uma pesquisa baseada na "imagem" do registro informada, buscando
     * por todas as informa��es passadas na imagem nos registros do reposit�rio,
     * cada um no seu respectivo campo, em qualquer posi��o deste.
     *
     * @param pesquisaListaRemessaDto {@link br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto
     * }
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<Remessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto) throws DaoException;

    /**
     * Realiza uma pesquisa baseada na "imagem" do registro informada, buscando
     * por todas as informa��es passadas na imagem nos registros do reposit�rio,
     * cada um no seu respectivo campo, em qualquer posi��o deste.
     * Retorna apenas remessas enviadas.
     *
     * @param pesquisaListaRemessaDto {@link br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto }
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<Remessa> pesquisarEnviadas(PesquisaListaRemessaDto pesquisaListaRemessaDto) throws DaoException;
}
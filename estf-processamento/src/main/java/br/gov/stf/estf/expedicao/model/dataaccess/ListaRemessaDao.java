package br.gov.stf.estf.expedicao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.ListaRemessa;
import br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ListaRemessaDao extends GenericDao<ListaRemessa, Long> {

    /**
     * Realiza uma pesquisa baseada na "imagem" do registro informada, buscando
     * por todas as informações passadas na imagem nos registros do repositório,
     * cada um no seu respectivo campo, em qualquer posição deste.
     *
     * @param pesquisaListaRemessaDto {@link br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto }
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<ListaRemessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto) throws DaoException;

    /**
     * Realiza uma pesquisa baseada na "imagem" do registro informada, buscando
     * por todas as informações passadas na imagem nos registros do repositório,
     * cada um no seu respectivo campo, em qualquer posição deste.
     * Retorna apenas remessas enviadas.
     *
     * @param pesquisaListaRemessaDto {@link br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto }
     * @param enviada
     *
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<ListaRemessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto, Boolean enviada) throws DaoException;

    /**
     * Busca uma lista de remessa pelo número e ano informados.
     *
     * @param numeroListaRemessa
     * @param ano
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    ListaRemessa pesquisar(long numeroListaRemessa, int ano) throws DaoException;

    /**
     * Gera um número de lista de remessa para o ano informado. Este número será gerado de forma incremental
     * iniciando em 1, para o ano informado, ou seja, a cada ano a contagem será reiniciada.
     *
     * @param ano
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    long gerarNumeroListaRemessa(int ano) throws DaoException;
}
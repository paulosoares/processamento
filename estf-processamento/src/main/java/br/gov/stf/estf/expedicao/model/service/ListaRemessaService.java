package br.gov.stf.estf.expedicao.model.service;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.ListaRemessa;
import br.gov.stf.estf.expedicao.model.dataaccess.ListaRemessaDao;
import br.gov.stf.estf.expedicao.model.util.FinalizarRemessaDTO;
import br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ListaRemessaService extends GenericService<ListaRemessa, Long, ListaRemessaDao> {

    /**
     * Realiza uma pesquisa baseada na "imagem" do registro informada, buscando
     * por todas as informações passadas na imagem nos registros do repositório,
     * cada um no seu respectivo campo, em qualquer posição deste.
     *
     * @param pesquisaListaRemessaDto {@link br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto }
     * @return 
     * @throws br.gov.stf.framework.model.service.ServiceException 
     */
    List<ListaRemessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto) throws ServiceException;

    /**
     * Realiza uma pesquisa baseada na "imagem" do registro informada, buscando
     * por todas as informações passadas na imagem nos registros do repositório,
     * cada um no seu respectivo campo, em qualquer posição deste.
     *
     * @param pesquisaListaRemessaDto {@link br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto }
     * @param enviada
     *
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException 
     */
    List<ListaRemessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto, boolean enviada) throws ServiceException;

    /**
     * Busca uma lista de remessa pelo número e ano informados.
     *
     * @param numeroListaRemessa
     * @param ano
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException 
     */
    ListaRemessa pesquisar(long numeroListaRemessa, int ano) throws ServiceException;

	void finalizarListaRemessa(FinalizarRemessaDTO finalizarListaRemessaDTO) throws ServiceException, DaoException;

	void reabrirListaRemessa(ListaRemessa listaRemessa) throws ServiceException;
}